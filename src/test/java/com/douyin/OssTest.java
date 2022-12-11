package com.douyin;

import com.douyin.util.OssClient;
import org.junit.jupiter.api.Test;

/**
 * @Author hongxiaobin
 * @Time 2022/12/11-15:49
 */
public class OssTest {
    @Test
    public void ossTest() {
        System.out.println(OssClient.ossClientInitialization());
    }
}
