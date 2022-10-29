package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.pojo.User;
import com.douyin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("UserServiceImpl")
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 根据账号返回用户信息
     * param: username
     * return:用户信息
     */
    @Override
    public User getUserByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",username);
        //查询该账号下的用户是否为空
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public User getUserById(String userId) {
        QueryWrapper<User> queryWrapper =  new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

}