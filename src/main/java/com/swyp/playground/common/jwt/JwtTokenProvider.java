package com.swyp.playground.common.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret:default-secret-key}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}")
    private long expiration;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration;


    // JWT 생성
    public String generateToken(String email,String nickname, Long parentId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("nickname",nickname)
                .claim("parentId", parentId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(String email,String nickname, Long parentId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("nickname",nickname)
                .claim("parentId", parentId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("잘못된 토큰: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("서명 검증 실패: {}", e.getMessage());
        }
        return false;
    }

    // JWT에서 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public String getNicknameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("nickname", String.class);
    }
    public Long getParentIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("parentId", Long.class);
    }
}
