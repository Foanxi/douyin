package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.pojo.User;
import com.douyin.service.impl.userServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: zhuanghaoxin
 * @Description:
 * @create: 2022-10-23 15:00
 **/
@Service
public class userService extends ServiceImpl<UserMapper, User> implements userServiceImpl{
}
