package com.douyin.listeners;

import com.alibaba.fastjson.JSONObject;
import com.douyin.model.CommentModel;
import com.douyin.service.CommentService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author foanxi
 */
@Component
public class Listener {

    @Autowired
    private CommentService commentService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("douyin.delete"),
            exchange = @Exchange(value = "douyin.exchange", type = ExchangeTypes.TOPIC),
            key = "comment.delete"
    ))
    public void listenDeleteQueue(JSONObject jsonObject) {
        CommentModel commentModel = JSONObject.parseObject(jsonObject.toJSONString(), CommentModel.class);
        commentService.deleteComment(commentModel.getVideoId(), commentModel.getCommentId());
    }

}
