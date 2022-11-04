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

    boolean updateFavourite(String actionType, Favourite favourite, Long userId, String videoId);

    /**
     * 获取当前用户点赞过的视频id集合
     *
     * @Param:
     * @Return:
     */
    List<Favourite> getVideoId(Long id);

    /**
     * 通过用户id获取该用户点赞的用户信息
     *
     * @param userId 用户id
     * @return 返回于该用户相关的所有视频列表
     */
    List<VideoModel> getVideoByUser(String userId);
}
