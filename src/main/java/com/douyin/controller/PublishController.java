package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.model.VideoModel;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hongxiaobin
 */
@RestController
@RequestMapping("/douyin/publish")
@Slf4j
public class PublishController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public JSON getPublishList(@RequestParam("token") String token,
                               @RequestParam("user_id") String userId) {
        log.info("getPublishList enter param token: {}, userId: {}", token, userId);
        if (JwtHelper.isExpiration(token)) {
            log.warn("getPublishList token: {} isExpiration", token);
            JSONObject jsonObject = CreateJson.createJson(200, 1, "token失效");
            jsonObject.put("user", null);
        }
        List<VideoModel> list = videoService.getPublishById(Long.valueOf(userId), token);
        log.info("getPublishList list: {}", list);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "发布视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("getPublishList out param json:{}", JSONObject.toJSONString(jsonObject, true));
        return jsonObject;
    }

    @PostMapping("/action")
    public JSON uploadVideo(MultipartFile data, @RequestParam("title") String title,
                            @RequestParam("token") String token) {
        JSONObject jsonObject;
        // 校验token
        if (JwtHelper.isExpiration(token)) {
            jsonObject = CreateJson.createJson(200, 1, "用户token已过期");
            return jsonObject;
        }
        boolean status = videoService.publishVideo(data, title, token);
        if (status) {
            jsonObject = CreateJson.createJson(200, 0, "视频上传成功");
        } else {
            jsonObject = CreateJson.createJson(200, 1, "视频上传失败");
        }
        return jsonObject;
    }
}
