package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.VideouserMapper;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.pojo.Videouser;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.service.VideouserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author hongxiaobin
 * @Time 2022/11/3-16:20
 */
@Service("VideouserServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class VideouserServiceImpl extends ServiceImpl<VideouserMapper, Videouser> implements VideouserService {
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;

    @Override
    public List<Videouser> getVideouserList(String userId) {
        List<Videouser> list = new ArrayList<>();
        User user = userService.getUserById(userId);
        List<Video> videoList = videoService.getVideo(userId);
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
        return list;
    }
}
