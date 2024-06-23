package com.jm.dad.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
	
	public void setCookie (HttpServletResponse response, Cookie Cookie) {
		
        Cookie.setHttpOnly(true);
        Cookie.setSecure(true);
        Cookie.setPath("/");
        response.addCookie(Cookie); 
	}
	
	public void delCookie (HttpServletRequest request, HttpServletResponse response, String cookieNm) {
		
	    Cookie[] cookies = request.getCookies();
	    
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookieNm.equals(cookie.getName())) {
	                cookie.setMaxAge(0);
	                response.addCookie(cookie);
	            }
	        }
	    }
	}
}
