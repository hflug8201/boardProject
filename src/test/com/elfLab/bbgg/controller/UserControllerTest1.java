package com.elfLab.bbgg.controller;

import com.elfLab.bbgg.model.User;
import com.elfLab.bbgg.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 동은 테스트
@WebMvcTest
@Transactional
public class UserControllerTest1 {

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

        when(userServiceImpl.join(any())).thenReturn(1);

        ObjectMapper objectMapper = new ObjectMapper(); // java 객체 => JSON
        String json = objectMapper.writeValueAsString(user);

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/joinProc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print());

        // then
        ((ResultActions) resultActions)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

}


//TODO 회원가입 시, 아이디 정상입력 예외처리

//TODO 회원가입 시, 아이디 중복 예외처리

//TODO 회원가입 시, 아이디 길이 체크 예외처리

//TODO 회원가입 시, password 길이 체크 예외처리

//TODO 회원가입 시, pasword 정상입력 및 해시화 테스트

//TODO 회원가입 시, email 형식 여부 예외처리

//TODO 회원가입 시, email 빈값 예외처리

//TODO - ID 미입력 체크
//    - 비밀번호 미입력 체크
//    - ID / password 불일치 체크
//    - 소셜로그인 요청&응답 테스트
//    - 카카오 로그인 시, email 동의 미체크 시 처리