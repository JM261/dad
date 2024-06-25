package com.jm.dad.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jm.dad.common.CommonFunc;
import com.jm.dad.config.auth.CustomUserDetails;

import static com.jm.dad.common.AlertMessage.*;

import java.time.Duration;

import com.jm.dad.model.dto.PwdUpdateDto;
import com.jm.dad.model.dto.UserLoginDto;
import com.jm.dad.model.dto.UserUpdateDto;
import com.jm.dad.model.entity.User;
import com.jm.dad.security.JwtTokenProvider;
import com.jm.dad.service.RedisService;
import com.jm.dad.service.UserService;
import com.jm.dad.utils.CookieUtil;
import com.jm.dad.utils.ValidationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {
	
	private final UserService userService;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	private final RedisService redisService;
	
	/**
	 * @DESC   : 로그인 페이지
	 * @UPDATE : 
	 */
	@GetMapping("/login")
	public ModelAndView loginPage(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("thymeleaf/user/account/login");
		
        Cookie[] cookies = request.getCookies();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth".equals(cookie.getName())) {
                    return new ModelAndView("redirect:/");
                }
            }
        }
		return mav;
	}
	
	/**
	 * @DESC   : 로그인 실행
	 * @UPDATE : 
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(UserLoginDto userLoginDto, HttpServletResponse response) {
		
		CookieUtil cookieUtil = new CookieUtil();
		
		try {
			User user = userService.findUserByUsername(userLoginDto.getUsername());
			
			if (user == null || !passwordEncoder.matches(userLoginDto.getPwd(), user.getPwd())){
				return new ResponseEntity<String>("아이디 또는 패스워드를 확인해 주세요.",HttpStatus.UNAUTHORIZED);
			}
			
	        Cookie Cookie = new Cookie("auth", jwtTokenProvider.generateAccessToken(user.getUsername())); // 액세스 토큰 쿠키에 세팅
	        
	        cookieUtil.setCookie(response, Cookie);
	        
	        redisService.setValues(user.getUsername(), jwtTokenProvider.generateRefreshToken(user.getUsername()), Duration.ofDays(7)); // 7일
	        
		} catch(Exception e) {
			log.error("Error", e);
			return new ResponseEntity<String>(ERR_MSG_1, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>("Y", HttpStatus.OK);
	}
	
	/**
	 * @DESC   : 회원가입 페이지
	 * @UPDATE : 
	 */
	@GetMapping("/join")
	public ModelAndView joinPage(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("thymeleaf/user/account/join");
		
		return mav;
	}
	
	/**
	 * @DESC   : 회원가입 실행
	 * @UPDATE : 
	 */
	@PostMapping("/join")
	public ResponseEntity<String> join(User user) {
		
		String vldRsltMsg = "";
		
		ValidationUtil validationUtil = new ValidationUtil();
		
		try {
			vldRsltMsg = validationUtil.userVaild(userService, user);
			
			if (!CommonFunc.isNullOrEmpty(vldRsltMsg)) {
				return new ResponseEntity<String>(vldRsltMsg, HttpStatus.BAD_REQUEST);
			} else {
				user.setEncryptedPwd(passwordEncoder.encode(user.getPwd()));
				userService.save(user);
			}
			return new ResponseEntity<String>("Y", HttpStatus.OK);
		} catch(Exception e) {
			log.error("Error", e);
			return new ResponseEntity<String>(ERR_MSG_1, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * @DESC   : 아이디 중복확인
	 * @UPDATE : 
	 */
	@GetMapping("/join/dupl-check/{userId}")
	public ResponseEntity<String> duplCheck(@PathVariable String userId) {
		
		try {
			if (userService.loadUserByUsername(userId) == null) {
				return new ResponseEntity<String>("Y", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("N", HttpStatus.OK);
			}
		} catch(Exception e) {
			log.error("Error", e);
			return new ResponseEntity<String>(ERR_MSG_1, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * @DESC   : 로그아웃 실행
	 * @UPDATE : 
	 */	
	@GetMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		
		CookieUtil cookieUtil = new CookieUtil();
		
		try {
			cookieUtil.delCookie(request, response, "auth");
			
	        SecurityContextHolder.clearContext();
	        
//	        redisService.deleteValues("");
		} catch(Exception e) {
			log.error("Error", e);
			return new ModelAndView("redirect:/login");
		}
        
		return new ModelAndView("redirect:/login");
	}	
	
	/**
	 * @DESC   : 개인정보 페이지
	 * @UPDATE : 
	 */
	@GetMapping("/privacy")
	public ModelAndView privacyPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		ModelAndView mav = new ModelAndView("thymeleaf/user/account/privacy");
		
		User user = userService.findUserByUsername(userDetails.getUsername());
		
		mav.addObject("updateForm",user);
		
		return mav;
	}
	
	/**
	 * @DESC   : 회원정보 수정 실행
	 * @UPDATE : 
	 */
	@PutMapping("/privacy")
	public ResponseEntity<String> privacy(@AuthenticationPrincipal CustomUserDetails userDetails, UserUpdateDto userUpdateDto) {
		
		String vldRsltMsg = "";
		
		ValidationUtil validationUtil = new ValidationUtil();
		
		try {
			vldRsltMsg = validationUtil.userUpdateVaild(userUpdateDto);
			
			System.out.println("vldRsltMsg : " + vldRsltMsg);
			
			if (!CommonFunc.isNullOrEmpty(vldRsltMsg)) {
				return new ResponseEntity<String>(vldRsltMsg, HttpStatus.BAD_REQUEST);
			} else {
				userService.update(userDetails.getUsername(), userUpdateDto);
			}
			return new ResponseEntity<String>("Y", HttpStatus.OK);
		} catch(Exception e) {
			log.error("Error", e);
			return new ResponseEntity<String>(ERR_MSG_1, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @DESC   : 패스워드 변경 실행
	 * @UPDATE : 
	 */
	@PostMapping("/privacy")
	public ResponseEntity<String> privacyPwd(@AuthenticationPrincipal CustomUserDetails userDetails, PwdUpdateDto pwdUpdateDto) {
		
		String vldRsltMsg = "";
		
		ValidationUtil validationUtil = new ValidationUtil();
		
		try {
			String username = userDetails.getUsername();
			
			User user = userService.findUserByUsername(username);
			
			if (user == null || !passwordEncoder.matches(pwdUpdateDto.getOgPwd(), user.getPwd())){
				return new ResponseEntity<String>("기존 패스워드를 확인해 주세요.",HttpStatus.UNAUTHORIZED);
			}
			
			vldRsltMsg = validationUtil.pwdUpdateVaild(pwdUpdateDto);
			
			if (!CommonFunc.isNullOrEmpty(vldRsltMsg)) {
				return new ResponseEntity<String>(vldRsltMsg, HttpStatus.BAD_REQUEST);
			} else {
				pwdUpdateDto.setNewPwd(passwordEncoder.encode(pwdUpdateDto.getNewPwd()));
				userService.update(username, pwdUpdateDto);
			}
			return new ResponseEntity<String>("Y", HttpStatus.OK);
		} catch(Exception e) {
			log.error("Error", e);
			return new ResponseEntity<String>(ERR_MSG_1, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
}
