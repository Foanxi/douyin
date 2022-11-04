package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Favourite;

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
}
