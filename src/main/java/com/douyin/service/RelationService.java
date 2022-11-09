package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Relation;

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
     * param userId
     * param authorId
     * return
     */
    boolean updateVideoAndRelation(Long userId, Long authorId);

    /**
     * 用户取消关注
     * param userId
     * param authorId
     * return
     */
    boolean cancelRelation(Long userId, Long authorId);
}
