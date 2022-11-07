package com.douyin;

import com.douyin.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentService commentService;

    @Test
    void testDeleteComment() {
        System.out.println(commentService.deleteComment("videoId", "787118932279427072"));
    }

    @Test
    void testGetCommentList() {
        System.out.println(commentService.getCommentList("787073805309706240"));
    }

}
