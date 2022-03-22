package com.elfLab.bbgg.controller;

import com.elfLab.bbgg.dto.ResponseDto;
import com.elfLab.bbgg.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import com.elfLab.bbgg.service.UserService;

import java.util.Map;


@Controller
public class UserController {
	
	@Autowired
	private UserService userServiceImpl;
	
	
	// 회원가입 화면 조회  
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}


	// 회원 가입 요청
	@ResponseBody
	@PostMapping("/auth/joinProc")
	public ResponseDto join(@RequestBody User user) {

		return 	ResponseDto.sendData(userServiceImpl.join(user));
	}


	// 유저 아이디 중복 체크
	@ResponseBody
	@GetMapping("/auth/checkUsername")
	public ResponseDto checkUsername(@RequestParam("username") String username) {

		return ResponseDto.sendData(userServiceImpl.checkUsername(username));
	}


	// 로그인 화면 조회 
	@GetMapping("/auth/loginForm")
	public String loginForm() {			
		return "user/loginForm";
	}


	// 카카오 로그인 요청 콜백
	@GetMapping("auth/kakao/callback")
	public String kakaoCallback(String code) {
		String kakaoAccessToken = userServiceImpl.getKakaoAccessToken(code);
		userServiceImpl.getKakaoUserInfo(kakaoAccessToken);
		return "redirect:/";
	}

}
