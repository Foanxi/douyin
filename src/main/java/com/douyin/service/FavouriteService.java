package com.douyin.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Favourite;

public interface FavouriteService extends IService<Favourite> {
    JSON getFavouriteListService(String userId);
}
