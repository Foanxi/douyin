package com.douyin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;


@SpringBootTest
public class TimeTest {

    @Test
    public void Timestamp2Date(){
        String Timestamp = "1508824283292";
        String Timestamp2 = "1508824283295";
        long timestampLong = Long.parseLong(Timestamp);
        long timestampLong2 = Long.parseLong(Timestamp2);
        System.out.println(timestampLong<timestampLong2);
        Date date = new Date(timestampLong);
        Date date2 = new Date(timestampLong2);
        System.out.println(date.compareTo(date2));
    }
}
