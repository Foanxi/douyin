package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.model.VideoModel;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/douyin/favorite")
@Slf4j
public class FavouriteController {

    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private VideoService videoService;

    /**
     * 点赞列表
     *
     * @Param:
     * @Return:
     */
    @GetMapping("/list")
    public JSON getFavouriteList(@RequestParam("token") String token,
                                 @RequestParam("user_id") String userId) {
        boolean expiration = JwtHelper.isExpiration(token);
        VideoModel[] list = videoService.getVideoUser(userId);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("返回的数据体为:{}", jsonObject);
        return jsonObject;
    }
}
