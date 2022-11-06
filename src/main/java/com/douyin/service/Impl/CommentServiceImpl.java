package com.douyin.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.CommentMapper;
import com.douyin.model.CommentModel;
import com.douyin.model.UserModel;
import com.douyin.pojo.Comment;
import com.douyin.pojo.User;
import com.douyin.service.CommentService;
import com.douyin.service.UserService;
import com.douyin.util.Entity2Model;
import com.douyin.util.JwtHelper;
import com.douyin.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author foanxi
 */
@Service("CommentServiceImpl")
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;
    @Autowired
    private Entity2Model entity2Model;

    @Override
    public CommentModel addComment(String token, String videoId, String commentText) {
        Long userId = JwtHelper.getUserId(token);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Long id = SnowFlake.nextId();

        Comment comment = new Comment(id, userId, Long.parseLong(videoId), commentText, null, null, 1);
        if (baseMapper.insert(comment) == 1) {
            User user = userService.getById(userId);
            UserModel userModel = entity2Model.user2userModel(user, Long.valueOf(videoId));
            return new CommentModel(id, userModel, commentText, now);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteComment(String commentId) {
        return 1 == baseMapper.deleteById(Long.parseLong(commentId));
    }

    @Override
    public List<CommentModel> getCommentList(String videoId) {
        log.info("getCommentList的vidoeId:{}",videoId);
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        List<Comment> comments = baseMapper.selectList(qw);
        List<CommentModel> commentModelList = new ArrayList<>();
        for (Comment c : comments) {
            User user = userService.getById(c.getUserId());
            UserModel userModel = entity2Model.user2userModel(user, Long.valueOf(videoId));
            CommentModel commentModel = new CommentModel(c.getCommentId(), userModel, c.getCommentText(), c.getCreateTime());
            commentModelList.add(commentModel);
        }
        return commentModelList;
    }
}
