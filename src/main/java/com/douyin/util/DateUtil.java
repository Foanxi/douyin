package com.douyin.util;

import java.text.SimpleDateFormat;

/**
 * @author foanxi
 */
public class DateUtil {
    public static String timestampToDate(Long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm-DD");
        return simpleDateFormat.format(timestamp.toString());
    }

}
