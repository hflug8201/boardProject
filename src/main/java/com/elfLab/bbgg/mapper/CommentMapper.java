package com.elfLab.bbgg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Comment;

@Mapper
public interface CommentMapper {

	// 댓글 리스트 조회
	public List<Comment> getCommentListByBoardId(int boardId);
	
	// 모댓글 작성
	public int createComment(Comment comment);
	
	// 첫댓글인지 체크 : 해당 board_id 가진 데이터 갯수 리턴
	public int checkFirstComment(int boardId);
	
	//대댓글 작성
	public int createRecomment(Comment Comment);
	
	//모댓글 정보 가져오기
	public Comment getCommentById(int id);
	
	// 댓글 수정
	public int updateComment(Comment comment);
	
	// 댓글 삭제
	public int deleteComment(int id);
	
	// 댓글 정보
	public Comment getAboutComment(int id);
	
	// 하위 댓글 순서 미루기 
	public int updateCommentBelow(int groupNo, int commentOrd);
	

}
