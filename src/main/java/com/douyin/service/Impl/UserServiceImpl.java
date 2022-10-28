package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.pojo.User;
import com.douyin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("UserServiceImpl")
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
