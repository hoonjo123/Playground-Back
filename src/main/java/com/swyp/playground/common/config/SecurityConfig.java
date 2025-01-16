package com.swyp.playground.common.config;

import com.swyp.playground.common.jwt.JwtAuthFilter;
import com.swyp.playground.common.jwt.JwtTokenProvider;
import com.swyp.playground.common.redis.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final RedisService redisService;

    public SecurityConfig(JwtTokenProvider tokenProvider, RedisService redisService) {
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/auth/**",
                                "/find-friend-list/**",
                                "/find-friend/**",
                                "/comment/**"
                                ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(tokenProvider, redisService),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키를 사용할 경우 true로 설정
        config.addAllowedOriginPattern("*"); // 모든 도메인 허용 (*로 설정)
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
