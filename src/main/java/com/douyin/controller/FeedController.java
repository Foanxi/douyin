package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.douyin.pojo.Video;
import com.douyin.service.CommentService;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public JSON videoFeed(@RequestParam("latest_time") String latestTime,
                          @RequestParam("token") String token) {
        JSONObject jsonObject = new JSONObject();
        log.info("latestTime :{}",latestTime);
//        if (token != null && JwtHelper.isExpiration(token)){
//            Long userId = JwtHelper.getUserId(token);
//            Favourite byId = favouriteService.getById(userId);
//        }
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> list;
        if ("0".equals(latestTime)) {
            queryWrapper.last("limit 20");
            list = videoService.list(queryWrapper);
        } else {
            queryWrapper.le("create_time", latestTime).last("limit 20");
            list = videoService.list(queryWrapper);
        }
        for (Video video : list) {
            video.setPlayUrl(ipPath + video.getPlayUrl());
            video.setCoverUrl(ipPath + video.getCoverUrl());
        }
        long createTime = list.get(list.size() - 1).getCreateTime();

        jsonObject.put("status_code", 200);
        jsonObject.put("status_msg", "获取视频流成功");
        jsonObject.put("next_time", (int)createTime);
        jsonObject.put("video_list", list);
        System.out.println(jsonObject);
        return jsonObject;

    }
}
