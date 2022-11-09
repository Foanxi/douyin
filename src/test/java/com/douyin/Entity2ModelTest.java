package com.douyin;

import com.douyin.service.UserService;
import com.douyin.service.VideoService;
import com.douyin.util.Entity2Model;
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
}
