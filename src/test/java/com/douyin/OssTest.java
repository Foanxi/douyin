package com.douyin;

import com.douyin.util.OssClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author hongxiaobin
 * @Time 2022/12/11-15:49
 */
@SpringBootTest
public class OssTest {

    @Test
    public void ossTest() {
        System.out.println(OssClient.ossClientInitialization());
    }

    @Test
    public void ossUploadTest() throws Exception {
        Path path = Paths.get("D:\\桌面\\WeChat_20221211201424.mp4");
        File file = new File(path.toUri());
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), fileInputStream);
    }

    @Test
    public void getImg() throws Exception {
        Path path = Paths.get("D:\\桌面\\59ea9ef27fe5d461542efedc1726471f.mp4");
        File file = new File(path.toUri());
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), fileInputStream);
//        InputStream inputStream = VideoProcessing.grabberVideoFramer(multipartFile.getInputStream());
        System.out.println("==============================" + multipartFile.getOriginalFilename());
        System.out.println("==============================" + multipartFile.getResource());
        System.out.println("==============================" + multipartFile.getContentType());
    }
}
