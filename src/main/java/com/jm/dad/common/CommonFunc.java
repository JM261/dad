package com.jm.dad.common;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class CommonFunc {
	
	// 문자열 null, 빈 문자열("") 유효성 체크
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    // 문자열 null, 공백을 포함한 빈 문자열("") 유효성 체크
    public static boolean isNullOrWhitespace(String str) {
        return str == null || str.trim().isEmpty();
    }    
	
    // 문자열 null, 빈 문자열(""), 길이 유효성 체크
	public static boolean isVaildStr(String str, int len) {
		return !isNullOrEmpty(str) && str.length() <= len;
	}

	// 문자열 null, 빈 문자열(""), 길이, 정규표현식 유효성 체크
    public static boolean isVaildStr(String str, int len, String regex) {
        return isVaildStr(str, len) && Pattern.matches(regex, str);
    }
	
	// 문자열 "yyyyMMdd" 8자리 날짜형식 유효성 체크
	public static boolean isVaildBirthday(String birthday) {
		if (isNullOrEmpty(birthday) || birthday.length() < 8) {return false;}
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(birthday);
            return true;
        } catch (Exception e) {
            return false;
        }
	}
}
