package com.douyin.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhuanghaoxin
 * @Description:关注实体类
 * @create: 2022-10-23 14:59
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Fans {
    @ApiModelProperty(value = "作者ID")
    private String authorId;
    @ApiModelProperty(value = "粉丝ID")
    private String favouriteId;
}
