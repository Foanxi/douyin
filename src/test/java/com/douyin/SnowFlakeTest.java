package com.douyin;

import com.douyin.util.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SnowFlakeTest {

    @Test
    void testGet() {
        System.out.println(SnowFlake.getDataCenterId());
    }
}
