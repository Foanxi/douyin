package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.douyin.model.UserModel;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Video;
import com.douyin.service.CommentService;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author foanxi
 */
@RestController
@Slf4j
@RequestMapping("/douyin/feed")
public class FeedController {
    @Value("${douyin.ip_path}")
    private String ipPath;

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

        JSONObject jsonObject;
        log.info("latestTime :{}", latestTime);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> list;
        if ("0".equals(latestTime) || latestTime == null) {
            queryWrapper.last("limit 20");
            list = videoService.list(queryWrapper);
        } else {
            queryWrapper.last("limit 20");
            list = videoService.list(queryWrapper);
        }
        for (Video video : list) {
            video.setPlayUrl(ipPath + video.getPlayUrl());
            video.setCoverUrl(ipPath + video.getCoverUrl());
        }
        if (list.size() - 1 == -1) {
            jsonObject = CreateJson.createJson(400, 1, "当前无视频");
            return jsonObject;
        }
        long create = System.currentTimeMillis();
        VideoModel[] videoList = new VideoModel[list.size() + 1];
        UserModel user = new UserModel(1L, "Te1stUser", 0, 0, false);
        UserModel user2 = new UserModel(1L, "Te2stUser", 0, 0, false);
        videoList[0] = new VideoModel(1, user,
                "https://www.w3schools.com/html/movie.mp4",
                "https://cdn.pixabay.com/photo/2016/03/27/18/10/bear-1283347_1280.jpg", 0, 0, false, "");
        for (int i = 0; i < list.size(); i++) {
            Video video = list.get(i);
            VideoModel videoModel = new VideoModel(video.getId(), user2, video.getPlayUrl(), video.getCoverUrl(), 0, 0, false, "");
            videoModel.setPlayUrl(list.get(i).getPlayUrl());
            videoModel.setCoverUrl(list.get(i).getCoverUrl());
            videoList[i + 1] = videoModel;
        }
        httpResponse.setStatus(200);
        jsonObject = CreateJson.createJson(200, 0, "视频发布成");
        jsonObject.put("video_list", videoList);
        jsonObject.put("next_time", create);
        return jsonObject;
    }
}
