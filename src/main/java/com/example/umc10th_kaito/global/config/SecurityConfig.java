package com.example.umc10th_kaito.global.config;

import com.example.umc10th_kaito.global.security.exception.CustomAccessDenied;
import com.example.umc10th_kaito.global.security.exception.CustomEntryPoint;
import com.example.umc10th_kaito.global.security.filter.JwtAuthFilter;
import com.example.umc10th_kaito.global.security.handler.OAuthSuccessHandler;
import com.example.umc10th_kaito.global.security.service.CustomOAuthService;
import com.example.umc10th_kaito.global.security.service.CustomUserDetailsService;
import com.example.umc10th_kaito.global.security.util.JwtUtil;
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
    private final CustomOAuthService customOAuthService;  // 추가

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtUtil, customUserDetailsService);
    }

    @Bean
    public OAuthSuccessHandler oAuthSuccessHandler() {  // 추가
        return new OAuthSuccessHandler(jwtUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/auth/**",
                                "/oauth/**",  // OAuth 콜백 주소 허용
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                // OAuth 세션이 필요하므로 IF_REQUIRED로 변경
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                // OAuth 설정 추가
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(auth -> auth
                                .baseUri("/oauth/authorize")
                        )
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/oauth/callback/*")
                        )
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuthService)
                        )
                        .successHandler(oAuthSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
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
