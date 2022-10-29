package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.User;

public interface UserService extends IService<User> {
//    根据账号查询用户信息
    User getUserByUsername(String username);

    User getUserById(String userId);
}
