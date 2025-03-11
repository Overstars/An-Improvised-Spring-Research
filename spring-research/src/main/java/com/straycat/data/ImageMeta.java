package com.straycat.data;

import java.time.LocalDateTime;
/**
 * @projectName: spring-research
 * @package: com.straycat.data
 * @className: ImageMeta
 * @author: Overstars
 * @description: TODO
 * @date: 2025/2/12 15:05
 * @version: 1.0
 */
public class ImageMeta {
    private String id;          // 图片唯一ID（如UUID）
    private String filename;    // 原始文件名
    private String filePath;    // 存储路径（如 /images/xxx.jpg）
    private long size;          // 文件大小（字节）
    private LocalDateTime uploadTime; // 上传时间
    // 必须有无参构造方法（JSON 反序列化需要）
    public ImageMeta() {}

    // Getter 和 Setter 方法
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }
}
