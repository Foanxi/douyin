package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Favourite;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/douyin/favorite")
public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private VideoService videoService;

    /**
     * 点赞操作
     * param token
     * param videoId
     * param actionType
     * return
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
    @PostMapping("/action")
    public JSONObject favouriteAction(@RequestParam("token")String token,
                                      @RequestParam("video_id") String videoId,
                                      @RequestParam("action_type") String actionType){
        log.info("传递的数据为：{}，{}，{}",token,videoId,actionType);
        JSONObject jsonObject = new JSONObject();
//        校验token
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            jsonObject.put("status_code", 404);
            jsonObject.put("status_msg", "token失效");
            jsonObject.put("user", null);
            return jsonObject;
        }
        Long userId = JwtHelper.getUserId(token);
        log.info("userId:{}",userId);
        //查询点赞表中是否存在该记录
        Favourite favourite = favouriteService.isExistFavourite(userId, Long.parseLong(videoId));
        log.info("点赞列表中是否存在该记录：{}",favourite);
//      用户如果尚未点赞，则数据库中添加新的数据，并返回点赞成功。
        if (favourite == null){
            log.info("用户尚未点赞");
            Favourite create = new Favourite(0L, userId, videoId, 1);
            favouriteService.save(create);
            jsonObject.put("http_status", 200);
            jsonObject.put("status_code", 0);
            jsonObject.put("status_msg", "点赞成功");
            log.info("返回的数据体为：{}", jsonObject);
            return jsonObject;
        }
//        修改点赞表中记录
        boolean update = favouriteService.updateFavourite(actionType, favourite,userId,videoId);
        if (!update){
            log.info("修改用户点赞失败");
            jsonObject.put("http_status", 500);
            jsonObject.put("status_code", 1);
            jsonObject.put("status_msg", "点赞失败");
            return jsonObject;
        }
        jsonObject.put("http_status", 200);
        jsonObject.put("status_code",0);
        jsonObject.put("status_msg","取消点赞成功");
        return jsonObject;
    }
}
