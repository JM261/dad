package com.jm.dad.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jm.dad.service.RedisService;
import com.jm.dad.utils.CookieUtil;
import com.jm.dad.utils.EncryptionUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final RedisService redisService;
	
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        
        String token = null;
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
        	
        	CookieUtil cookieUtil = new CookieUtil();
        	
        	EncryptionUtil encryptionUtil = new EncryptionUtil();
        	
            String resultStr = jwtTokenProvider.validateToken(token);
            
            Claims info = jwtTokenProvider.getUserInfoFromToken(token);
            
            String userId = encryptionUtil.decrypt(info.get("sub", String.class));
            
            if ("E".equals(resultStr)) { // 액세스 토큰 유효기간 만료
            	
            	String refreshTk = redisService.getValues(userId); // 리프레시 토큰
            	
            	if (refreshTk == null) { // redis에 리프레시 토큰이 없으면 => 재 로그인
            		
                	cookieUtil.delCookie(request, response, "auth");
                	
                	SecurityContextHolder.clearContext();
                	
            	} else { // 액세스 토큰 재발급
            		
        	        Cookie Cookie = new Cookie("auth", jwtTokenProvider.generateAccessToken(userId));
        	        
        	        cookieUtil.setCookie(response, Cookie);
            		
        	        jwtTokenProvider.setAuthentication(userId);
            	}
            	
            } else if ("W".equals(resultStr)) { // 유효하지 않은 형식의 토큰
            	
            	cookieUtil.delCookie(request, response, "auth");
            	
            	SecurityContextHolder.clearContext();
            	
            } else { // 유효한 토큰 => 다음 단계로 이동
            	
            	jwtTokenProvider.setAuthentication(userId);
            }
	        
        } else {
        	
            SecurityContextHolder.clearContext();
        }    	
    	
        filterChain.doFilter(request, response);
    }
}
