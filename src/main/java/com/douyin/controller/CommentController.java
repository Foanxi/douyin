package com.douyin.controller;

import com.alibaba.fastjson.JSONObject;
import com.douyin.model.CommentModel;
import com.douyin.service.CommentService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author foanxi
 */
@RestController
@RequestMapping("/douyin/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/action/")
    public JSONObject commentAction(@RequestParam("token") String token,
                                    @RequestParam("video_id") String videoId,
                                    @RequestParam("action_type") String actionType,
                                    @RequestParam(value = "comment_text", required = false) String commentText,
                                    @RequestParam(value = "comment_id", required = false) String commentId) {
        JSONObject json;
        log.info("传递过来的comment_id为：{}", commentId);
        if (JwtHelper.isExpiration(token)) {
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }

        if ("1".equals(actionType)) {
            CommentModel res = commentService.addComment(token, videoId, commentText);
            if (res != null) {
                json = CreateJson.createJson(200, 0, "评论成功");
                json.put("comment", res);
            } else {
                json = CreateJson.createJson(200, 1, "评论失败");
            }
        } else {
            if (commentService.deleteComment(videoId, commentId)) {
                json = CreateJson.createJson(200, 0, "删除评论成功");
            } else {
                json = CreateJson.createJson(200, 1, "删除评论失败");
            }
        }
        return json;
    }

    @GetMapping("/list/")
    public JSONObject getCommentList(@RequestParam("token") String token,
                                     @RequestParam("video_id") String videoId) {
        JSONObject json;
        if (JwtHelper.isExpiration(token)) {
            json = CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        } else {
            List<CommentModel> commentList = commentService.getCommentList(videoId);
            json = CreateJson.createJson(200, 0, "");
            json.put("comment_list", commentList);
            log.info("CommentController的评论列表为" + json);
        }
        return json;
    }
}
