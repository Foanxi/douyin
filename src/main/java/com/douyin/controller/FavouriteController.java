package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.model.FavouriteModel;
import com.douyin.model.VideoModel;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        log.info("getFavouriteList enter param token:{},userId:{}", token, userId);
        if (JwtHelper.isExpiration(token)) {
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        List<VideoModel> list = favouriteService.getVideoByUser(userId, token);
        JSONObject jsonObject = CreateJson.createJson(200, 0, "用户点赞视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("getFavouriteList return json:{}", JSONObject.toJSONString(jsonObject, true));
        return jsonObject;
    }

    /**
     * param token 用于判断用户是否已登陆以及获取用户Id
     * param videoId 被点赞的视频Id
     * param actionType 1 表示点赞，2表示取消点赞
     * return
     */
    @PostMapping("/action")
    public JSON favouriteAction(@RequestParam("token") String token,
                                @RequestParam("video_id") String videoId,
                                @RequestParam("action_type") String actionType) {
        log.info("favouriteAction enter param token: {},videoId: {},actionType: {}", token, videoId, actionType);
        final String addType = "1";
        final String deleteType = "2";
        JSONObject json = null;
        // 校验token
        if (JwtHelper.isExpiration(token)) {
            log.warn("favouriteAction token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "token失效");
        }
        Long userId = JwtHelper.getUserId(token);
        Long videoIdLong = Long.parseLong(videoId);
        String exchange = "douyin.favouriteExchange";
        FavouriteModel favouriteModel = new FavouriteModel(videoIdLong, userId);
        if (Objects.equals(actionType, addType)) {
            // 说明用户点赞，首先先在点赞表中创建新的点赞
            String doKey = "favourite.do";
            try {
                rabbitTemplate.convertAndSend(exchange, doKey, favouriteModel);
                json = CreateJson.createJson(200, 0, "点赞成功");
                log.info("favouriteAction return Json: {}", JSONObject.toJSONString(json, true));
                return json;
            } catch (AmqpException e) {
                json = CreateJson.createJson(200, 1, "点赞失败");
                log.warn("favouriteAction operation failed");
                return json;
            }
        } else if (Objects.equals(actionType, deleteType)) {
            // 说明用户取消点赞，首先先删除点赞表中的点赞列
            String cancelKey = "favourite.cancel";
            try {
                rabbitTemplate.convertAndSend(exchange, cancelKey, favouriteModel);
                log.info("favouriteAction return Json:{}", JSONObject.toJSONString(json, true));
                json = CreateJson.createJson(200, 0, "取消点赞成功");
                return json;
            } catch (AmqpException e) {
                log.warn("favouriteAction cancelOperation failed");
                json = CreateJson.createJson(200, 1, "取消点赞失败");
                return json;
            }
        } else {
            log.warn("favouriteAction operation failed");
            return CreateJson.createJson(200, 1, "操作失败");
        }

    }
}

