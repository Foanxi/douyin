package com.douyin.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author foanxi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentModel {
    private String videoId;
    private String commentId;
}
