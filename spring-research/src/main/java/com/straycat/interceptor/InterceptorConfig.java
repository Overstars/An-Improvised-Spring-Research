package com.straycat.interceptor;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 将上面自定义好的拦截器添加进去。
        registry.addInterceptor(new ContextParmInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
