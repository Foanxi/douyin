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

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
    public JSON videoFeed(HttpServletResponse httpResponse,
                          @RequestParam("latest_time") String latestTime,
                          @RequestParam(value = "token", required = false) String token) {
        if (JwtHelper.isExpiration(token)) {
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }
        Map<String, Object> map = videoService.feedVideo(latestTime);
        if (map == null) {
            return CreateJson.createJson(200, 1, "当前无视频");
        } else {
            List<VideoModel> videoModelList = (List<VideoModel>) map.get("videoModelList");
            Timestamp nextTime = (Timestamp) map.get("nextTime");
            JSONObject jsonObject;
            log.info("latestTime :{}", latestTime);
            httpResponse.setStatus(200);
            jsonObject = CreateJson.createJson(200, 0, "视频流获取成功");
            jsonObject.put("video_list", videoModelList);
            jsonObject.put("next_time", nextTime.getTime());
            log.info("视频流" + jsonObject.toJSONString());
            return jsonObject;
        }

    }
}
