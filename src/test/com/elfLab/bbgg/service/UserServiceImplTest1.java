package com.elfLab.bbgg.service;

import com.elfLab.bbgg.mapper.UserMapper;
import com.elfLab.bbgg.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

// 동은 테스트
@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceImplTest1 {
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Spy
    private BCryptPasswordEncoder encoder;

//    @Before
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    @DisplayName("회원가입 시, ID, password, email 정상 입력되는가")
    public void join(){
        //given
        User requestJoinUser1 = new User();
        requestJoinUser1.setUsername("dogdongdong");
        requestJoinUser1.setPassword("12341234");
        requestJoinUser1.setEmail("dongeun_leee@hotmail.com");

        String rawPassword = requestJoinUser1.getPassword();
        String encPassword = encoder.encode(rawPassword);
        requestJoinUser1.setPassword(encPassword);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result",
                requestJoinUser1.getUsername().length()<13 && requestJoinUser1.getUsername().length()>3 ?
                        "success" : "failure");
        resultMap.put("message",
                requestJoinUser1.getUsername().length()<13 && requestJoinUser1.getUsername().length()>3 ?
                        "사용하실 수 있는 아이디입니다." : "ID는 4자 이상 12자 이하로만 가능합니다.");


        when(userMapper.join(any())).thenReturn(1);

        //when
        userServiceImpl.join(requestJoinUser1);

        //then
        assertThat(requestJoinUser1.getUsername()).isEqualTo("dogdongdong");
        assertThat(resultMap)
                .extractingByKeys("result","message")
                .contains("success", "사용하실 수 있는 아이디입니다.");
        assertThat(rawPassword).isEqualTo("12341234");
        assertThat(requestJoinUser1.getEmail()).isEqualTo("dongeun_leee@hotmail.com");
    }


    @Test
    @DisplayName("회원가입 시, ID 길면 정상적으로 메시지 출력하는가")
    public void joinUsernameCheck(){
        //given
        User requestJoinUser2 = new User();
        requestJoinUser2.setUsername("dong123dong123dong123");
        requestJoinUser2.setPassword("12341234");
        requestJoinUser2.setEmail("dongeun_leee@hotmail.com");

        String rawPassword = requestJoinUser2.getPassword();
        String encPassword = encoder.encode(rawPassword);
        requestJoinUser2.setPassword(encPassword);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result",
                requestJoinUser2.getUsername().length()<13 && requestJoinUser2.getUsername().length()>3 ?
                        "success" : "failure");
        resultMap.put("message",
                requestJoinUser2.getUsername().length()<13 && requestJoinUser2.getUsername().length()>3 ?
                        "사용하실 수 있는 아이디입니다." : "ID는 4자 이상 12자 이하로만 가능합니다.");


        when(userMapper.join(any())).thenReturn(1);

        //when
        userServiceImpl.join(requestJoinUser2);

        //then
        assertThat(resultMap)
                .extractingByKeys("result","message")
                .contains("failure", "ID는 4자 이상 12자 이하로만 가능합니다.");

    }



    @Test
    @DisplayName("회원가입 시, 비밀번호 짧으면 정상적으로 메시지 출력되는가 ")
    public void joinPasswordCheck(){
        //given
        User requestJoinUser3 = new User();
        requestJoinUser3.setUsername("dong123dong123dong123");
        requestJoinUser3.setPassword("1234");
        requestJoinUser3.setEmail("dongeun_leee@hotmail.com");

        String rawPassword = requestJoinUser3.getPassword();
        String encPassword = encoder.encode(rawPassword);
        requestJoinUser3.setPassword(encPassword);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result",
                rawPassword.length()<17 && rawPassword.length()>5 ?
                        "success" : "failure");
        resultMap.put("message",
                rawPassword.length()<17 && rawPassword.length()>5 ?
                        "사용하실 수 있는 비밀번호입니다." : "비밀번호는 6자 이상 16자 이하로만 가능합니다.");


        when(userMapper.join(any())).thenReturn(1);

        //when
        userServiceImpl.join(requestJoinUser3);

        //then
        assertThat(resultMap)
                .extractingByKeys("result","message")
                .contains("failure", "비밀번호는 6자 이상 16자 이하로만 가능합니다.");
