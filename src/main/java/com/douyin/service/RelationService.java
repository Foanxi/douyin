package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.UserModel;
import com.douyin.pojo.Relation;

import java.util.List;

/**
 * @author foanxi
 */
public interface RelationService extends IService<Relation> {
    /**
     * 查询是否存在关注关系
     *
     * @param userId   关注者id
     * @param authorId 被关注者id
     * @return 返回是否存在被关注
     */
    boolean getIsFollow(Long userId, Long authorId);

    /**
     * 用户点击关注
     *
     * @param userId   当前用户id
     * @param authorId 当前作者id
     * @return 返回用户是否关注成功
     */
    boolean doFollow(Long userId, Long authorId);

    /**
     * 用户取消关注
     *
     * @param userId   当前用户id
     * @param authorId 当前作者id
     * @return 返回用户是否取消点赞成功
     */
    boolean cancelFollow(Long userId, Long authorId);

    /**
     * 查询用户的关注列表
     *
     * @param userId 目前查看状态的用户的id
     * @return 返回用户的关注列表
     */
    List<UserModel> getFollowList(Long userId);


    /**
     * 查询用户的粉丝列表
     *
     * @param userId 目前查看状态的用户的id
     * @return 返回用户的粉丝列表
     */
    List<UserModel> getFollowerList(Long userId);
}
