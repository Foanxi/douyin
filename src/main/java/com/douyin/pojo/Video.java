package com.douyin.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JSONField(name = "play_url")
    private String playUrl;
    @JSONField(name ="cover_url")
    private String coverUrl;
    @JSONField(name ="favourite_count")
    private Integer favouriteCount;
    @JSONField(name ="comment_count")
    private Integer commentCount;
    private Long createTime;
    private String title;
}
