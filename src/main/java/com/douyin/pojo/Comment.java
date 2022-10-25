package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment {
    @TableId(value = "comment_id", type = IdType.ID_WORKER_STR)
    private String commentId;
    private String userId;
    private String videoId;
    private String commentText;
    private String commentData;
}
