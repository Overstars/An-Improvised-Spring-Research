package com.straycat.data;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

/**
 * @description: 
 * @param null: 
 * @return: 
 * @author: Overstars
 * @date: 2025/5/19 22:32
 */
@Data
@Entity
@Table(name = "straycat_visit_log")
public class VisitLog {
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(nullable = false, length = 45)
    private String ip;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String url;

    @Column(columnDefinition = "TEXT")
    private String referrer;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String ua;

    @Column(length = 20)
    private String screen;

    @Column(length = 10)
    private String lang;

    @Column(name = "create_time", nullable = false, updatable = false)
    private Instant createTime = Instant.now();

    @Column(name = "update_time", nullable = false)
    private Instant updateTime = Instant.now();

    // getters/setters 省略...
}