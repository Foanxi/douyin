package com.douyin;

import com.douyin.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Map;


@SpringBootTest
public class TimeTest {

    @Autowired
    private VideoService videoService;

    @Test
    public void Timestamp2Date() {
        Map<String, Object> map = videoService.feedVideo("1667818133790");
        Timestamp nextTime = (Timestamp) map.get("nextTime");
        System.out.println(nextTime.getTime());
        System.out.println(nextTime);
    }
}
