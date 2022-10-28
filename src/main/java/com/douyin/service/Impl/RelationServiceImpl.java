package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.RelationMapper;
import com.douyin.mapper.VideoMapper;
import com.douyin.pojo.Relation;
import com.douyin.pojo.Video;
import com.douyin.service.RelationService;
import com.douyin.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("RelationServiceImpl")
@Transactional
public class RelationServiceImpl extends ServiceImpl<RelationMapper, Relation> implements RelationService {
}
