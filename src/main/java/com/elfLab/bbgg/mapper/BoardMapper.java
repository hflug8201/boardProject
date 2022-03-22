package com.elfLab.bbgg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Search;

@Mapper
public interface BoardMapper {
	
	
	
	// 게시글 목록 조회 : 리스트 형태
	public List<Board> getBoardList(Search search) throws Exception;

	// 게시글 전체 수 조회
	public int getTotalListCnt(Search search) throws Exception;
	
	// 게시글 상세 조회
	public Board getBoardById(int id);
	
	// 게시글 조회수 증가
	public void increaseHit(int id);
	
	// 게시글 작성
	public int createBoard(Board board);
	
	// 게시글 수정
	public int updateBoard(Board board);
	
	// 게시글 삭제
	public int deleteBoard(Board board);


	
	
}
 