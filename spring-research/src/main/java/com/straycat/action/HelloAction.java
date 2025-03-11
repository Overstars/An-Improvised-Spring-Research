package com.straycat.action;

import com.straycat.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloAction {
    private HelloService helloService = new HelloService();
    private static Logger logger = LoggerFactory.getLogger(HelloAction.class);
    @GetMapping("hello")
    public String hello() {
        String str = helloService.getHello();
        logger.info(str);
        return str;
    }
}
