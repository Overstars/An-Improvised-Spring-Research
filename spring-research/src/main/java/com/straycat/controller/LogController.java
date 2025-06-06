package com.straycat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.straycat.data.VisitLog;
import com.straycat.service.LogStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class LogController {
    private static Logger logger = LoggerFactory.getLogger(LogController.class);

    private final LogStorageService storage;
    private final HttpServletRequest request;

    public LogController(LogStorageService storage,
                         HttpServletRequest request) {
        this.storage = storage;
        this.request = request;
    }
    /**
     * @description:  记录访问日志
     * @param params: 
     * @return: org.springframework.http.ResponseEntity<?>
     * @author: Overstars
     * @date: 2025/4/22 1:09
     */
    @PostMapping("/logs/visit")
    public ResponseEntity<?> logVisit (
        @RequestAttribute("traceNo") String traceNo,  // 直接获取属性值
        @RequestParam MultiValueMap<String, String> params,
        HttpServletRequest request  // 获取请求头
    ) {
        VisitLog log = new VisitLog();
        logger.info("RequestParam params = {}", params);
//        log.setId(UUID.randomUUID().toString());
        log.setId(traceNo);
        log.setIp(getClientIp(request));
        // 从请求参数中获取数据
        log.setUrl(params.getFirst("url"));
        log.setScreen(params.getFirst("screen"));

        // 从HTTP请求头中获取数据
        log.setReferrer(request.getHeader("Referer"));  // 引用来源
        log.setUa(request.getHeader("User-Agent"));     // 用户代理信息
        log.setLang(request.getHeader("Accept-Language")); // 语言偏好

        logger.info("VisitLog : {}", log);
        storage.storeLog(log);
        return ResponseEntity.ok().build();
    }
    /**
     * @description:  查询访问日志
     * @param start: 
     * @param end: 
     * @return: org.springframework.http.ResponseEntity<java.util.List < com.straycat.data.VisitLog>>
     * @author: Overstars
     * @date: 2025/4/22 1:20
     */
    @GetMapping("/logs/query")
    public ResponseEntity<List<VisitLog>> getLogs(
            @RequestParam(defaultValue = "0") long start,
            @RequestParam(defaultValue = "0") long end) {

        if (start == 0) start = System.currentTimeMillis() - 86400000; // 默认查最近24小时
        if (end == 0) end = System.currentTimeMillis();

        return ResponseEntity.ok(storage.queryLogs(start, end));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
