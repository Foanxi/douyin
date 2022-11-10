package com.douyin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.douyin.pojo.Video;
import com.douyin.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FeedTest {

    @Autowired
    private VideoService videoService;

    @Test
    public void getDataTime() {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 20");
        List<Video> list = videoService.list(queryWrapper);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String strNextTime = simpleDateFormat.format(list.get(list.size() - 1).getCreateTime());
        Integer nextTime = Integer.parseInt(strNextTime);
        System.out.println(nextTime);
    }

    @Test
    public void getAuthor() {
        System.out.println(videoService.getById(0L).getAuthorId());
    }

    @Test
    public void testCompareTime() {
        long l = System.currentTimeMillis();
        String time = String.valueOf(l);
        Map<String, Object> map = videoService.feedVideo("1668020689000", "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSiox099ANDXYNUtJRSq0oULIyNDMztzQzNzEy01EqLU4t8kxRsjK3MDe2NDI2NjQzNjWxNLAwNKsFAGLsm_1AAAAA.bji4nlz12YA7C9ypSs18PN8m4rgokWfq90TCeonM3yDyV0UOcmJs3pcV8CtZUjeAyhUnrWFBqx0tQEpe1FOhJg");
        System.out.println(JSONObject.toJSONString(map, true));
    }
}
