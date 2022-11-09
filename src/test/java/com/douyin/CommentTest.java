package com.douyin;

import com.douyin.mapper.CommentMapper;
import com.douyin.pojo.Comment;
import com.douyin.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    @Test
    void testDeleteComment() {
        System.out.println(commentService.deleteComment("videoId", "787118932279427072"));
    }


    @Test
    void testInsert() {
        Comment comment = new Comment(1L, 787305215958188032L, 787305313140211712L, "你好啊");
        commentMapper.insert(comment);
    }


}
