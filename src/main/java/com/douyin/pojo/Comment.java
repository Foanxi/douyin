package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * @author foanxi
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
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
    @TableLogic(value = "1", delval = "0")
    private Integer logicDelete;
}
