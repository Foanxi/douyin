package com.douyin.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author foanxi
 */
@Slf4j
public class Interceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @param request  被拦截的请求
     * @param response 返回的响应
     * @param handler  处理器
     * @return 放行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 获取请求的URL
        log.info("请求URL:{}", request.getRequestURL().toString());
        // 获取请求的查询串
        log.info(request.getQueryString());
        return true;
    }
}

