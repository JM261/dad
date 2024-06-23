package com.jm.dad.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.jm.dad.service.UserService;
import com.jm.dad.utils.EncryptionUtil;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.token.access-expiration-time}")
    private int jwtExpirationAccess;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private int jwtExpirationRefresh;
    
    private Key key;
    
    private EncryptionUtil encryptionUtil;
    
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        key = Keys.hmacShaKeyFor(bytes);
        encryptionUtil = new EncryptionUtil();
    }
    
    private final UserService userService;
    
    // 액세스 토큰 생성
    public String generateAccessToken(String username) {
    	
    	Map<String, Object> claims = new HashMap<>();
    	claims.put("sub", encryptionUtil.encrypt(username));
    	
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationAccess))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }
    
    // 리프레시 토큰 생성
    public String generateRefreshToken(String username) {
    	Map<String, Object> claims = new HashMap<>();
    	claims.put("sub", username);
        return Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationRefresh))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }    

    // 토큰 검증
    public String validateToken(String token) {
    	String resultStr = "W";
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return null;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
            resultStr = "E";
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return resultStr;
    }
    
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
    	Claims claims = null;
        try {
        	claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ex) {
            claims = ex.getClaims();
        }
        return claims;
    }

    // 인증 객체 생성 & 세팅
    public void setAuthentication(String username) {
        UserDetails userDetails = userService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
    }    
}
