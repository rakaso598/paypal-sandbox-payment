package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable()  // CSRF 보호 비활성화 (필요한 경우 활성화)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/paypal/**").permitAll()  // /api/paypal/** 경로는 인증 없이 접근 허용
                                .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
                )
                .formLogin(withDefaults())  // 기본 로그인 폼 활성화
                .logout(withDefaults());  // 기본 로그아웃 활성화

        return http.build();
    }
}
