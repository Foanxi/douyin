package com.douyin.util;

import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author hongxiaobin
 * @Time 2022/12/11-15:32
 */
@Component
@Slf4j
public class OssClient {
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
        return "https://" + bucketName + "." + endpoint;
    }

    /**
     * 获取OSSClient对象
     *
     * @Param:
     * @Return: com.aliyun.oss.OSSClient
     */
    public static com.aliyun.oss.OSSClient ossClientInitialization() {
        return new com.aliyun.oss.OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 判断是否存在bucketName
     *
     * @Param: OSSClient OSSClient对象
     * @Return: boolean
     */
    private static boolean hasBucket(com.aliyun.oss.OSSClient ossClient) {
        return ossClient.doesBucketExist(bucketName);
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param data     文件流
     * @param fileName 文件名称 完整文件名包括后缀名 如picture/1.png
     * @return 返回访问路径
     */
    public static String uploadSource(InputStream data, String fileName) {
        String resultPath = "";
        com.aliyun.oss.OSSClient ossClient = ossClientInitialization();
        try {
            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(data.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName.substring(fileName.lastIndexOf("."))));
            metadata.setContentDisposition("inline;filename=" + fileName);

            //上传文件
            ossClient.putObject(bucketName, fileName, data, metadata);
            //返回访问路径
            resultPath = getStartStaff() + "/" + fileName;
            log.info("OssClient.uploadSource", "File upload succeeded");
        } catch (IOException e) {
            log.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
        } finally {
            ossClient.shutdown();
        }
        return resultPath;
    }

    /**
     * 判断OSS服务文件上传时文件的contentType
     *
     * @Param: filename 文件名
     * @Return: String 文件类型对应的contentType
     */
    public static String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".jpg".equalsIgnoreCase(fileExtension)) {
            return "image/jpg";
        }
        if (".png".equalsIgnoreCase(fileExtension)) {
            return "image/png";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        if (".mp4".equalsIgnoreCase(fileExtension)) {
            return "video/mp4";
        }
        if (".mp3".equalsIgnoreCase(fileExtension)) {
            return "audio/mp3";
        }
        return "text/html";
    }

}
