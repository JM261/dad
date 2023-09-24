package com.jm.dad.user.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
	
	@GetMapping("/")
	public ModelAndView MainController(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("thymeleaf/main");
		System.out.println("test1");
		return mav;
	}
	
}
