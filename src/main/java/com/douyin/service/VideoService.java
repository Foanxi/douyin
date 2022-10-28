package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Video;

import java.util.List;


public interface VideoService extends IService<Video> {
    List<Video> getVideo(String userId);
}
