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

import java.util.List;
import java.util.Objects;

/**
 * @author hongxiaobin, zhuanghaoxin
 */
@Slf4j
@RestController
@RequestMapping("/douyin/favorite")

public class FavouriteController {
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private VideoService videoService;

    /**
     * 获取用户的点赞视频列表
     *
     * @param token  用户token
     * @param userId 用户id
     * @return 返回用户点赞过的视频
     */
    @GetMapping("/list")
    public JSON getFavouriteList(@RequestParam("token") String token,
                                 @RequestParam("user_id") String userId) {
        log.info("getFavouriteList enter param token: {}, userId: {}", token, userId);
        if (JwtHelper.isExpiration(token)) {
            log.warn("getFavouriteList token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        List<VideoModel> list = favouriteService.getVideoByUser(userId);
        log.info("getFavouriteList list: {}", list);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("getFavouriteList out param json:{}", jsonObject);
        return jsonObject;
    }

    @PostMapping("/action")
    public JSON favouriteAction(@RequestParam("token") String token,
                                @RequestParam("video_id") String videoId,
                                @RequestParam("action_type") String actionType) {
        final String addType = "1";
        final String deleteType = "2";

        // 校验token
        if (JwtHelper.isExpiration(token)) {
            return CreateJson.createJson(200, 1, "token失效");
        }
        Long userId = JwtHelper.getUserId(token);
        Long videoIdLong = Long.parseLong(videoId);

        // 获取某个视频的点赞数
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("user_id", userId);
        favouriteService.getOne(queryWrapper);
        if (Objects.equals(actionType, addType)) {
            // 说明用户点赞，首先先在点赞表中创建新的点赞
            boolean doFavourite = favouriteService.doFavourite(videoIdLong, userId);
            if (doFavourite) {
                CreateJson.createJson(200, 1, "点赞失败");
            }
            return CreateJson.createJson(200, 0, "点赞成功");
        } else if (Objects.equals(actionType, deleteType)) {
            // 说明用户取消点赞，首先先删除点赞表中的点赞列，
            boolean cancelFavourite = favouriteService.cancelFavourite(videoIdLong, userId);
            if (cancelFavourite) {
                return CreateJson.createJson(200, 1, "取消点赞失败");
            }
            return CreateJson.createJson(200, 0, "取消点赞成功");
        } else {
            return CreateJson.createJson(200, 1, "操作失败");
        }
    }
}

