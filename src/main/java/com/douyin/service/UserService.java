package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.UserModel;
import com.douyin.pojo.User;

import java.util.Map;

/**
 * @author hongxiaobin
 */
public interface UserService extends IService<User> {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据用户id查询用户信息
     *
     * @param userId   用户id
     * @param authorId 关注者的id
     * @return 返回用户模型
     */
    UserModel getUserById(Long userId, Long authorId);

    /**
     * 实现用户注册
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回注册状态
     */
    Map<String, Object> register(String username, String password);
    /**
     * 更改关注者与被关注者的关注数或被关注数，并新增关注关系
     * param userId
     * param authorFollowerCount
     * param authorId
     * param followCount
     * return
     */
    boolean updateUserFollowCount(Long authorId,Integer authorFollowerCount,Long userId,Integer followCount);

    /**
     * 更改关注者与被关注者的关注数或被关注数，并删除相关的关注关系
     * param authorId
     * param authorFollowerCount
     * param userId
     * param followCount
     * return
     */
    boolean updateUserFollowerCount(Long authorId,Integer authorFollowerCount,Long userId,Integer followCount);
}
