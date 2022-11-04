package com.douyin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.douyin.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author foanxi, zhuanghaoxin
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
