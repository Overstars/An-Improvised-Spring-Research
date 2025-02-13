package com.straycat.action;

import com.straycat.service.baidu.TransService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author 16353 Overstars
 * @Date 2023/12/1 0:05
 * @Version 1.0.0
 * @Description
 */
@RestController
public class TranslateAction {
    private static Logger logger = LoggerFactory.getLogger(HelloAction.class);
    private TransService transService = new TransService();
    @GetMapping("translate")
    public Map<String, Object> translateToEnglish() {
        Map<String, Object> result = transService.getTranslation();
        logger.info(result.toString());
        return result;
    }
}
