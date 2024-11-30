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
                                "/auth/signup",
                                "/auth/login",
                                "/auth/logout",
                                "/find-friend/**",
                                "/playgrounds/**",
                                "/auth/send-email").permitAll()
                        .requestMatchers("/auth/users/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(tokenProvider, redisService),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
