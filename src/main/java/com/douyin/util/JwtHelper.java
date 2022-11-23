package com.douyin.util;

import io.jsonwebtoken.*;

import java.util.Date;

/**
 * token口令生成JwtHelper
 *
 * @author hongxiaobin
 * @Time 2022/10/11-11:22
 */
public class JwtHelper {
    private static final long TOKEN_EXPIRATION = 20 * 60 * 60 * 1_000;
    private static final String TOKEN_SIGN_KEY = "123456";

    /**
     * 生成token字符串
     *
     * @Param: Long userId, Integer userType
     * @Return: String
     */
    public static String createToken(Long userId) {
        return Jwts.builder()
                .setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    /**
     * 从Token字符串获取userid
     *
     * @Param: String token
     * @Return: Long
     */
    public static Long getUserId(String token) {
        if (token.isEmpty()) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return (Long) body.get("userId");
    }

    /**
     * 判读token是否有效
     *
     * @Param: String token
     * @Return: boolean
     */
    public static boolean isExpiration(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(TOKEN_SIGN_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            //过期出现异常，返回true
            return true;
        }
    }
}
