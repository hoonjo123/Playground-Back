package com.swyp.playground.common.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 토큰 저장
    public void saveAccessToken(String email, String nickname, String accessToken, long duration) {
        String accessKey = String.format("access_%s", email);
        String accessValue = String.format("email:%s,nickname:%s,token:%s", email, nickname, accessToken);
        redisTemplate.opsForValue().set(accessKey, accessValue, duration, TimeUnit.MILLISECONDS);
    }


    public void saveRefreshToken(String email, String refreshToken, long duration) {
        String refreshKey = String.format("refresh_%s", email);
        redisTemplate.opsForValue().set(refreshKey, refreshToken, duration, TimeUnit.MILLISECONDS);
    }

    // 토큰 조회
    public String getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 토큰 삭제
    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
    public void deleteRefreshToken(String email) {
        String refreshKey = "refresh_" + email;
        redisTemplate.delete(refreshKey);
    }

    // 토큰 검증
    public boolean isTokenValid(String key) {
        return redisTemplate.hasKey(key);
    }
}
