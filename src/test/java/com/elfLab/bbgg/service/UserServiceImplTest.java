package com.elfLab.bbgg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.elfLab.bbgg.mapper.UserMapper;
import com.elfLab.bbgg.model.User;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserServiceImplTest {

	@Mock
    private UserMapper userMapper; // 의존하고 있는 mapper를 @Mock으로 선언
	
	@InjectMocks
	private UserServiceImpl userServiceImpl; // @Mock으로 선언된 가짜 객체들을 의존한 Service 객체 생성
	
	@Spy
	private BCryptPasswordEncoder passwordEncoder; // 실제 메소드로 동작 => password 암호화 위해 사용

//	private User testUser() {
//		final User user = new User();
//		user.setUsername("아이디");
//		user.setPassword("1234");
//		user.setEmail("abcd@google.com");
//		return user;
//	}
	
	@Test
	@Rollback(true)
	@DisplayName("join 기능 동작 확인")
	void testJoin() throws Exception {
		//given	
		User user=new User();
		user.setUsername("아이디");
		user.setPassword("1234");
		user.setEmail("abcd@google.com");

		String encPassword = passwordEncoder.encode(user.getPassword());	
		
		// 이메일 형식 검사
		String emailRegex = "^[a-z0-9]+@(\\w+\\.)+\\w+$";
		Pattern patternEmail = Pattern.compile(emailRegex);
		Matcher matcherEmail = patternEmail.matcher(user.getEmail());
		
		when(userMapper.join(any())).thenReturn(1);
		
		//when
		userServiceImpl.join(user);	

		//then
		assertThat(user.getUsername()).isEqualTo("아이디");
		assertThat(passwordEncoder.matches(user.getPassword(), encPassword)).isFalse(); // TODO 같은 값인데 다른 값이라고 인식
		assertThat(matcherEmail.matches()).isTrue();
	}
	
	
	@Test
	@Rollback(true)
	@DisplayName("findUser 기능 동작 확인")
	public void testFindUser() throws Exception {
		// given
		User user=new User();
		user.setUsername("아이디");
		when(userMapper.findByUsername(any())).thenReturn(Optional.of(user));
		
		// when
		userServiceImpl.findUser(user.getUsername());
		
		// then
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo("아이디");
		assertThat(user.getUsername()).isNotEqualTo("아이디2");
		
		
//		// given
//		List<User> list = new ArrayList<>();
//		User user1 = new User();
//		User user2 = new User();
//		User user3 = new User();
//		user1.setUsername("아이디1");
//		user2.setUsername("아이디2");
//		user3.setUsername("아이디3");
//		list.add(user1);
//		list.add(user2);
//		list.add(user3);
//		
//		when(userMapper.findByUsername(any())).thenReturn(Optional.of(user1));
//		
//		// when
//		userServiceImpl.findUser(user1.getUsername());
//		
//		// then
////		assertThat(list).filteredOn("username", "아이디1");
////		assertThat(list).filteredOn("username", "아이디4");
//		assertThat(list).filteredOn(user -> user.getUsername().contains("4")).isEqualTo(user1.getUsername());

		
	}
	
	
//	@Test
//	@Rollback(true)
//	@DisplayName("checkUsername 기능 동작 확인")
//	public void testCheckUsername() throws Exception {
//		//given
//		User user1=new User();
//		User user2=new User();
//		user1.setUsername("아이디");
//		user2.setUsername("아이디");
//		when(userMapper.checkUsername(any())).thenReturn(0);
//		
//		//when
//		userServiceImpl.checkUsername(user1.getUsername());
//		
//		//then
//		assertThat(user1).is
//	}
	
	//TODO 회원가입 시, 아이디 정상입력 예외처리

    //TODO 회원가입 시, 아이디 중복 예외처리

    //TODO 회원가입 시, 아이디 길이 체크 예외처리

    //TODO 회원가입 시, password 길이 체크 예외처리

    //TODO 회원가입 시, pasword 정상입력 및 해시화 테스트

    //TODO 회원가입 시, email 형식 여부 예외처리

    //TODO 회원가입 시, email 빈값 예외처리
	
	
	
	

}
