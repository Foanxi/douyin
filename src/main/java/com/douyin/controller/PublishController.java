package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/douyin/publish")
public class PublishController {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;

    /**
     * 发布列表
     *
     * @Param:
     * @Return:
     */
    @GetMapping("/list")
    public JSON getUserList(@RequestParam("token") String token,
                            @RequestParam("user_id") String userId) {
        JSONObject jsonObject = new JSONObject();
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            jsonObject.put("status_code", 404);
            jsonObject.put("status_msg", "token失效");
            jsonObject.put("user", null);
            return jsonObject;
        }
        User user = userService.getUser(userId);
        List<Video> videoList = videoService.getVideo(userId);
        JSONObject jsonObjectVideo;
        jsonObjectVideo = (JSONObject) videoList;
        jsonObjectVideo.put("author",user);

        jsonObject.put("status_code", 200);
        jsonObject.put("status_msg", "视频列表展示成功");
        jsonObject.put("video_list",jsonObjectVideo);
        System.out.println(1);
        System.out.println(jsonObject);
        return jsonObject;
    }
}
