package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.RelationMapper;
import com.douyin.mapper.VideoMapper;
import com.douyin.pojo.Favourite;
import com.douyin.pojo.Relation;
import com.douyin.pojo.Video;
import com.douyin.service.RelationService;
import com.douyin.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("RelationServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService {
    @Override
    public boolean getIsFollow(String authorId, String favouriteId) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", authorId);
        queryWrapper.eq("favourote_id", favouriteId);
        return baseMapper.selectOne(queryWrapper) != null;
    }
}
