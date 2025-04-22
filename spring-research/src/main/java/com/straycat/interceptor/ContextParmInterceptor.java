package com.straycat.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ContextParmInterceptor implements HandlerInterceptor {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LocalDate date = LocalDate.now(); // get the current date
        String traceNo = "10000" + date.format(DateTimeFormatter.BASIC_ISO_DATE);

        if (!(handler instanceof HandlerMethod)) {
            return true; // 非Controller方法直接放行
        }


        // 包装请求以支持重复读取
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        // 打印请求基本信息
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        logger.info("[{}] ==== 拦截方法: {}.{} ====",
                traceNo, method.getDeclaringClass().getSimpleName(), method.getName());
        logger.info("[{}] Request URL: {} {}",
                traceNo, wrappedRequest.getMethod(), wrappedRequest.getRequestURL());
        // 打印请求头
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, wrappedRequest.getHeader(headerName));
        }
        logger.debug("[{}] Headers: {}", traceNo, headers);
        logger.info("====交易流水：{}====", traceNo);
        // 返回true才会继续执行，返回false则取消当前请求
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("执行完方法之后进执行(Controller方法调用之后)，但是此时还没进行视图渲染");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse
            response, Object handler, Exception ex) throws Exception {
        logger.info("整个请求都处理完咯，DispatcherServlet也渲染了对应的视图咯，此时可以做一些清理的工作了");
    }
    public void onRequest(Context context) {
        // Do nothing because of forgotten.
        logger.info("context = {}", context);
        return;
    }
    public void onResponse(Context context) throws NamingException {
        String msg = String.format("交易结束%s", context.getNameInNamespace());
        logger.info(msg);
    }
}
