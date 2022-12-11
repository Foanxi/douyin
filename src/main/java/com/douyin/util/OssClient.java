package com.douyin.util;

import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

/**
 * @Author hongxiaobin
 * @Time 2022/12/11-15:32
 */
@Component
public class OssClient {
    private static final Logger log = LoggerFactory.getLogger(OssClient.class);

    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        OssClient.endpoint = endpoint;
    }

    @Value("${oss.access-key-id}")
    public void setAccessKeyId(String accessKeyId) {
        OssClient.accessKeyId = accessKeyId;
    }

    @Value("${oss.access-key-secret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OssClient.accessKeySecret = accessKeySecret;
    }

    @Value("${oss.bucketname}")
    public void setBucketName(String bucketName) {
        OssClient.bucketName = bucketName;
    }

    public static String getStartStaff() {
        return "http://" + bucketName + "." + endpoint;
    }

    /**
     * 获取ossClient
     *
     * @return
     */
    public static com.aliyun.oss.OSSClient ossClientInitialization() {
        return new com.aliyun.oss.OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 判断是否存在bucketName
     */
    private static boolean hasBucket(com.aliyun.oss.OSSClient ossClient) {
        return ossClient.doesBucketExist(bucketName);
    }

    public static String createFileName(String mime) { // 需要创建一个文件名称
        return UUID.randomUUID() + "_" + mime + ".jpg";
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param data     文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public static String uploadImage(MultipartFile data, String fileName) {
        InputStream inputStream = null;
        inputStream = multipartToInputStream(data);
        String resultStr = "";
        //上传到指定文件夹
        fileName = "picture/" + fileName;
        try {
            /*
              创建OSS客户端
             */
            com.aliyun.oss.OSSClient ossClient = new com.aliyun.oss.OSSClient(endpoint, accessKeyId, accessKeySecret);
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(inputStream.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            metadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, fileName, inputStream, metadata);
            //解析结果
            resultStr = putResult.getETag();
            System.out.println(resultStr);
        } catch (IOException e) {
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        }
        return resultStr;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String filenameExtension) {
        if (".bmp".equalsIgnoreCase(filenameExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(filenameExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(filenameExtension) ||
                ".jpg".equalsIgnoreCase(filenameExtension) ||
                ".png".equalsIgnoreCase(filenameExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(filenameExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(filenameExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(filenameExtension)) {
            return "application/vnd.visio";
        }
        if (".pptx".equalsIgnoreCase(filenameExtension) ||
                ".ppt".equalsIgnoreCase(filenameExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".docx".equalsIgnoreCase(filenameExtension) ||
                ".doc".equalsIgnoreCase(filenameExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(filenameExtension)) {
            return "text/xml";
        }
        if (".mp4".equalsIgnoreCase(filenameExtension)) {
            return "video/mp4";
        }
        return "image/jpeg";
    }

    /**
     * MultipartFile转换为InputStream
     *
     * @param multipartFile 输入文件
     * @return 返回输入流
     */
    private static InputStream multipartToInputStream(MultipartFile multipartFile) {
        InputStream inputStream = null;
        File file = null;
        try {
            // 创建临时文件
            file = File.createTempFile("temp", null);
            // 把multipartFile写入临时文件
            multipartFile.transferTo(file);
            // 使用文件创建 inputStream 流
            inputStream = Files.newInputStream(file.toPath());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 最后记得删除文件
                file.deleteOnExit();
                // 关闭流
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return inputStream;
    }
}
