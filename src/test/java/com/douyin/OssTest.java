package com.douyin;

import com.douyin.util.OssClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author hongxiaobin
 * @Time 2022/12/11-15:49
 */
@SpringBootTest
public class OssTest {

    @Test
    public void ossTest() {
        System.out.println(OssClient.ossClientInitialization());
    }
}
