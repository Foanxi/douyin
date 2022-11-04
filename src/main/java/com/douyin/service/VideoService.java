package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Video;

import java.util.List;


/**
 * @author foanxi, zhuanghaoxin
 */
public interface VideoService extends IService<Video> {
    /**
     * 获取对应的用户id的视频列表
     *
     * @param userId 用户id
     * @return 返回视频列表
     */
    List<Video> getVideo(String userId);

    /**
     * 通过用户id获取该用户的视频列表
     *
     * @param userId 用户id
     * @return 返回于该用户相关的所有视频列表
     */
    VideoModel[] getVideoByUser(String userId);

    void updateVideoFavourite(Long videoId, Long userId, String actionType);
}
