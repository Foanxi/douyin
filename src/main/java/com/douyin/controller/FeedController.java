package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.douyin.model.VideoModel;
import com.douyin.service.CommentService;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author foanxi
 */
@RestController
@Slf4j
@RequestMapping("/douyin/feed")
public class FeedController {


    @Autowired
    private VideoService videoService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FavouriteService favouriteService;

    @GetMapping("")
    public JSON videoFeed(@RequestParam("latest_time") String latestTime,
                          @RequestParam(value = "token", required = false) String token) {
        log.info("token：{}",token);
        if (token != null && JwtHelper.isExpiration(token)) {
            log.warn("videoFeed token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        Map<String, Object> map = videoService.feedVideo(latestTime,token);
        if (map == null) {
            log.warn("videoFeed is currently no video to play");
            return CreateJson.createJson(200, 1, "当前无视频");
        } else {
            List<VideoModel> videoModelList = (List<VideoModel>) map.get("videoModelList");
            Timestamp nextTime = (Timestamp) map.get("nextTime");
            JSONObject jsonObject;
            jsonObject = CreateJson.createJson(200, 0, "视频流获取成功");
            jsonObject.put("video_list", videoModelList);
            jsonObject.put("next_time", nextTime.getTime());
            log.info("videoFeed return json: {}", JSONObject.toJSONString(jsonObject, true));
            return jsonObject;
        }

    }
}
