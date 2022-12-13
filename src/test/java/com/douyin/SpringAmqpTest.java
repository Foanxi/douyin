package com.douyin;

import com.douyin.mapper.UserMapper;
import com.douyin.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserMapper userMapper;

    @Test
    void testSendMessage() {
        String exchange = "douyin.exchange";
        String key = "video";
        User user = userMapper.selectById(792731296978698240L);
        rabbitTemplate.convertAndSend(exchange, key, user);
    }

    @Test
    void testSendVideo() {
    }
}
