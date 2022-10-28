package com.douyin.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Video {

    private String id;
    private String authorId;
    private String playUrl;
    private String coverUrl;
    private Integer favouriteCount;
    private Integer commentCount;
    private Date createTime;
}
