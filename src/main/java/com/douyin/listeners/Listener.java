package com.douyin.listeners;

import com.alibaba.fastjson.JSONObject;
import com.douyin.model.CommentModel;
import com.douyin.model.FavouriteModel;
import com.douyin.service.CommentService;
import com.douyin.service.FavouriteService;
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
    @Autowired
    private FavouriteService favouriteService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("douyin.delete"),
            exchange = @Exchange(value = "douyin.exchange", type = ExchangeTypes.TOPIC),
            key = "comment.delete"
    ))
    public void listenDeleteQueue(JSONObject jsonObject) {
        CommentModel commentModel = JSONObject.parseObject(jsonObject.toJSONString(), CommentModel.class);
        commentService.deleteComment(commentModel.getVideoId(), commentModel.getCommentId());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("douyin.favouriteDo"),
            exchange = @Exchange(value = "douyin.favouriteDoExchange", type = ExchangeTypes.TOPIC),
            key = "favourite.do"
    ))
    public void listenDoFavourite(JSONObject jsonObject) {
        FavouriteModel favouriteModel = JSONObject.parseObject(jsonObject.toJSONString(), FavouriteModel.class);
        favouriteService.doFavourite(favouriteModel.getVideoId(), favouriteModel.getUserId());
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("douyin.favouriteCancel"),
            exchange = @Exchange(value = "douyin.favouriteCancelExchange", type = ExchangeTypes.TOPIC),
            key = "favourite.cancel"
    ))
    public void listenCancelFavourite(JSONObject jsonObject) {
        FavouriteModel favouriteModel = JSONObject.parseObject(jsonObject.toJSONString(), FavouriteModel.class);
        favouriteService.cancelFavourite(favouriteModel.getVideoId(), favouriteModel.getUserId());
    }
}
