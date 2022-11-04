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

    public static JSONObject createJson(Integer httpStatus, Integer statusCode, String statusMsg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(HTTP_STATUS, httpStatus);
        jsonObject.put(STATUS_CODE, statusCode);
        jsonObject.put(STATUS_MSG, statusMsg);
        return jsonObject;
    }
}
