package org.blog.blogflatformproject.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenizer {
    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public static Long ACCESS_TOKEN_EXPIRE_COUNT = 2*60*60*1000L;//30분 엑세스토큰의 유지시간
    public static Long REFRESH_TOKEN_EXPIRE_COUT = 7*24*60*60*1000L;//7일

    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret){
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    //token create
    private String createToken(Long id , String email , String name , String username , List<String> roles , Long expire , byte[] secretKey){
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("roles" , roles);
        claims.put("userId",id);
        claims.put("name" , name);
        claims.put("username" , username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }
    public static Key getSigningKey(byte[] secretKey){
        return Keys.hmacShaKeyFor(secretKey);
    }

    //AccessToken생성
    public String createAccessToken(Long id , String email , String name , String username , List<String> roles){
        return createToken(id,email,name , username ,roles,ACCESS_TOKEN_EXPIRE_COUNT,accessSecret);
    }
    //RefreshToken생성
    public String createRefreshToken(Long id , String email , String name , String username , List<String> roles){
        return createToken(id,email,name,username ,roles,REFRESH_TOKEN_EXPIRE_COUT,refreshSecret);
    }

    public Long getUserIdFromToken(String token){
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token,accessSecret);
        return Long.valueOf((Integer)claims.get("userId"));
    }

    public Claims parseToken(String token, byte[] secretKey){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }


    // Token 유효성 검증
    public boolean validateToken(String token, byte[] secretKey) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
        } catch (Exception e) {
            log.error("Token invalid", e);
        }
        return false;
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }
}
