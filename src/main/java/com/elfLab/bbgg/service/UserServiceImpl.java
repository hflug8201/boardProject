package com.elfLab.bbgg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.elfLab.bbgg.mapper.UserMapper;
import com.elfLab.bbgg.model.KakaoProfile;
import com.elfLab.bbgg.model.OAuthToken;
import com.elfLab.bbgg.model.User;
import com.elfLab.bbgg.util.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {

	@Value("${elflab.key}")
	private String elflabKey;
	
	@Value("${elflab.redirect-uri}")
	private String elflabRedirectUri;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	// 회원가입
	@Override
	@Transactional
	public Map<String, String> join(User user) {
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "회원가입이 완료되었습니다." );
		
		if(CommonUtils.isUserId(user.getUsername()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "아이디는 3자 이상 12자 이하의 숫자, 영어 대/소문자로 입력해주세요.");
			
			return resultMap;
		}
		
		String rawPassword = user.getPassword();
		if(CommonUtils.isPassword(rawPassword) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "비밀번호는 3자 이상 12자 이하의 숫자, 영어 대/소문자로 입력해주세요.");
			
			return resultMap;
		}
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		if(CommonUtils.isNotEmpty(user.getEmail()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "이메일을 입력해주세요.");
			
			return resultMap;
		}
		if(CommonUtils.isEmail(user.getEmail()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "이메일을 형식에 맞게 입력해주세요.");
			
			return resultMap;
		}
		
		userMapper.join(user);
		return resultMap;
	}
	
	// 회원 찾기
	@Override
	@Transactional
	public User findUser(String username) {
		User user = userMapper.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		return user;
	}

	// 유저 아이디 중복 체크
	@Override
	@Transactional
	public Map<String, String> checkUsername(String username) {
		int count = userMapper.checkUsername(username);
		
		Map<String, String> resultMap = new HashMap<>();
		
		resultMap.put("messageType", count == 0 ? "success" : "failure");
		resultMap.put("message", count == 0 ? "사용하실 수 있는 아이디입니다." : username+"은 이미 있는 아이디입니다.");
		return resultMap;
	}
	
	
	
	// 카카오 oauth 로그인 : Access Token 받아오기
	@Override
	@Transactional
	public String getKakaoAccessToken(String code) {
		// POST 방식으로 key=value 타입의 데이터를 카카오 쪽으로 요청

		RestTemplate rt = new RestTemplate(); // http 요청 편리하게 가능

		// HttpHeader object 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // header에 Content-type 담기 = 전송한 데이터라고 알려줌

		// body 데이터 담을 HttpBody object 생성 => params 삽입
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "83f62558c42acf043dcc1b2506c81a64");
		params.add("redirect_uri", elflabRedirectUri + "/auth/kakao/callback");
		params.add("code", code); // code는 동적으로 받아서 사용

		// HttpEntity : HttpHeader와 HttpBody를 하나의 object에 담기 => exchange 함수가 HttpEntity를 필요로 함
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

		// 카카오로 http 요청 : POST 방식
		ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", // 토큰 발급 요청 주소
				HttpMethod.POST, // 요청 method
				kakaoTokenRequest, // http body의 데이터와 header의 값
				String.class // responseType(응답 받을 타입)
		);

		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;

		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());

		return oauthToken.getAccess_token();
	}

	// 카카오 회원정보 가져오기
	@Override
	@Transactional
	public void getKakaoUserInfo(String accessToken) {

		RestTemplate rt2 = new RestTemplate(); // http 요청 편리하게 가능

		// HttpHeader object 생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + accessToken);
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

		// 카카오로 사용자 정보 요청 : POST 방식
		ResponseEntity<String> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", // 토큰 발급 요청 주소
				HttpMethod.POST, // 요청 method
				kakaoProfileRequest, // http body의 데이터와 header의 값
				String.class // responseType(응답 받을 타입)
		);

		// kakaoProfile에 회원 정보 담기
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;

		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		// User object : username, password, email
		System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
		System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());

		// 카카오에서 받은 user 정보로 회원구성
		System.out.println("게시판 서버 유저네임 : " + kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId());
		System.out.println("게시판 서버 이메일 : " + kakaoProfile.getKakao_account().getEmail());
		System.out.println("게시판 서버 패스워드 : " + elflabKey);
		
		// userService에 user object 삽입 : 받아온 회원 정보로 강제로 회원가입
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId()).password(elflabKey) // service에서 패스워드 인코딩되므로 그대로 삽입
				.email(kakaoProfile.getKakao_account().getEmail()).oauth("kakao") // kakao로 로그인하면 kakao가 들어오도록 함
				.build();

		// 가입 여부 확인 => service의 회원찾기
		User originUser = findUser(kakaoUser.getUsername());

		// 회원가입 요청
		if (originUser.getUsername() == null) {
			System.out.println("기존 회원이 아니므로 자동 회원가입을 진행합니다");
			join(kakaoUser);
		}

		System.out.println("자동 로그인을 진행합니다");

		// null이 아니면 로그인 처리
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), elflabKey));
		SecurityContextHolder.getContext().setAuthentication(authentication); // 현재 세션에 사용자 저장

		System.out.println("자동 로그인 완료되었습니다");
	}
	
	// 카카오 연결 끊기 : 로그인은 성공했으나 회원가입이 실패한 경우 => 로그인한 유저의 엑세스 토큰 이용해 API 호출....?????
	@Override
	@Transactional
	public void unlinkKakao(String accessToken) {
		
		RestTemplate rt3 = new RestTemplate(); // http 요청
		
		HttpHeaders headers3 = new HttpHeaders();
		headers3.add("Authorization", "Bearer " + accessToken);
		
		HttpEntity<MultiValueMap<String, String>> kakaoUnlinkRequest = new HttpEntity<>(headers3);

		// 카카오 연결 끊기 요청 : POST 방식
		ResponseEntity<String> response3 = rt3.exchange("https://kapi.kakao.com/v1/user/unlink", // 토큰 발급 요청 주소
				HttpMethod.POST, // 요청 method
				kakaoUnlinkRequest, // http header의 값
				String.class // responseType(응답 받을 타입)
		);
		
		ObjectMapper objectMapper3 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		
	
		
		try {
			kakaoProfile = objectMapper3.readValue(response3.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		// 연결 끊기에 성공한 사용자 회원번호
		// System.out.println("연결 끊기에 성공한 사용자 회원번호 : " + kakaoProfile.getId());
		
	}
	

}
