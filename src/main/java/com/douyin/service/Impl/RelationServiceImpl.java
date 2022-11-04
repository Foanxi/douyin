package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.RelationMapper;
import com.douyin.pojo.Relation;
import com.douyin.service.RelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hongxiaobin
 */
@Service("RelationServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService {
    @Override
    public boolean getIsFollow(Long authorId, Long favouriteId) {
        QueryWrapper<Relation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id", authorId);
        queryWrapper.eq("favourite_id", favouriteId);
        return baseMapper.selectOne(queryWrapper) != null;
    }
}
