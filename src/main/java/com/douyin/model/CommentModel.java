package com.douyin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author foanxi
 */
@Data
@AllArgsConstructor
public class CommentModel {
    private Long id;
    private UserModel user;
    private String content;
    @JsonProperty("create_date")
    private Timestamp createDate;
}
