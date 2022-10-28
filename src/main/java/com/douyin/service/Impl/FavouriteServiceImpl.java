package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.pojo.Favourite;
import com.douyin.service.FavouriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("FavouriteServiceImpl")
@Transactional
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {
}
