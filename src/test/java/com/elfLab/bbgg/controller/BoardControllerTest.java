package com.elfLab.bbgg.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Comment;
import com.elfLab.bbgg.service.BoardServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(BoardController.class) // API만 테스트
class BoardControllerTest {

	@Autowired
	private MockMvc mockMvc; // 의존해야하지만 등록되지 않은 Bean 사용하기 위해 Mockito 객체 주입받기
	
	@MockBean
	private BoardServiceImpl boardServiceImpl; // 가짜 Bean 등록
	
	@Test
	@DisplayName("파일 업로드 요청 확인")
	void uploadFiles() throws Exception{
		
		// given
		MockMultipartFile file = new MockMultipartFile("image", "test.jpg", "image/jpg", new FileInputStream(Paths.get("C:", "Temp").toString())); // MultipartFile 인터페이스를 상속받아 모의 구현
		
		// when
		ResultActions resultActions = mockMvc.perform(
				multipart("/api/board")
				.file(file))
				.andDo(print());
		
		// then
		resultActions
			.andExpect(status().isOk())
		    .andDo(print());
	}
	
	@Test
	@DisplayName("게시글 등록 확인")
	void createBoard() throws Exception{
		// given
		Board board = new Board();
		board.setTitle("제목");
		board.setContent("내용");
		board.setUserId("아이디");

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "게시글 작성이 완료되었습니다." );

		when(boardServiceImpl.createBoard(board)).thenReturn(resultMap);
		
		ObjectMapper objectMapper = new ObjectMapper(); // java 객체 => JSON
        String json = objectMapper.writeValueAsString(board);
        
		// when
		ResultActions resultActions = mockMvc.perform(
				post("/api/board")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
		
		// then
		resultActions
			.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andDo(print());
		
	}
	
	@Test
	@DisplayName("모댓글 작성 확인")
	void createComment() throws Exception{
		// given
		Comment comment = new Comment();
		comment.setBoardId(1);
		comment.setContent("댓글 내용");

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "게시글 작성이 완료되었습니다." );
		
		when(boardServiceImpl.createComment(comment)).thenReturn(resultMap);
		ObjectMapper objectMapper = new ObjectMapper(); // java 객체 => JSON
        String json = objectMapper.writeValueAsString(comment);
		
		// when
        ResultActions resultActions = mockMvc.perform(
				post("board/comment")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
		
		// then
		resultActions
			.andExpect(status().isOk())
        	.andExpect(content().contentType(MediaType.APPLICATION_JSON))
        	.andDo(print());
	}
	

}
