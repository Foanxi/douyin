package com.douyin.pojo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhuanghaoxin
 * @Description: 用户实体类
 * @create: 2022-10-23 14:59
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User {
    @ApiModelProperty(value = "用户ID")
    private String id;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "用户密码")
    private String password;
    @ApiModelProperty(value = "关注用户数量")
    private Integer followCount;
    @ApiModelProperty(value = "粉丝数量")
    private Integer followerCount;
}
