package com.elfLab.bbgg.util;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

	// 빈 값 체크
	public static boolean isNotEmpty(Object obj) {
		if(obj == null) {
			return false;
		}
		if((obj instanceof String) && (((String) obj).trim().length() == 0)) {
			return false;
		}
		if((obj instanceof Map) && ((Map) obj).isEmpty()) {
			return false;
		}
		if((obj instanceof List) && ((List) obj).isEmpty()) {
			return false;
		}
		return true;
	}
	
	// 아이디 유효성 검사
	public static boolean isUserId(String str) {
		String userIdRegex = "^[a-zA-Z0-9]+$";
		boolean patternUserId = Pattern.matches(userIdRegex, str);
		if(patternUserId == false) {
			return false;
		}
		if(str.length() < 3 || str.length() > 12) {
			return false;
		}
		return true;
	}
	
	// 비밀번호 유효성 검사
	public static boolean isPassword(String str) {
		String passwordRegex = "^[a-zA-Z0-9]+$";
		boolean patternPassword = Pattern.matches(passwordRegex, str);
		if(patternPassword == false) {
			return false;
		}
		if(str.length() < 3 || str.length() > 12) {
			return false;
		}
		return true;
	}
	
	// 이메일 유효성 검사
	public static boolean isEmail(String str) {
		String emailRegex = "^[a-zA-Z0-9_.]+@(\\w+\\.)+\\w+$";
		boolean patternEmail = Pattern.matches(emailRegex, str);
		if(patternEmail == false) {
			return false;
		}
		return true;
	}
}
