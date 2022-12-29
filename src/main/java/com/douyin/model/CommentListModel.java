package com.douyin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author foanxi
 */
@Data
@AllArgsConstructor
public class CommentListModel {
    private Long id;
    private UserModel user;
    private String content;
    @JsonProperty(value = "create_date")
    private String createDate;
}
