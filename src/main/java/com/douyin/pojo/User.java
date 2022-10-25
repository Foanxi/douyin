package com.douyin.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User {
    private String id;
    private String name;
    private String password;
    private Integer followCount;
    private Integer followerCount;
}
