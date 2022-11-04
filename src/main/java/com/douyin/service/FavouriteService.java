package com.douyin.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Favourite;

public interface FavouriteService extends IService<Favourite> {
    boolean getIsFavourite(String userId, Long id);
//    JSON getFavouriteListService(String userId);
    Favourite isExistFavourite(Long userId,Long videoId);
//    修改点赞记录，返回boolean值
    boolean updateFavourite(String actionType,Favourite favourite,Long userId,String videoId);
}
