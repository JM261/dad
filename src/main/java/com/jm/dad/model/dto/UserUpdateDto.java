package com.jm.dad.model.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
	
	private String name;
	
	private String nickname;
	
	private String phone;
	
	private String birthday;
	
	private String email;
}
