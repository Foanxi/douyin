package com.douyin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("follow_count")
    private Integer followCount;
    @JsonProperty("follower_count")
    private Integer followerCount;
    @JsonProperty("is_follow")
    private boolean isFollow;
}
