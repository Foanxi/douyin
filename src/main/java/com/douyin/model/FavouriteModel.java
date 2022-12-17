package com.douyin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author hongxiaobin
 * @Time 2022/12/18-0:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteModel {
    private Long videoId;
    private Long userId;
}
