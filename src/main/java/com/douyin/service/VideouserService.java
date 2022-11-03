package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Videouser;

import java.util.List;

/**
 * @Author hongxiaobin
 * @Time 2022/11/3-16:20
 */
public interface VideouserService extends IService<Videouser> {
    List<Videouser> getVideouserList(String userId);
}
