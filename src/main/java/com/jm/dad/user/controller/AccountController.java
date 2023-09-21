package com.jm.dad.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AccountController {

	// 로그인 페이지
	@GetMapping("/auth/login")
	public ModelAndView pageLogin(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("thymeleaf/user/account/login");
		mav.addObject("classType", "membership login");
		return mav;
	}
}
