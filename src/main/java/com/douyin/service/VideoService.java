package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Video;

import java.util.List;
import java.util.Map;


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
    List<Video> getVideo(Long userId);

    /**
     * 返回视频流的视频集合
     *
     * @param latestTime 上次返回的视频时间
     * @return 视频集合
     */
    Map<String, Object> feedVideo(String latestTime);

    /**
     * 通过用户Id获取当前用户的发布列表
     *
     * @Param: Long userId 用户id
     * @Return: List<VideoModel>
     */
    List<VideoModel> getVideoById(Long userId);

}
