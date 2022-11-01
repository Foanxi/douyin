package com.douyin.controller;

import com.douyin.config.NonStaticResourceHttpRequestConfig;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author hongxiaobin
 * @Time 2022/11/1-19:02
 */
@Controller
public class FileController {

    @Value("${douyin.path}")
    private String resourcePath;

    @Autowired
    private NonStaticResourceHttpRequestConfig nonStaticResourceHttpRequestConfig;

    @GetMapping("/{type}/{userId}/{fileName}")
    public void getFile(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           @PathVariable("type") String type,
                           @PathVariable("userId") String userId,
                           @PathVariable("fileName") String fileName) throws IOException, ServletException {
        String path = resourcePath + "/" + type + "/" + userId + "/" + fileName;
        File file = new File(path);
        Path filePath = Paths.get(path);
        String mimeType = Files.probeContentType(filePath);
        httpServletResponse.setContentType(mimeType);
        httpServletResponse.addHeader("Content-Length", "" + file.length());
        httpServletRequest.setAttribute(NonStaticResourceHttpRequestConfig.ATTR_FILE, filePath);
        nonStaticResourceHttpRequestConfig.handleRequest(httpServletRequest, httpServletResponse);
    }
}
