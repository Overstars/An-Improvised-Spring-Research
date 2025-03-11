package com.straycat.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//通过filterName名来达到排序效果
@Component
@WebFilter(filterName="LogFilter",urlPatterns={"/*"})
public class LogFilter implements Filter {
    private  Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("过滤器[ {} ] 创建啦..., filterConfig = {}", this.getClass().getSimpleName(), filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("过滤器[ {} ] 执行啦...", this.getClass().getSimpleName());

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("过滤器[ {} ] 被摧毁啦...", this.getClass().getSimpleName());
    }

    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = servletRequest;
        HttpServletResponse response = servletResponse;
        long startTime = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } catch (IOException e) {
            logger.info(e.getMessage());
        } catch (ServletException e) {
            logger.info(e.getMessage());
        }
        String costTime = (System.currentTimeMillis() - startTime) + "";
        logger.info("costTime = {}", costTime);
    }
}
