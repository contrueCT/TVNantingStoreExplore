package com.contrue.util.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author confff
 */
public class JWTUtil {

    // 访问令牌过期时间（15分钟）
    private static final long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000;

    // 刷新令牌过期时间（7天）
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    private static final SecretKey SECRET_KEY = KeyLoader.getSecretKey();

    /**
     * 生成token
     * @param expiration 有效时间
     * @param claims 声明内容
     * @return
     */
    public static String generateToken(String subjectId, long expiration, Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime()+expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subjectId)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(KeyLoader.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成访问token
     * @param subjectType 用户类型（User/Store）
     * @param subjectName
     * @return
     */
    public static String generateAccessToken(String subjectId, String subjectType, String subjectName) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("subjectType", subjectType);
        claims.put("subjectName", subjectName);
        return generateToken(subjectId, ACCESS_TOKEN_EXPIRATION, claims);
    }

    public static String generateRefreshToken(String userId, String subjectType, String subjectName) {
        Map<String, Object> claims = new HashMap<>();
        String refreshId = UUID.randomUUID().toString();
        claims.put("subjectType", subjectType);
        claims.put("subjectName", subjectName);
        claims.put("refreshId", refreshId);
        return generateToken(userId, REFRESH_TOKEN_EXPIRATION, claims);
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getSubjectId(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public static String getSubjectType(String token) {
        Claims claims = parseToken(token);
        return claims.get("subjectType", String.class);
    }

    public static String getSubjectName(String token) {
        Claims claims = parseToken(token);
        return claims.get("subjectName", String.class);
    }

    public static Date getExpiration(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration();
    }

    private static boolean isTokenExpired(String token) {
        Date expiration = getExpiration(token);
        return expiration.before(new Date());
    }

    public static boolean validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return isTokenExpired(token);
        }catch (Exception e){
            return false;
        }
    }

    public static String refreshAccessToken(String refreshToken) {
        if(isTokenExpired(refreshToken)){
            String subjectId = getSubjectId(refreshToken);
            String subjectType = getSubjectType(refreshToken);
            String subjectName = getSubjectName(refreshToken);
            return generateAccessToken(subjectId, subjectType, subjectName);
        }
        return null;
    }




}