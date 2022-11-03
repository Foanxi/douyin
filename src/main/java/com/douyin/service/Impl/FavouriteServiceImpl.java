package com.douyin.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.pojo.Favourite;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.pojo.Videouser;
import com.douyin.service.FavouriteService;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongxiaobin
 */
@Slf4j
@Service("FavouriteServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
}
