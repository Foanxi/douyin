package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.CommentMapper;
import com.douyin.pojo.Comment;
import com.douyin.service.impl.commentServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author: zhuanghaoxin
 * @Description:
 * @create: 2022-10-23 15:01
 **/
@Service
public class commentService extends ServiceImpl<CommentMapper, Comment> implements commentServiceImpl{
}
