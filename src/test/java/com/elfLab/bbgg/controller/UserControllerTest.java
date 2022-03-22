package com.elfLab.bbgg.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.elfLab.bbgg.model.User;
import com.elfLab.bbgg.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


@WebMvcTest(UserController.class) // API만 테스트
public class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc; // 의존해야하지만 등록되지 않은 Bean 사용하기 위해 Mockito 객체 주입받기
	
	@MockBean
	private UserServiceImpl userServiceImpl; // 가짜 Bean 등록
	
	@Test
	@DisplayName("회원가입 확인")
	public void joinUser() throws Exception{
		
		// given
		User user = new User();
		user.setUsername("사용자 아이디");
		user.setPassword("1234");
		user.setEmail("abcd@google.com");

		Map<String,String> resultMap = new HashMap<>();

		when(userServiceImpl.join(any())).thenReturn(resultMap);
		
		ObjectMapper objectMapper = new ObjectMapper(); // java 객체 => JSON
        String json = objectMapper.writeValueAsString(user);
        
		// when
        ResultActions resultActions = mockMvc.perform(post("/auth/joinProc")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andDo(print());
        
        // then
        resultActions
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andDo(print());
	}

}
