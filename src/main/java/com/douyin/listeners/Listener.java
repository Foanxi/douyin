package com.douyin.listeners;

import com.alibaba.fastjson.JSONObject;
import com.douyin.model.PublishMessageModel;
import com.douyin.service.VideoService;
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
    private VideoService videoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("douyin.video"),
            exchange = @Exchange(value = "douyin.exchange", type = ExchangeTypes.TOPIC),
            key = "video.#"
    ))
    public void listenVideoQueue(JSONObject jsonObject) {
        PublishMessageModel publishMessageModel = JSONObject.parseObject(jsonObject.toJSONString(), PublishMessageModel.class);
        videoService.publishVideo(publishMessageModel.getData(), publishMessageModel.getTitle(), publishMessageModel.getToken());
    }

}
