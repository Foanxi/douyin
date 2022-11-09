package com.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.RelationMapper;
import com.douyin.pojo.Relation;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hongxiaobin
 */
@Service("RelationServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService {
    @Autowired
    private RelationService relationService;
    @Autowired
    private UserService userService;
    @Override
    public boolean getIsFollow(Long authorId, Long favouriteId) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", authorId);
        queryWrapper.eq("favourite_id", favouriteId);
        return baseMapper.selectOne(queryWrapper) != null;
    }
    @Override
    public boolean updateVideoAndRelation(Long authorId, Long userId) {
        //先获取两个用户的关注数和被关注数
        //视频作者
        Integer authorFollowerCount = userService.getById(authorId).getFollowerCount();
        //关注者
        Integer followCount = userService.getById(userId).getFollowCount();
        //说明用户还没有关注，需要来创建新的关注列，
        Relation relation = new Relation(SnowFlake.nextId(),authorId,userId);
        relationService.save(relation);
        //并同时修改两个用户的    关注数，一个是被关注数，一个是关注数。
        return userService.updateUserFollowCount(authorId, authorFollowerCount, userId, followCount);
    }
    @Override
    public boolean cancelRelation(Long authorId, Long userId) {
        //先获取两个用户的关注数和被关注数
        //视频作者
        Integer authorFollowerCount = userService.getById(authorId).getFollowerCount();
        //关注者
        Integer followCount = userService.getById(userId).getFollowCount();
        //说明用户已经关注过，因此删除用户关注列，同时将两者的关注数和被关注数各-1
        QueryWrapper<Relation> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id", authorId).eq("favourite_id", userId);
        boolean remove = relationService.remove(wrapper);
        //同时修改两个用户的关注数，一个是被关注数，一个是关注数。
        return userService.updateUserFollowerCount(authorId, authorFollowerCount, userId, followCount);
    }
}
