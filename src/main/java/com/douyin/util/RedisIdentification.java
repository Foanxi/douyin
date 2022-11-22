package com.douyin.util;

/**
 * @author foanxi
 */
public class RedisIdentification {
    public static final String USER_QUERY_KEY = "user:query:";
    public static final Long USER_QUERY_TTL = 60L;

    public static final Long NULL_CACHE_TTL = 10L;
    public static final String NULL_CACHE_KEY = "null:cache:";

    public static final String LOCK_CACHE_KEY = "lock:cache:";
    public static final Long LOCK_CACHE_TTL = 10L;

    public static final String COMMENT_QUERY_KEY = "comment:query:";
    public static final Long COMMENT_QUERY_TTL = 60L;

    public static final String VIDEO_QUERY_KEY = "video:query:";
    public static final Long VIDEO_QUERY_TTL = 60L;
}
