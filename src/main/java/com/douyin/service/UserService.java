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
     *
     * @param userId              用户id
     * @param authorFollowerCount 用户的粉丝数量
     * @param authorId            作者id
     * @param followCount         作者的关注数量
     * @return 返回是否更新成功
     */
    boolean updateUserFollowCount(Long authorId, Integer authorFollowerCount, Long userId, Integer followCount);

    /**
     * 更新关注者与被关注者的关注数或被关注数，并删除相关的关注关系
     *
     * @param authorId            作者id
     * @param authorFollowerCount 作者粉丝id
     * @param userId              用户id
     * @param followCount         作者关注id
     * @return 返回是否更新成功
     */
    boolean updateUserUnfollowCount(Long authorId, Integer authorFollowerCount, Long userId, Integer followCount);

    /**
     * 实现用户登录校验
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回登陆状态码
     */
    Long login(String username, String password);
}
