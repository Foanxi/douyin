package com.douyin.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.pojo.Favourite;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.pojo.Videouser;
import com.douyin.service.FavouriteService;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongxiaobin
 */
@Slf4j
@Service("FavouriteServiceImpl")
@Transactional
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;

    @Override
    public JSON getFavouriteListService(String userId) {
        User user = userService.getUserById(userId);
        List<Video> videoList = videoService.getVideo(userId);
        ArrayList<Videouser> list = new ArrayList<>();
        for (Video video : videoList) {
            Videouser videouser = new Videouser(
                    video.getId(),
                    user,
                    video.getPlayUrl(),
                    video.getCoverUrl(),
                    video.getFavouriteCount(),
                    video.getCommentCount(),
                    video.getCreateTime(),
                    video.getTitle()
            );
            list.add(videouser);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("http_status", 200);
        jsonObject.put("status_code", 0);
        jsonObject.put("status_msg", "视频列表展示成功");
        jsonObject.put("video_list", list);
        log.info("返回的数据体为:{}", jsonObject);
        return jsonObject;
    }
}
