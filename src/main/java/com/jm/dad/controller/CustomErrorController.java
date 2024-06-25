package com.jm.dad.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
    	
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
        if (status == null) {
            return new ModelAndView(new RedirectView("/"));
        } else {
            ModelAndView mav = new ModelAndView("thymeleaf/user/common/error");
            mav.addObject("status", status);
            return mav;
        }
    }
}
