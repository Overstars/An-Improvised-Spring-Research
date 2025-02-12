package com.straycat.data;

import java.time.LocalDateTime;
/**
 * @projectName: muwpob-research
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
}
