package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author foanxi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @TableId(type = IdType.INPUT)
    private Long commentId;

    private Long userId;

    private Long videoId;

    private String commentText;

    private Timestamp createTime;

    private Timestamp deleteTime;

    @TableLogic(delval = "1", value = "0")
    private Integer isDeleted;
}
