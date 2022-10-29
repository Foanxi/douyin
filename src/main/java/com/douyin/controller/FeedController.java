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
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

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
                          @RequestParam("token") String token) {
        JSONObject jsonObject = new JSONObject();
        log.info("latestTime :{}",latestTime);
//        if (token != null && JwtHelper.isExpiration(token)){
//            Long userId = JwtHelper.getUserId(token);
//            Favourite byId = favouriteService.getById(userId);
//        }
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> list;
        if ("".equals(latestTime)) {
            queryWrapper.last("limit 20");
            list = videoService.list(queryWrapper);
        } else {
            queryWrapper.le("create_time", latestTime).last("limit 20");
            list = videoService.list(queryWrapper);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String strNextTime = simpleDateFormat.format(list.get(list.size() - 1).getCreateTime());
        Integer nextTime = Integer.parseInt(strNextTime);

        jsonObject.put("status_code", 200);
        jsonObject.put("status_msg", "获取视频流成功");
        jsonObject.put("next_time", nextTime);
        jsonObject.put("video_list", list);
        System.out.println(jsonObject);
        return jsonObject;

    }
}
