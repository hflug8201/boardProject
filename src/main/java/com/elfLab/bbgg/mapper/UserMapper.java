package com.elfLab.bbgg.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.elfLab.bbgg.model.User;

@Mapper
public interface UserMapper {

	//로그인
	int join(User user);

	//회원 찾기
	Optional<User> findByUsername(String username);

	// 유저 아이디 중복 체크
	int checkUsername(String username);

}
 