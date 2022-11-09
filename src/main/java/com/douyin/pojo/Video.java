package com.douyin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Video {
    @TableId(value = "video_id", type = IdType.INPUT)
    private Long videoId;
    private Long authorId;
    private String playUrl;
    private String coverUrl;
    private Integer favouriteCount;
    private Integer commentCount;
    private Timestamp createTime;
    private Timestamp deleteTime;
    private String title;

    public Video(Long videoId, Long authorId, String playUrl, String coverUrl, String title) {
        this.videoId = videoId;
        this.authorId = authorId;
        this.playUrl = playUrl;
        this.coverUrl = coverUrl;
        this.title = title;
    }
}
