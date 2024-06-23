package com.jm.dad.utils;

import static com.jm.dad.common.CommonFunc.*;

import com.jm.dad.model.dto.PwdUpdateDto;
import com.jm.dad.model.dto.UserUpdateDto;
import com.jm.dad.model.entity.User;
import com.jm.dad.service.UserService;

public class ValidationUtil {
	
    private final static String REGEX_ID = "^[a-zA-Z0-9_-]{5,20}$"; // 아이디 정규표현식

    private final static String REGEX_PWD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\\\\\|\\[\\]{};:'\",.<>\\/?]).{8,16}$"; // 패스워드 정규표현식
    
    private final static String REGEX_NUM = "\\d+"; // 숫자형식 문자열인지 체크용도 정규표현식
    
    private final static String REGEX_EMAIL  = "[a-z0-9]+@[a-z]+\\.[a-z]{2,3}"; // 이메일형식인지 체크용도 정규표현식
    
    // 회원가입 유효성 체크
	public String userVaild(UserService userService, User user) {
		
		String msg = "";
		
		String userId = user.getUsername(); // 아이디
		String pwd = user.getPwd(); // 패스워드
		String name = user.getName(); // 이름
		String nickname = user.getNickname(); // 닉네임
		String phone = user.getPhone(); // 연락처
		String birthday = user.getBirthday(); // 생년월일
		String email = user.getEmail(); // 이메일
		
		if (userService.loadUserByUsername(userId) != null) {
			msg = "이미 존재하는 아이디입니다.";
		} else if (!isVaildStr(userId, 20, REGEX_ID)) {
			msg = "아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.";
		} else if (!isVaildStr(pwd, 20, REGEX_PWD)) {
			msg = "패스워드는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.";
		} else if (!isVaildStr(name, 20)) {
			msg = "이름을 입력해 주세요.";
		} else if (!isVaildStr(nickname, 20)) {
			msg = "닉네임을 입력해 주세요.";
		} else if (!isVaildStr(phone, 20, REGEX_NUM)) {
			msg = "연락처는 숫자만 입력해 주세요 ex) 01012345678";
		} else if (!isVaildBirthday(birthday)) {
			msg = "생년월일은 숫자만 8자리, 존재하는 날짜를 입력해 주세요 ex) 19911231";
		} else if (!isVaildStr(email, 200, REGEX_EMAIL)) {
			msg = "이메일 주소 형식이 올바른지 확인해 주세요.";
		}
		
		return msg;
	}
	
	// 회원정보 수정 유효성 체크
	public String userUpdateVaild(UserUpdateDto user) {
		
		String msg = "";
		
		String name = user.getName(); // 이름
		String nickname = user.getNickname(); // 닉네임
		String phone = user.getPhone(); // 연락처
		String birthday = user.getBirthday(); // 생년월일
		String email = user.getEmail(); // 이메일
		
		if (!isVaildStr(name, 20)) {
			msg = "이름을 입력해 주세요.";
		} else if (!isVaildStr(nickname, 20)) {
			msg = "닉네임을 입력해 주세요.";
		} else if (!isVaildStr(phone, 20, REGEX_NUM)) {
			msg = "연락처는 숫자만 입력해 주세요 ex) 01012345678";
		} else if (!isVaildBirthday(birthday)) {
			msg = "생년월일은 숫자만 8자리, 존재하는 날짜를 입력해 주세요 ex) 19911231";
		} else if (!isVaildStr(email, 200, REGEX_EMAIL)) {
			msg = "이메일 주소 형식이 올바른지 확인해 주세요.";
		}
		
		return msg;
	}
	
	// 패스워드 변경 유효성 체크
	public String pwdUpdateVaild(PwdUpdateDto pwdUpdateDto) {
		
		String msg = "";
		
		String newPwd = pwdUpdateDto.getNewPwd(); // 변경 패스워드
		
		if (!isVaildStr(newPwd, 20, REGEX_PWD)) {
			msg = "패스워드는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.";
		}
		
		return msg;
	}
}
