package com.douyin.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author hongxiaobin
 * @Time 2022/11/1-19:02
 */
@Controller
public class FileController {
    @GetMapping("/video/{userId}/{fileName}")
    public InputStream getVideo(@PathVariable("userId") String userId,
                                @PathVariable("fileName") String fileName) {
//        String path = "classpath:/public/" + userId + "/" + fileName;
        String path = "E:\\curriculum design\\douyin\\src\\main\\resources\\public\\video\\" + userId + "\\" + fileName;
        System.out.println(path);
        System.out.println("用户ID：" + userId + " 视频文件名：" + fileName);
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    @ResponseBody
    @GetMapping("/picture/{userId}/{fileName}")
    public void getPicture(@PathVariable("userId") String userId,
                           @PathVariable("fileName") String fileName,
                           HttpServletResponse response) {
        String path = "classpath:/public/" + userId + "/" + fileName;
        System.out.println("用户ID：" + userId + " 视频文件名：" + fileName);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", fileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                InputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = response.getOutputStream();
                IOUtils.copy(inputStream, outputStream);
                //刷新输出流
                outputStream.flush();
                //关闭输出流
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
