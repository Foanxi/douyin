package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author foanxi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @TableId(type = IdType.INPUT)
    private Long userId;
    private String name;
    private String password;
    private Integer followCount;
    private Integer followerCount;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp deleteTime;

    public User(Long userId, String name, String password, Integer followCount, Integer followerCount) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.followCount = followCount;
        this.followerCount = followerCount;
    }
}
