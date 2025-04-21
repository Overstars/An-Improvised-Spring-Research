package com.straycat.data;

import lombok.Data;

@Data
public class VisitLog {
    private String id;         // UUID
    private String ip;         // 客户端IP
    private String url;       // 访问路径
    private String referrer;   // 来源页面
    private String ua;         // UserAgent
    private String screen;     // 屏幕分辨率
    private String lang;       // 语言
    private long timestamp;    // 时间戳
}
