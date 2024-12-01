package com.swyp.playground.common.jwt;

import com.swyp.playground.common.redis.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;

    public JwtAuthFilter(JwtTokenProvider tokenProvider, RedisService redisService) {
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String accessToken = resolveToken(request);

        if (accessToken != null) {
            // 액세스 토큰 검증
            if (tokenProvider.validateToken(accessToken)) {
                authenticateUser(accessToken);
            } else if (isTokenExpired(accessToken)) {
                // 액세스 토큰 만료 시 리프레시 토큰 검증 및 재발급
                String refreshToken = resolveRefreshToken(request);
                if (refreshToken != null && tokenProvider.validateToken(refreshToken)) {
                    String email = tokenProvider.getEmailFromToken(refreshToken);
                    String nickname = tokenProvider.getNicknameFromToken(refreshToken);
                    if (redisService.isTokenValid(refreshToken)) {
                        // 새 액세스 토큰 발급
                        String newAccessToken = tokenProvider.generateToken(email,nickname);
                        response.setHeader("Access-Token", newAccessToken);
                        authenticateUser(newAccessToken);
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\": \"Invalid refresh token\", \"message\": \"Refresh token expired or not valid.\"}");
                        return;
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Access token expired\", \"message\": \"No valid refresh token provided.\"}");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid token\", \"message\": \"Token is invalid.\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void authenticateUser(String token) {
        String email = tokenProvider.getEmailFromToken(token);
        String nickname = tokenProvider.getNicknameFromToken(token);
        UserDetails userDetails = new User(email, "", Collections.emptyList());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, nickname, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean isTokenExpired(String token) {
        try {
            tokenProvider.validateToken(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        log.debug("Authorization 헤더: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Refresh-Token");
        log.debug("Refresh-Token 헤더: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
