package com.douyin.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhuanghaoxin
 * @Description:点赞实体类
 * @create: 2022-10-23 14:59
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Favourite {
    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "视频ID")
    private String videoId;
}
