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
    void testGetCommentList() {
        System.out.println(commentService.getCommentList("787073805309706240"));
    }

    //token=eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJSiox099ANDXYNUtJRSq0oULIyNDMztzQxMzM101EqLU4t8kxRsjK3MDc2MDUyNLU0tTC0sDAwNqoFAHh5iBJAAAAA.Pvc030O8WFPAOS8Dl2Oz-36pztB4vTFgbbGEolh4UlgaHOWGn1e_vN6Jpc8N3cZjgwKTj3rL0KE7dheyqiQ2yw
    // video_id=787305313140211712
    // action_type=1
    //comment_text=%E4%BD%A0%E5%A5%BD%E5%95%8A
    @Test
    void testInsert() {
        Comment comment = new Comment(1L, 787305215958188032L, 787305313140211712L, "你好啊");
        commentMapper.insert(comment);
    }


}
