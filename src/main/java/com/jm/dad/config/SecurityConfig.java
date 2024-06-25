package com.jm.dad.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import com.jm.dad.security.JwtAuthFilter;
import com.jm.dad.security.JwtAuthenticationEntryPoint;
import com.jm.dad.security.JwtTokenProvider;
import com.jm.dad.service.RedisService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    @Bean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
    
    private final JwtTokenProvider jwtTokenProvider;
    
    private final RedisService redisService;
    
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	return http
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basic -> basic.disable())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/common/**", "/login/**", "/join/**", "/logout", "/favicon.ico").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, redisService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handling -> handling
                      .authenticationEntryPoint(jwtAuthenticationEntryPoint()))
                .build();
    }
    
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
        		.requestMatchers("/common/**", "/login/**", "/join/**", "/logout", "/favicon.ico");
    }    
}
