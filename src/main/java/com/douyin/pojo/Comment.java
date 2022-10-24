package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: zhuanghaoxin
 * @Description:评论实体类
 * @create: 2022-10-23 14:59
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment {
    @ApiModelProperty(value = "评论ID")
    @TableId(value = "comment_id", type = IdType.ID_WORKER_STR)
    private String commentId;
    @ApiModelProperty(value = "用户ID")
    private String userId;
    @ApiModelProperty(value = "视频ID")
    private String videoId;
    @ApiModelProperty(value = "评论内容")
    private String commentText;
    @ApiModelProperty(value = "评论日期")
    private String commentData;
}
