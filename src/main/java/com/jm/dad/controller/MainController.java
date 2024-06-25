package com.jm.dad.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.jm.dad.config.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	@GetMapping("/")
	public ModelAndView MainPage(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		ModelAndView mav = new ModelAndView("thymeleaf/user/main");
		
		return mav;
	}
}
