package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Favourite;
import com.douyin.pojo.Video;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author hongxiaobin
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
        if (JwtHelper.isExpiration(token)) {
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        List<VideoModel> list = favouriteService.getVideoByUser(userId);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("返回的数据体为:{}", jsonObject);
        return jsonObject;
    }

    @PostMapping("/action")
    public JSON favouriteAction(@RequestParam("token") String token,
                                      @RequestParam("video_id") String videoId,
                                      @RequestParam("action_type") String actionType) {
        log.info("传递的数据为：{}，{}，{}", token, videoId, actionType);
//        校验token
        if (JwtHelper.isExpiration(token)) {
            JSONObject jsonObject = CreateJson.createJson(404, 1, "token失效");
            jsonObject.put("user", null);
            return jsonObject;
        }
        Long userId = JwtHelper.getUserId(token);
        Long videoIdLong = Long.parseLong(videoId);
        log.info("userId:{}", userId);
        //获取某个视频的点赞数
        QueryWrapper<Favourite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId).eq("user_id", userId);
        Favourite favourite = favouriteService.getOne(queryWrapper);
        log.info("favourite:{}", favourite);
        if (Objects.equals(actionType, "1")) {
            //说明用户点赞，首先先在点赞表中创建新的点赞
            boolean giveFavourite = favouriteService.giveFavourite(actionType, videoIdLong, userId);
            if (giveFavourite){CreateJson.createJson(500, 1, "点赞失败");}
            return CreateJson.createJson(200, 0, "点赞成功");
        } else if (Objects.equals(actionType, "2")) {
            //说明用户取消点赞，首先先删除点赞表中的点赞列，
            boolean notGiveFavourite = favouriteService.notGiveFavourite(actionType, videoIdLong, userId);
            if (notGiveFavourite){return CreateJson.createJson(500, 1, "取消点赞失败");}
            return CreateJson.createJson(200, 0, "取消点赞成功");
        }else {
            return CreateJson.createJson(400,1,"操作失败");
        }
    }
}

