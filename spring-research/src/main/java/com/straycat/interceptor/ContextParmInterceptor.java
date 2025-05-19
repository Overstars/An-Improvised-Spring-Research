package com.straycat.interceptor;

import com.straycat.utils.TraceNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ContextParmInterceptor implements HandlerInterceptor {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LocalDate date = LocalDate.now(); // get the current date
        TraceNoGenerator idGenerator = new TraceNoGenerator(0, 0);
        String traceNo = String.valueOf(idGenerator.generateUniqueId());

        if (!(handler instanceof HandlerMethod)) {
            return true; // 非Controller方法直接放行
        }
        // 将traceNo放入请求属性中，以便后续使用
        request.setAttribute("traceNo", traceNo);

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
        // 打印所有请求参数
        printRequestParameters(wrappedRequest, traceNo);
        // 返回true才会继续执行，返回false则取消当前请求
        return true;
    }
    private void printRequestParameters(ContentCachingRequestWrapper request, String traceNo) {
        // 打印通用请求参数（包括GET参数和POST表单参数）
        Map<String, String[]> params = request.getParameterMap();
        logger.info("[{}] Request Parameters: {}", traceNo, formatParameterMap(params));

        // 处理非表单POST请求（如JSON）
        if (HttpMethod.POST.matches(request.getMethod())
                && request.getContentType() != null
                && !request.getContentType().contains("form")) {

            String requestBody = getRequestBody(request);
            logger.info("[{}] Request Body: {}", traceNo, requestBody);
        }
    }
    private String formatParameterMap(Map<String, String[]> paramMap) {
        StringBuilder sb = new StringBuilder();
        paramMap.forEach((key, values) -> {
            sb.append(key)
                    .append("=")
                    .append(Arrays.toString(values))  // 处理多值参数
                    .append(", ");
        });
        return sb.length() > 0 ?
                sb.substring(0, sb.length() - 2) :  // 移除末尾的 ", "
                "{}";
    }
    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] buf = request.getContentAsByteArray();
        if (buf.length > 0) {
            try {
                return new String(buf, request.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                return "[UNKNOWN ENCODING]";
            }
        }
        return "";
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
