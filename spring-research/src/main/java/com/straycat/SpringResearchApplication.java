package com.straycat;

//import com.straycat.utils.ParmApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.straycat.repository")
@EntityScan(basePackages = "com.straycat.data")
public class SpringResearchApplication {
    private static Logger logger = LoggerFactory.getLogger(SpringResearchApplication.class);
    public static void main(String[] args) {
        logger.info("Hello World!!!!");
        Map<String, Object> map = new HashMap<>();
        List<Object> list = new ArrayList<>();
        map.put("list1", list);
        Boolean flag1 = map instanceof Map;
        Boolean flag2 = map.get("list1") instanceof List;
        logger.info(flag1.toString());
        logger.info(flag2.toString());
//        String log = ParmApi.getParameter("description");
//        logger.info("description = " + log);
        SpringApplication.run(SpringResearchApplication.class, args);
    }
}
