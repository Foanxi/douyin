package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.pojo.Relation;

/**
 * @author foanxi
 */
public interface RelationService extends IService<Relation> {
    boolean getIsFollow(String userId, String authorId);
}