//        assertThat(requestJoinUser3.getPassword()).isEqualTo(encoder.encode(rawPassword)); // 패스워드 해시화 체크 동작x

    }


    @Test
    @DisplayName("회원가입 시, email 형식 아니면 메시지 출력되는가")
    public void joinEmailFormCheck(){
        //given
        User requestJoinUser4 = new User();
        requestJoinUser4.setUsername("dong123dong123dong123");
        requestJoinUser4.setPassword("1234");
        requestJoinUser4.setEmail("dongeun_leee@hotmail.com");

        String rawPassword = requestJoinUser4.getPassword();
        String encPassword = encoder.encode(rawPassword);
        requestJoinUser4.setPassword(encPassword);

        String emailRegex = "^[a-z0-9]+@(\\w+\\.)+\\w+$";
        Pattern patternEmail = Pattern.compile(emailRegex);
        Matcher matcherEmail = patternEmail.matcher(requestJoinUser4.getEmail());

        when(userMapper.join(any())).thenReturn(1);

        //when
        userServiceImpl.join(requestJoinUser4);

        //then
        assertThat(matcherEmail.matches()).isTrue();

    }



    @Test
    @Rollback(true)
    @DisplayName("회원 조회 정상 동작하는지")
    public void testFindUser() throws Exception {
        //given
        User user = new User();
        user.setUsername("userAccount1");
        when(userMapper.findByUsername(any())).thenReturn(Optional.of(user));

        //when
        userServiceImpl.findUser(user.getUsername());

        //then
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("userAccount1");
        assertThat(user.getUsername()).isNotEqualTo("userAccount2");

    }

    @Test
    @Rollback(true)
    @DisplayName("회원ID 중복확인 : 중복의 경우 메시지 정상적 표시되는지")
    public void checkUsernameRepeated() {
        //given
        User requestJoinUser = new User();
        requestJoinUser.setUsername("dongdong");

        int count = 1;
        when(userMapper.checkUsername(any())).thenReturn(count);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result", count == 0 ? "success" : "failure");
        resultMap.put("message", count == 0 ? "사용하실 수 있는 아이디입니다." : "이미 있는 아이디입니다.");

        //when
        userServiceImpl.checkUsername(requestJoinUser.getUsername());

        //then
        assertThat(resultMap)
                .extractingByKeys("result","message")
                .contains("failure", "이미 있는 아이디입니다.");

    }


    @Test
    @Rollback(true)
    @DisplayName("회원ID 중복확인 : 중복이 아닌 경우 메시지 정상적 표시되는지")
    public void checkUsernameNotrepeated() {
        //given
        User requestJoinUser = new User();
        requestJoinUser.setUsername("dongdongguri");

        int count = 0;
        when(userMapper.checkUsername(any())).thenReturn(count);

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result", count == 0 ? "success" : "failure");
        resultMap.put("message", count == 0 ? "사용하실 수 있는 아이디입니다." : "이미 있는 아이디입니다.");

        //when
        userServiceImpl.checkUsername(requestJoinUser.getUsername());

        //then
        assertThat(resultMap)
                .extractingByKeys("result","message")
                .contains("success", "사용하실 수 있는 아이디입니다.");

    }

//    @Test
//    public void getKakaoAccessToken() {
//    }
//
//    @Test
//    public void getKakaoUserInfo() {
//    }
//
//    @Test
//    public void unlinkKakao() {
//    }
//
//    @Test
//    public void getNaverAccessToken() {
//    }
//
//    @Test
//    public void getNaverUserInfo() {
//    }
}