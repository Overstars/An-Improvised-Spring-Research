package com.straycat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.straycat.data.ImageMeta;
import org.iq80.leveldb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

@RestController
public class ImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final DB levelDB;

    @Autowired
    private ObjectMapper objectMapper;  // 使用Spring依赖注入

    @Value("${image.storage.path}")
    private String imageStoragePath;

    @Value("${image.arts.path}") // 新增配置项
    private String artsBasePath;

    @Value("${network.domain}")
    private String networkDomain;

    @Value("${network.port}")
    private String networkPort;

    @Autowired
    public ImageController(DB levelDB) {
        this.levelDB = levelDB;
    }
    /**
     * @description: 上传图片
     * @param file:
     * @return: org.springframework.http.ResponseEntity<java.lang.String>
     * @author: Overstars
     * @date: 2025/2/13 10:04
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // 校验文件类型
        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body("仅允许上传图片文件");
        }
        // 创建图片存储路径
        File directory = new File(imageStoragePath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                logger.info("路径已创建: " + imageStoragePath);
            } else {
                logger.info("无法创建路径: " + imageStoragePath);
            }
        } else {
            logger.info("路径已存在: " + imageStoragePath);
        }

        // 生成唯一ID和存储路径
        String fileId = UUID.randomUUID().toString();
        String filename = file.getOriginalFilename();
        String fileExtension = filename.substring(filename.lastIndexOf("."));
        String storedFilename = fileId + fileExtension;
        Path filePath = Paths.get(imageStoragePath, storedFilename);


        logger.info(file.toString());

        // 保存图片file到文件系统filePath
        Files.copy(file.getInputStream(), filePath);

        // 存储元数据到 LevelDB
        ImageMeta meta = new ImageMeta();
        meta.setId(fileId);
        meta.setFilename(filename);
        meta.setFilePath(filePath.toString());
        meta.setSize(file.getSize());
        meta.setUploadTime(LocalDateTime.now());

        logger.info("meta = {}", meta);

        objectMapper.registerModule(new JavaTimeModule());

        byte[] metaBytes = objectMapper.writeValueAsBytes(meta); //Java对象meta序列化为字节数组
        levelDB.put(bytes(fileId), metaBytes);

        // 返回访问URL
        String imageUrl = "http://" + networkDomain + ":" + networkPort +"/images/" + storedFilename;
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/arts/**")
    public ResponseEntity<Resource> getArtsImages(HttpServletRequest request) {
        // 获取完整请求路径
        try {
            String encodedPath = request.getRequestURI().split("/arts/")[1];
            String decodedPath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);
            return handleImageRequest(decodedPath, artsBasePath, Integer.MAX_VALUE);
        } catch (ArrayIndexOutOfBoundsException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/images/**")
    public ResponseEntity<Resource> serveImage(HttpServletRequest request) {
        try {
            String encodedPath = request.getRequestURI().split("/images/")[1];
            String decodedPath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);
            return handleImageRequest(decodedPath, imageStoragePath, 2);
        } catch (ArrayIndexOutOfBoundsException e) {
            return ResponseEntity.notFound().build();
        }
    }
    private ResponseEntity<Resource> handleImageRequest(String requestedPath, String basePath, int maxDepth) {
        try {
            String normalizedRequestPath = requestedPath.replace('\\', '/').trim();
            if (normalizedRequestPath.isEmpty() || normalizedRequestPath.startsWith("/")) {
                logger.warn("非法路径访问: {}", requestedPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Path relativePath = Paths.get(normalizedRequestPath).normalize();
            if (relativePath.isAbsolute() || relativePath.startsWith("..") || relativePath.getNameCount() == 0 || relativePath.getNameCount() > maxDepth) {
                logger.warn("非法路径访问: {}", requestedPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            String filename = relativePath.getFileName().toString();
            if (!isAllowedImageExtension(filename)) {
                logger.warn("非法文件类型访问: {}", requestedPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Path base = Paths.get(basePath).toAbsolutePath().normalize();
            Path targetPath = base.resolve(relativePath).normalize();
            // 防止路径遍历攻击
            if (!targetPath.startsWith(base)) {
                logger.warn("非法路径访问: {}", requestedPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            if (!Files.exists(targetPath) || !Files.isRegularFile(targetPath, LinkOption.NOFOLLOW_LINKS)) {
                return ResponseEntity.notFound().build();
            }

            Path baseRealPath = base.toRealPath();
            Path targetRealPath = targetPath.toRealPath();
            if (!targetRealPath.startsWith(baseRealPath)) {
                logger.warn("检测到符号链接越权访问: {}", requestedPath);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            Resource resource = new UrlResource(targetRealPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = determineContentType(filename);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            logger.error("路径解析失败: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }



    // 根据文件名判断 Content-Type
    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }

    private boolean isAllowedImageExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex <= 0 || dotIndex == filename.length() - 1) {
            return false;
        }

        String extension = filename.substring(dotIndex + 1).toLowerCase();
        return "jpg".equals(extension)
                || "jpeg".equals(extension)
                || "png".equals(extension)
                || "gif".equals(extension)
                || "webp".equals(extension);
    }
}
