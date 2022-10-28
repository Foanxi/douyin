package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.User;

public interface UserService extends IService<User> {
    User getUser(String userId);
}
