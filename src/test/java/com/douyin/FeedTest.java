package com.douyin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.douyin.pojo.Video;
import com.douyin.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.List;

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
}
