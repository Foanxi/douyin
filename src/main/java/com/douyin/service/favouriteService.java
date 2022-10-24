package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.pojo.Favourite;
import com.douyin.service.impl.favouriteServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: zhuanghaoxin
 * @Description:
 * @create: 2022-10-23 15:00
 **/
@Service
public class favouriteService extends ServiceImpl<FavouriteMapper, Favourite> implements favouriteServiceImpl{
}
