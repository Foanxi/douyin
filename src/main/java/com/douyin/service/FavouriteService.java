package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Favourite;

public interface FavouriteService extends IService<Favourite> {
    boolean getIsFavourite(String userId, Long id);
}
