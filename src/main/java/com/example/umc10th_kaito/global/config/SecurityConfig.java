package com.example.umc10th_kaito.global.config;

import com.example.umc10th_kaito.global.security.CustomAccessDenied;
import com.example.umc10th_kaito.global.security.CustomEntryPoint;
import com.example.umc10th_kaito.global.security.CustomUserDetailsService;
import com.example.umc10th_kaito.global.security.filter.JwtAuthFilter;
import com.example.umc10th_kaito.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDenied customAccessDenied;
    private final CustomEntryPoint customEntryPoint;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    // 로그인 없이 접근 가능한 URL 목록
    private final String[] allowUris = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/auth/**",
            "/error"
    };

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil, customUserDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // URI 허용 여부
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(allowUris).permitAll()
                        .anyRequest().authenticated()
                )
                // 폼 로그인 비활성화 (JWT 방식이므로 불필요)
                .formLogin(AbstractHttpConfigurer::disable)
                // 세션 비활성화 → Stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                // 예외 핸들러
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDenied)
                        .authenticationEntryPoint(customEntryPoint)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
