package com.elfLab.bbgg.service;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.elfLab.bbgg.model.User;
import com.fasterxml.jackson.databind.JsonNode;

public interface UserService {
	
	// 사용자 추가 / 회원가입
	@Transactional
	public Map<String, String> join(User user);

	// 회원 찾기
	@Transactional
	public User findUser(String username);
	
	// 유저 아이디 중복 체크
	@Transactional
	public Map<String, String> checkUsername(String username);
	
	// 카카오 : Access Token 받아오기
	@Transactional
	public String getKakaoAccessToken(String code);
	
	// 카카오 : Access Token으로 사용자 정보 가져오기
	@Transactional
	public void getKakaoUserInfo(String accessToken);
	
	// 카카오 연결 끊기
	@Transactional
	public void unlinkKakao(String accessToken);
		
}
