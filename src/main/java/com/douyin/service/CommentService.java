package com.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.douyin.model.CommentModel;
import com.douyin.pojo.Comment;

import java.util.List;

/**
 * @author foanxi
 */
public interface CommentService extends IService<Comment> {

    /**
     * 向数据库中插入评论
     *
     * @param token       评论用户的token
     * @param videoId     用户评论的视频的id
     * @param commentText 用户评论内容
     * @return 返回是否插入成功
     */
    CommentModel addComment(String token, String videoId, String commentText);

    /**
     * 删除评论，但是执行的是软删除
     *
     * @param commentId 需要删除的评论的id
     * @return 返回是否软删除成功
     */
    boolean deleteComment(String commentId);

    /**
     * 获得该视频的完整的评论列表
     *
     * @param videoId 需要获取的视频的id
     * @return 返回该视频的评论列表
     */
    List<CommentModel> getCommentList(String videoId);
}
