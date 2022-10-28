package com.mpush.tools.common;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mpush.tools.log.Logs;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author
 * @date 2022/9/23 14:23
 * notes:
 */
public class JWTUtils {

    private static final String SIGN = "QW!@#as17$&#";
    private static final String ISSUER = "auth0";

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken(Map<String, String> map) {

        Calendar instance = Calendar.getInstance();
        //设置过期时间 7天
        instance.add(Calendar.DATE, 7);

        String token = JWT.create()
                .withIssuer(ISSUER)    // 发布者
                .withIssuedAt(new Date())   // 生成签名的时间
                .withExpiresAt(instance.getTime())   // 生成签名的有效期,小时
                .withClaim("userId", map.get("userId")) // 插入数据
                .sign(Algorithm.HMAC256(SIGN));
        return token;
    }

    /**
     * 验证token
     *
     * @return
     */
    public static DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SIGN);
        DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer(ISSUER) //匹配指定的token发布者
                .build().verify(token);

        return jwt;
    }

    /**
     * 根据key获取payload的值
     *
     * @param key
     * @param token
     * @return
     */
    public static String getPayload(String key, String token) {
        DecodedJWT decoded;
        String errorVal = "";
        try {
            decoded = JWTUtils.verify(token);//验证token
        } catch (SignatureVerificationException e) {
            Logs.PUSH.info(e.getMessage(), "签名异常");
            return errorVal;
        } catch (TokenExpiredException e) {
            Logs.PUSH.info(e.getMessage(), "token过期");
            return errorVal;
        } catch (AlgorithmMismatchException e) {
            Logs.PUSH.info(e.getMessage(), "算法不匹配");
            return errorVal;
        } catch (Exception e) {
            Logs.PUSH.info(e.getMessage(), "token无效");
            return errorVal;
        }
        Claim claim = decoded.getClaim(key);
        if (claim == null)
            return errorVal;
        return claim.asString();
    }
}
