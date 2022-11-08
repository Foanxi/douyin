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
}
