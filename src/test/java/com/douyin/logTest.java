package com.douyin;

import com.alibaba.fastjson.JSONObject;
import com.douyin.service.UserService;
import com.douyin.util.CreateJson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class logTest {

    @Autowired
    private UserService userService;

    @Test
    void testUserLog() {
        JSONObject jsonObject;
        jsonObject = CreateJson.createJson(200, 0, "test");
        log.info("jsonObject is {}", JSONObject.toJSONString(jsonObject, true));

    }
}
