package com.elfLab.bbgg.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.elfLab.bbgg.model.Board;

@ExtendWith(MockitoExtension.class)
//@MybatisTest
@AutoConfigureTestDatabase
class BoardMapperTest {

	@Autowired
	private BoardMapper boardMapper;
	
//	public Board setup() {
//		Board board = new Board();
//		board.setId(1);
//		board.setUserId("아이디");
//		board.setTitle("제목");
//		board.setContent("내용");
//		boardMapper.createBoard(board);
//		return board;
//	}
	
	@Test
	@DisplayName("게시글 상세 조회 확인")
	void mapperTest() {
		Board board = new Board();
		board.setId(1);
		board.setUserId("아이디");
		board.setTitle("제목");
		board.setContent("내용");
		boardMapper.createBoard(board);
		Board findBoard = boardMapper.getBoardById(board.getId());
		assertThat(board).isEqualTo(findBoard);

	}

}
