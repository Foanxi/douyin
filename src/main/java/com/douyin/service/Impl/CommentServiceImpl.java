package com.douyin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.CommentMapper;
import com.douyin.mapper.FavouriteMapper;
import com.douyin.pojo.Comment;
import com.douyin.pojo.Favourite;
import com.douyin.service.CommentService;
import com.douyin.service.FavouriteService;

public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
