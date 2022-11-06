package com.douyin;

import com.douyin.model.UserModel;
import com.douyin.pojo.User;
import com.douyin.pojo.Video;
import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.util.Entity2Model;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Entity2ModelTest {

    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;
    @Autowired
    private Entity2Model entity2Model;

    @Test
    public void testUser2UserModel() {
        Video video = videoService.getById(0L);
        User user = userService.getById(video.getAuthorId());
        System.out.println(user);
        UserModel userModel = entity2Model.user2userModel(user, video.getVideoId());
        System.out.println(userModel);
    }
}
