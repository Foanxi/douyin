package com.douyin.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author hongxiaobin
 * @Time 2022/11/3-16:32
 */

public class CreateJson {
    private final static String HTTP_STATUS = "http_status";
    private final static String STATUS_CODE = "status_code";
    private final static String STATUS_MSG = "status_msg";

    public static JSONObject createJson(Integer http_status, Integer status_code, String status_msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(HTTP_STATUS, http_status);
        jsonObject.put(STATUS_CODE, status_code);
        jsonObject.put(STATUS_MSG, status_msg);
        return jsonObject;
    }
}
