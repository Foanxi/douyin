package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hongxiaobin
 */
@Service("UserServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RelationService relationService;

    /**
     * 根据账号返回用户信息
     * param: username
     * return:用户信息
     */
    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        //查询该账号下的用户是否为空
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public UserModel getUserById(Long id, Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        User user = baseMapper.selectOne(queryWrapper);
        boolean isFollow = relationService.getIsFollow(id, userId);
        return new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
    }
}
