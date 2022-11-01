package com.douyin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author hongxiaobin
 * @Time 2022/11/1-16:30
 */
@Configuration
public class fileConfig implements WebMvcConfigurer {

    @Value("${douyin.path}")
    private String resourcePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/picture/**").addResourceLocations("file:picture/");
        registry.addResourceHandler("/video/**").addResourceLocations("file:video/");
    }
}
