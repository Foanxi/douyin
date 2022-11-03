package com.douyin.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Favourite;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public interface FavouriteService extends IService<Favourite> {
    boolean getIsFavourite(String userId, Long id);
}
