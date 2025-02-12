package com.straycat.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.straycat.data.ImageMeta;
import org.iq80.leveldb.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
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

    @Autowired
    public ImageController(DB levelDB) {
        this.levelDB = levelDB;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        // 校验文件类型
        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity.badRequest().body("仅允许上传图片文件");
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
        String imageUrl = "http://localhost:8081/images/" + storedFilename;
        return ResponseEntity.ok(imageUrl);
    }
}
