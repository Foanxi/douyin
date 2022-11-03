package com.douyin.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoModel {
    private long id;
    private UserModel author;
    @JSONField(name = "play_url")
    private String play_url;
    @JSONField(name = "cover_url")
    private String cover_url;
    @JSONField(name = "favorite_count")
    private long favorite_count;
    @JSONField(name = "comment_count")
    private long comment_count;
    @JSONField(name = "is_favorite")
    private boolean is_favorite;
    @JSONField(name = "title")
    private String title;
}


