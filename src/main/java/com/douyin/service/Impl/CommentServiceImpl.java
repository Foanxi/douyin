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
import com.douyin.util.DateUtil;
import com.douyin.util.Entity2Model;
import com.douyin.util.JwtHelper;
import com.douyin.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author foanxi
 */
@Service("CommentServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public CommentModel addComment(String token, String videoId, String commentText) {
        Long userId = JwtHelper.getUserId(token);
        Long nowTime = System.currentTimeMillis();
        Long id = SnowFlake.nextId();

        Comment comment = new Comment(id, userId, Long.parseLong(videoId), commentText, String.valueOf(nowTime), 1);

        if (baseMapper.insert(comment) == 1) {
            User user = userService.getById(userId);
            UserModel userModel = Entity2Model.user2userModel(user, videoId);
            return new CommentModel(id, userModel, commentText, DateUtil.timestampToDate(nowTime));
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
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("video_id", videoId);
        List<Comment> comments = baseMapper.selectList(qw);
        List<CommentModel> commentModelList = new ArrayList<>();
        for (Comment c : comments) {
            User user = userService.getById(c.getUserId());
            UserModel userModel = Entity2Model.user2userModel(user, videoId);
            CommentModel commentModel = new CommentModel(c.getCommentId(), userModel, c.getCommentText(), c.getCommentDate());
            commentModelList.add(commentModel);
        }
        return commentModelList;
    }
}
