package com.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.util.JwtHelper;
import com.douyin.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

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
    public UserModel getUserById(Long id, Long authorId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", authorId);
        User user = baseMapper.selectOne(queryWrapper);
        boolean isFollow = relationService.getIsFollow(id, authorId);
        return new UserModel(user.getUserId(), user.getName(), user.getFollowCount(), user.getFollowerCount(), isFollow);
    }

    @Override
    public Map<String, Object> register(String username, String password) {
        final int success = 1;
        final Integer exist = -1;
        final Integer fail = 0;
        Map<String, Object> map = new HashMap<>();
        if (this.getUserByUsername(username) != null) {
            map.put("statusCode", exist);
            return map;
        }
        // 先对用户的密码进行加密，后将账号密码发送到数据库存储
        String md5password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 由于用户初始注册，并没有关注数和被关注数，因此都设置为0
        long id = SnowFlake.nextId();
        User user = new User(id, username, md5password);
        int insert = baseMapper.insert(user);
        if (insert != success) {
            map.put("statusCode", fail);
        } else {
            map.put("statusCode", success);
            String token = JwtHelper.createToken(id);
            map.put("userId", id);
            map.put("token", token);
        }
        return map;
    }
}
