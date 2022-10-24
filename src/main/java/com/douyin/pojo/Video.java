package com.douyin.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhuanghaoxin
 * @Description: 视频实体类
 * @create: 2022-10-23 14:59
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Video {
    @ApiModelProperty(value = "视频ID")
    private String id;
    @ApiModelProperty(value = "作者ID")
    private String authorId;
    @ApiModelProperty(value = "视频地址")
    private String playUrl;
    @ApiModelProperty(value = "封面地址")
    private String coverUrl;
    @ApiModelProperty(value = "点赞数量")
    private Integer favouriteCount;
    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;
    @ApiModelProperty(value = "上传时间")
    private Data createTime;
}
