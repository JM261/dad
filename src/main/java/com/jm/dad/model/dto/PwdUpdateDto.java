package com.jm.dad.model.dto;

import lombok.Data;

@Data
public class PwdUpdateDto {
	
	private String username;
	
	private String ogPwd;
	
	private String newPwd;
}