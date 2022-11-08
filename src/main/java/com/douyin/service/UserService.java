package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.UserModel;
import com.douyin.pojo.User;

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
}
