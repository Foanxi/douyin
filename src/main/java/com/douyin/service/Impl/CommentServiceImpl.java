package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.CommentMapper;
import com.douyin.pojo.Comment;
import com.douyin.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("CommentServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
