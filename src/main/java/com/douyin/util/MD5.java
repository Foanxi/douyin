package com.douyin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Author hongxiaobin
 * @Time 2022/10/25-8:43
 */
public class MD5 {
    public static String encrypt(String strSrc) {
        try {
            char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                    '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            byte[] bytes = strSrc.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (byte b : bytes) {
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！！！" + e);
        }
    }
}
