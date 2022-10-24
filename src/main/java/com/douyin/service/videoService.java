package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.VideoMapper;
import com.douyin.pojo.Video;
import com.douyin.service.impl.videoServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: zhuanghaoxin
 * @Description:
 * @create: 2022-10-23 15:00
 **/
@Service
public class videoService extends ServiceImpl<VideoMapper, Video> implements videoServiceImpl {
}
