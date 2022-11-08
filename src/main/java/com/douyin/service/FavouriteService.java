package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.VideoModel;
import com.douyin.pojo.Favourite;

import java.util.List;

/**
 * @author zhuanghaoxin, hongxiaobin
 */
public interface FavouriteService extends IService<Favourite> {
    /**
     * 获取是否存在点赞关系
     *
     * @param userId  用户id
     * @param videoId 视频id
     * @return 返回是否存在点赞关系
     */
    Favourite isExistFavourite(Long userId, Long videoId);

    /**
     * 获取当前用户点赞过关系
     *
     * @param id 用户id
     * @return 返回用户点赞过的数据
     */
    List<Favourite> getFavouriteByVideoId(Long id);

    /**
     * 通过用户id获取该用户点赞的用户信息
     *
     * @param userId 用户id
     * @return 返回于该用户相关的所有视频列表
     */
    List<VideoModel> getVideoByUser(String userId);

    /**
     * 添加用户对该视频的点赞关系
     *
     * @param videoId 视频id
     * @param userId  用户id
     * @return 返回添加状态
     */
    boolean doFavourite(Long videoId, Long userId);

    /**
     * 删除用户对该视频的点赞关系
     *
     * @param videoId 视频id
     * @param userId  用户id
     * @return 返回删除状态
     */
    boolean cancelFavourite(Long videoId, Long userId);
}
