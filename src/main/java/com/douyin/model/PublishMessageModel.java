package com.douyin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author foanxi
 */
@Data
@AllArgsConstructor
public class PublishMessageModel {
    private MultipartFile data;
    private String title;
    private String token;
}
