package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private Long id;
    private String authorId;
    private String playUrl;
    private String coverUrl;
    private Integer favouriteCount;
    private Integer commentCount;
    private Long createTime;
    private String title;
}
