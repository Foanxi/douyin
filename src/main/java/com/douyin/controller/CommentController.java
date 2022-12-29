package com.douyin.controller;

import com.alibaba.fastjson.JSONObject;
import com.douyin.model.CommentListModel;
import com.douyin.model.CommentModel;
import com.douyin.service.CommentService;
import com.douyin.util.CreateJson;
import com.douyin.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/action/")
    public JSONObject commentAction(@RequestParam("token") String token,
                                    @RequestParam("video_id") String videoId,
                                    @RequestParam("action_type") String actionType,
                                    @RequestParam(value = "comment_text", required = false) String commentText,
                                    @RequestParam(value = "comment_id", required = false) String commentId) {

        JSONObject json;
        if (JwtHelper.isExpiration(token)) {
            log.warn("commentAction token: {} isExpiration", token);
            return CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        }

        final String success = "1";
        if (success.equals(actionType)) {
            // actionType属性不同的时候,所传入的参数不同,因此日志写于此处
            log.info("commentAction enter param token: {},video_id: {},action_type: {},comment_text: {}", token, videoId, actionType, commentText);
            CommentListModel res = commentService.addComment(token, videoId, commentText);
            if (res != null) {
                json = CreateJson.createJson(200, 0, "评论成功");
                json.put("comment", res);
                log.info("commentAction return json: {}", JSONObject.toJSONString(json, true));
            } else {
                log.warn("commentAction operation failed");
                json = CreateJson.createJson(200, 1, "评论失败");
            }
        } else {
            log.info("commentAction enter param video_id: {},comment_id: {}", videoId, commentId);
            CommentModel commentModel = new CommentModel(videoId, commentId);
            try {
                String exchange = "douyin.exchange";
                String key = "comment.delete";
                rabbitTemplate.convertAndSend(exchange, key, commentModel);
            } catch (Exception e) {
                log.warn("commentAction delete operation failed");
                json = CreateJson.createJson(200, 1, "删除评论失败");
                return json;
            }
            log.info("commentAction delete operation success");
            json = CreateJson.createJson(200, 0, "删除评论成功");
        }
        return json;
    }

    @GetMapping("/list/")
    public JSONObject getCommentList(@RequestParam("token") String token,
                                     @RequestParam("video_id") String videoId) {
        JSONObject json;
        if (JwtHelper.isExpiration(token)) {
            log.warn("getCommentList token: {} isExpiration", token);
            json = CreateJson.createJson(200, 1, "用户token过期，请重新登陆");
        } else {
            List<CommentListModel> commentList = commentService.getCommentList(videoId, token);
            json = CreateJson.createJson(200, 0, "");
            json.put("comment_list", commentList);
            log.info("getCommentList return json: {}", JSONObject.toJSONString(json, true));
        }
        return json;
    }
}
