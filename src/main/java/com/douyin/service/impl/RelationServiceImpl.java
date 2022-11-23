package com.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.RelationMapper;
import com.douyin.model.UserModel;
import com.douyin.pojo.Relation;
import com.douyin.pojo.User;
import com.douyin.service.RelationService;
import com.douyin.service.UserService;
import com.douyin.util.Entity2Model;
import com.douyin.util.RedisUtil;
import com.douyin.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.douyin.util.RedisIdentification.USER_QUERY_KEY;
import static com.douyin.util.RedisIdentification.USER_QUERY_TTL;

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
    @Autowired
    private Entity2Model entity2Model;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean getIsFollow(Long favouriteId, Long authorId) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", authorId);
        queryWrapper.eq("favourite_id", favouriteId);
        return baseMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public boolean doFollow(Long authorId, Long userId) {
        //先获取两个用户的关注数和被关注数
        //视频作者
        Integer authorFollowerCount = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, authorId, User.class, userService::getById, USER_QUERY_TTL, TimeUnit.MINUTES).getFollowerCount();
        //关注者
        Integer followCount = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, userId, User.class, userService::getById, USER_QUERY_TTL, TimeUnit.MINUTES).getFollowerCount();

        //说明用户还没有关注，需要来创建新的关注列，
        Relation relation = new Relation(SnowFlake.nextId(), authorId, userId);
        relationService.save(relation);
        //并同时修改两个用户的关注数，一个是被关注数，一个是关注数。
        return userService.updateUserFollowCount(authorId, authorFollowerCount, userId, followCount);
    }

    @Override
    public boolean cancelFollow(Long authorId, Long userId) {
        //先获取两个用户的关注数和被关注数
        //视频作者
        Integer authorFollowerCount = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, authorId, User.class, userService::getById, USER_QUERY_TTL, TimeUnit.MINUTES).getFollowerCount();

        //关注者
        Integer followCount = redisUtil.queryWithoutPassThrough(USER_QUERY_KEY, userId, User.class, userService::getById, USER_QUERY_TTL, TimeUnit.MINUTES).getFollowerCount();

        //说明用户已经关注过，因此删除用户关注列，同时将两者的关注数和被关注数各-1
        QueryWrapper<Relation> wrapper = new QueryWrapper<>();
        wrapper.eq("author_id", authorId).eq("favourite_id", userId);
        boolean remove = relationService.remove(wrapper);
        //同时修改两个用户的关注数，一个是被关注数，一个是关注数。
        return userService.updateUserUnfollowCount(authorId, authorFollowerCount, userId, followCount) && remove;
    }

    @Override
    public List<UserModel> getFollowList(Long userId) {
        QueryWrapper<Relation> qw = new QueryWrapper<>();
        qw.eq("favourite_id", userId);
        List<UserModel> userModelList = new ArrayList<>();
        List<Relation> relations = baseMapper.selectList(qw);
        for (Relation relation : relations) {
            Long authorId = relation.getAuthorId();
            User author = userService.getById(authorId);
            UserModel userModel = new UserModel(author.getUserId(), author.getName(), author.getFollowCount(), author.getFollowerCount(), true);
            userModelList.add(userModel);
        }
        return userModelList;
    }

    /**
     * 查询用户的粉丝列表
     *
     * @param userId 目前查看状态的用户的id
     * @return 返回用户的粉丝列表
     */
    @Override
    public List<UserModel> getFollowerList(Long userId) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", userId);
        List<UserModel> userModelFanList = new ArrayList<>();
        List<Relation> relations = baseMapper.selectList(queryWrapper);
        for (Relation relation : relations) {
            Long favouriteId = relation.getFavouriteId();
            User favourite = userService.getById(favouriteId);
            UserModel userModel = new UserModel(favourite.getUserId(), favourite.getName(), favourite.getFollowCount(), favourite.getFollowerCount(), true);
            userModelFanList.add(userModel);
        }
        return userModelFanList;
    }
}
