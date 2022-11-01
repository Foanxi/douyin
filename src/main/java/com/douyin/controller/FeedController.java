package com.douyin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.douyin.model.UserModel;
import com.douyin.model.VideoModel;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.CommentService;
import com.douyin.service.FavouriteService;
import com.douyin.service.VideoService;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
    public JSON videoFeed(HttpServletResponse httpResponse,
                          @RequestParam("latest_time") String latestTime,
                          @RequestParam("token") String token) {
        JSONObject jsonObject = new JSONObject();
        log.info("latestTime :{}", latestTime);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        List<Video> list;
        if ("0".equals(latestTime)) {
            queryWrapper.last("limit 20");
            list = videoService.list(queryWrapper);
        } else {
            queryWrapper.le("create_time", latestTime).last("limit 20");
            list = videoService.list(queryWrapper);
        }
        System.out.println(list.toString());
        for (Video video : list) {
            video.setPlayUrl(ipPath + video.getPlayUrl());
            video.setCoverUrl(ipPath + video.getCoverUrl());
        }
        if (list.size()-1==-1){
            jsonObject.put("status_code",1);
            jsonObject.put("status_msg","当前无视频");
            return jsonObject;
        }
        long createTime = list.get(list.size() - 1).getCreateTime();
        VideoModel[] videoList = new VideoModel[list.size()+1];
        UserModel user = new UserModel(1L, "Te1stUser", 0, 0, false);
        UserModel user2 = new UserModel(1L, "Te2stUser", 0, 0, false);
        videoList[0] = new VideoModel(1, user,
                "https://www.w3schools.com/html/movie.mp4",
                "https://cdn.pixabay.com/photo/2016/03/27/18/10/bear-1283347_1280.jpg",0,0,false);
        for (int i = 0; i < list.size(); i++) {
            Video video = list.get(i);
            VideoModel videoModel = new VideoModel(video.getId(),user2,video.getPlayUrl(),video.getCoverUrl(),0,0,false);
            videoModel.setPlay_url(list.get(i).getPlayUrl());
            videoModel.setCover_url(list.get(i).getCoverUrl());
            videoList[i+1] = videoModel;
        }
        httpResponse.setStatus(200);

        jsonObject.put("http_status",200);
        jsonObject.put("status_code", 0);
        jsonObject.put("video_list", videoList);
        jsonObject.put("next_time",createTime);
        System.out.println(jsonObject);
        return jsonObject;

    }
}
