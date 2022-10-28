package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.mapper.VideoMapper;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("VideoServiceImpl")
@Transactional
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
}
