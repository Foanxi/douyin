package com.douyin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyin.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author foanxi
 */
@Mapper
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
}
