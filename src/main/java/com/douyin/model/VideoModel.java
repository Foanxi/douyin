package com.douyin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author foanxi
 */
@Data
@AllArgsConstructor
public class VideoModel {
    private long id;
    private UserModel author;

    @JsonProperty("play_url")
    private String playUrl;

    @JsonProperty("cover_url")
    private String coverUrl;

    @JsonProperty("favorite_count")
    private long favoriteCount;

    @JsonProperty("comment_count")
    private long commentCount;

    @JsonProperty("is_favorite")
    private boolean isFavorite;
    @JsonProperty("title")
    private String title;
}


