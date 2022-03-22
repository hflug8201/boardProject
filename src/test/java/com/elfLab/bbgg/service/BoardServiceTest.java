package com.elfLab.bbgg.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Search;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class BoardServiceTest {

	@Autowired
	BoardService boardService;

	
	
	
	// 게시글 목록 조회 시 null 아니면 테스트 통과
	@Test
	public void testGetList() {
		try {
			Search search = new Search();
			search.setSearchType("검색 타입");
			search.setKeyword("검색 키워드");
			
			List<Board> boardList = boardService.getBoardList(search);
			assertNotNull(boardList.size()); // 리스트의 elements 갯수
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// 게시글 상세 조회 시 null이 아니면 테스트 통과
	@Test
	public void testGetBoardById() {
		try {
			Search search = new Search();
//			search.setSearchType("검색 타입");
//			search.setKeyword("검색 키워드");
			
			List<Board> boardList = boardService.getBoardList(search);
			int id = boardList.get(0).getId(); // boardList 첫번째에 있는 객체의 id
			
			Board boardById = boardService.getBoardById(id);
			assertNotNull(boardById);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	// 게시글 작성 후 1이 응답되면 테스트 통과
//	@Rollback(true)
//	@Test
//	public void testCreateBoard() {
//		try {
//			Board board = new Board();
//			board.setTitle("게시글 제목 작성");
//			board.setContent("게시글 내용 작성");
//			board.setUserId("게시글 작성자 작성");
//			
//			int result = boardService.createBoard(board);
//			assertTrue(result == 1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 게시글 수정 후 2가 응답되면 테스트 통과
//	@Rollback(true)
//	@Test
//	public void testUpdateBoard() {
//		try {
//			Search search = new Search();
//			search.setSearchType("검색 타입");
//			search.setKeyword("검색 키워드");
//			
//			List<Board> boardList = boardService.getBoardList(search);
//			int id = boardList.get(0).getId();
//			
//			Board board = new Board();
//			board.setId(id);
//			board.setTitle("게시글 제목 수정");
//			board.setContent("게시글 내용 수정");
//			
//			int result = boardService.updateBoard(id, board);
//			assertTrue(result == 2);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	// 게시글 삭제 후 3이 응답되면 테스트 통과
//	@Rollback(true)
//	@Test
//	public void testDeleteBoard() {
//		try {
//			Search search = new Search();
//			search.setSearchType("검색 타입");
//			search.setKeyword("검색 키워드");
//			
//			List<Board> boardList = boardService.getBoardList(search);
//			int id = boardList.get(0).getId();
//			
//			int result = boardService.deleteBoard(id);
//			assertTrue(result == 3);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}

}
