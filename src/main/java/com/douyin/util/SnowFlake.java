package com.douyin.util;


import org.springframework.beans.factory.annotation.Value;

/**
 * @author foanxi
 */
public class SnowFlake {
    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 1480166465631L;

    /**
     * //序列号占用的位数
     */
    private final static long SEQUENCE_BIT = 12;
    /**
     * 机器标识占用的位数
     */
    private final static long MACHINE_BIT = 5;
    /**
     * 数据中心占用的位数
     */
    private final static long DATA_CENTER_BIT = 5;

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    /**
     * 数据中心Id
     */
    @Value("${snowflake.data-center-id}")
    private static long dataCenterId;
    /**
     * 机器标识Id
     */
    @Value("${snowflake.machine-id}")
    private static long machineId;
    /**
     * 序列号
     */
    private static long sequence = 0L;
    /**
     * 上一次时间戳
     */
    private static long lastStamp = -1L;

    public SnowFlake() {
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
    }

    /**
     * 产生下一个ID
     *
     * @return 返回下一个雪花算法生成的id
     */
    public static synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("时钟回拨，拒绝该id");
        }

        if (currStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;
        //时间戳部分 + 数据中心部分 + 机器标识部分 + 序列号部分
        return (currStamp - START_STAMP) << TIMESTAMP_LEFT
                | dataCenterId << DATA_CENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private static long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private static long getNewStamp() {
        return System.currentTimeMillis();
    }
}
