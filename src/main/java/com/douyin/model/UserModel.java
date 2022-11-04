package com.douyin.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author foanxi
 */
@Data
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String name;
    @JSONField(name = "follow_count")
    private Integer followCount;
    @JSONField(name = "follower_count")
    private Integer followerCount;
    @JSONField(name = "is_follow")
    private Boolean isFollow;
}
