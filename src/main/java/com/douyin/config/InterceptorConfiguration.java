package com.douyin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author foanxi
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    /**
     * 重写addInterceptors()实现拦截器
     * 配置：要拦截的路径以及不拦截的路径
     *
     * @param registry 注册设置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(new Interceptor());
        registration.addPathPatterns("/**");
    }

}

