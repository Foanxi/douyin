package com.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.UserMapper;
import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.douyin.util.RedisIdentification.USER_QUERY_KEY;
import static com.douyin.util.RedisIdentification.USER_QUERY_TTL;

/**
 * @author hongxiaobin
 */
@Service("UserServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        //查询该账号下的用户是否为空
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public UserModel getUserById(Long id, Long authorId) {
        User user = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, authorId, User.class, this::getById, RedisIdentification.USER_QUERY_TTL, TimeUnit.MINUTES);
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
        if (baseMapper.insert(user) != success) {
            map.put("statusCode", fail);
        } else {
            redisUtil.putInBloom(USER_QUERY_KEY + id);
            map.put("statusCode", success);
            String token = JwtHelper.createToken(id);
            map.put("userId", id);
            map.put("token", token);
        }
        return map;
    }

    @Override
    public boolean updateUserFollowCount(Long authorId, Integer authorFollowerCount, Long userId, Integer followCount) {
        // 首先更新作者信息,将粉丝数+1
        User author = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, authorId, User.class, this::getById, USER_QUERY_TTL, TimeUnit.MINUTES);
        UpdateWrapper<User> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("user_id", authorId).set("follower_count", authorFollowerCount + 1);
        boolean update = userService.update(author, updateWrapper1);

        // 更新原本粉丝信息,将关注数-1
        User user = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, userId, User.class, this::getById, USER_QUERY_TTL, TimeUnit.MINUTES);
        UpdateWrapper<User> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("user_id", userId).set("follow_count", followCount + 1);
        boolean update2 = userService.update(user, updateWrapper2);
        return update && update2;
    }

    @Override
    public boolean updateUserUnfollowCount(Long authorId, Integer authorFollowerCount, Long userId, Integer followCount) {
        // 首先更新作者信息,将粉丝数-1
        User author = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, authorId, User.class, this::getById, USER_QUERY_TTL, TimeUnit.MINUTES);
        UpdateWrapper<User> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.eq("user_id", authorId).set("follower_count", authorFollowerCount - 1);
        boolean update = userService.update(author, updateWrapper1);

        // 更新原本粉丝信息,将关注数-1
        User user = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, userId, User.class, this::getById, USER_QUERY_TTL, TimeUnit.MINUTES);
        UpdateWrapper<User> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("user_id", userId).set("follow_count", followCount - 1);
        boolean update2 = userService.update(user, updateWrapper2);

        return update && update2;
    }

    @Override
    public Long login(String username, String password) {
        String md5Password = Md5.encrypt(password);
        User u = userService.getUserByUsername(username);
        final Long unExist = -1L;
        final Long inconsistent = 0L;
        if (u == null) {
            return unExist;
        } else if (!u.getPassword().equals(md5Password)) {
            return inconsistent;
        } else {
            redisUtil.putInBloom(USER_QUERY_KEY + u.getUserId());
            return u.getUserId();
        }
    }
}
