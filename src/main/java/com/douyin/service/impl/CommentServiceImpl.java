package com.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.douyin.mapper.CommentMapper;
import com.douyin.mapper.VideoMapper;
import com.douyin.model.CommentModel;
import com.douyin.model.UserModel;
import com.douyin.pojo.Comment;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.CommentService;
import com.douyin.service.UserService;
import com.douyin.util.Entity2Model;
import com.douyin.util.JwtHelper;
import com.douyin.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author foanxi
 */
@Service("CommentServiceImpl")
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private Entity2Model entity2Model;

    private final Integer SUCCESS = 1;

    @Override
    public CommentModel addComment(String token, String videoId, String commentText) {
        Long userId = JwtHelper.getUserId(token);
        Long id = SnowFlake.nextId();


        Comment comment = new Comment(id, userId, Long.parseLong(videoId), commentText);
        int insert = commentMapper.insert(comment);
        if (insert == SUCCESS) {
            QueryWrapper<Video> qw = new QueryWrapper<>();
            qw.eq("video_id", videoId);
            Video video = videoMapper.selectById(videoId);
            video.setCommentCount(video.getCommentCount() + 1);
            videoMapper.update(video, qw);
            User user = userService.getById(userId);
            UserModel userModel = entity2Model.user2userModel(user, Long.valueOf(videoId));
            Comment newComment = commentMapper.selectById(id);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            String time = simpleDateFormat.format(new Date(newComment.getCreateTime().getTime()));
            return new CommentModel(id, userModel, commentText, time);
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteComment(String videoId, String commentId) {
        QueryWrapper<Video> qw = new QueryWrapper<>();
        qw.eq("video_id", videoId);
        Video video = videoMapper.selectById(videoId);
        video.setCommentCount(video.getCommentCount() - 1);
        videoMapper.update(video, qw);
        return commentMapper.deleteById(commentId) == SUCCESS;
    }

    @Override
    public List<CommentModel> getCommentList(String videoId) {
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("video_id", videoId);
        List<Comment> comments = commentMapper.selectList(qw);
        List<CommentModel> commentModelList = new ArrayList<>();
        for (Comment c : comments) {
            User user = userService.getById(c.getUserId());
            UserModel userModel = entity2Model.user2userModel(user, Long.valueOf(videoId));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
            String time = simpleDateFormat.format(new Date(c.getCreateTime().getTime()));
            CommentModel commentModel = new CommentModel(c.getCommentId(), userModel, c.getCommentText(), time);
            commentModelList.add(commentModel);
        }
        return commentModelList;
    }
}
