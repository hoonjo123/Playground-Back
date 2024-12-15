package com.swyp.playground.domain.login.service;

import com.swyp.playground.common.domain.TypeChange;
import com.swyp.playground.common.jwt.JwtTokenProvider;
import com.swyp.playground.common.redis.RedisService;
import com.swyp.playground.domain.login.dto.req.LoginReqDto;
import com.swyp.playground.domain.login.dto.res.LoginResDto;
import com.swyp.playground.domain.parent.domain.Parent;
import com.swyp.playground.domain.parent.repository.ParentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final ParentRepository parentRepository;


    public LoginResDto login(LoginReqDto loginReqDto) {
        String email = loginReqDto.getEmail();
        String password = loginReqDto.getPassword();

        // DB에서 사용자 조회
        Parent parent = parentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // 비밀번호 비교
        if (!passwordEncoder.matches(password, parent.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }

        // JWT 토큰 생성
        String nickname = parent.getNickname(); // Parent 객체에서 닉네임 가져오기
        Long parentId = parent.getParentId();
        String token = tokenProvider.generateToken(email, nickname, parentId);
        String refreshToken = tokenProvider.generateRefreshToken(email, nickname, parentId);

        // Redis에 토큰 저장
        redisService.saveAccessToken(email, nickname, token, 3600000); // Access Token: 1시간 유지
        redisService.saveRefreshToken(email, refreshToken, 604800000); // Refresh Token: 7일 유지
        // 7일 유지

        return LoginResDto.builder()
                .email(email)
                .nickname(parent.getNickname())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String token) {
        // Access Token에서 이메일 추출
        String email = tokenProvider.getEmailFromToken(token);

        //Redis에 저장할때 형식이 좀 달라서, 형식에 맞춰서 키를 조회함
        String accessKey = String.format("access_%s", email);

        if (!redisService.isTokenValid(accessKey)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        // Access Token 삭제
        redisService.deleteToken(accessKey);

        // Refresh Token 삭제
        String refreshKey = String.format("refresh_%s", email);
        redisService.deleteToken(refreshKey);
    }
}