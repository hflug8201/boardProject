package com.elfLab.bbgg.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.elfLab.bbgg.model.Attach;
import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Comment;
import com.elfLab.bbgg.model.Pagination;
import com.elfLab.bbgg.model.Search;

public interface BoardService {
	
	
	//게시글 목록 조회
	public List<Board> getBoardList(Search search) throws Exception;
	
	//전체 게시글 수 조회
	public int getTotalListCnt(Search search) throws Exception;
	
	// 게시글 상세 조회
	public Board getBoardById(int id); // throws Exception;
	
	// 게시글 작성
	public Map<String, String> createBoard(Board board);
	
	// 파일 업로드
	public int uploadFiles(int boardId, MultipartFile[] files);
	
	// 게시글 수정
	public Map<String, String> updateBoard(int id, Board board);
	
	// 게시글 삭제
	public int deleteBoard(int id);
	
	// 해당 게시글 댓글 리스트 조회 
	public List<Comment> getCommentListByBoardId(int boardId);
	
	// 댓글 정보 가져오기
	public Comment getCommentById(int id);
	
	// 모댓글 작성
	public Map<String, String> createComment(Comment requestComment);
	
	// 대댓글 작성
	public Map<String, String> createRecomment(int id, Comment requestComment);

	// 댓글 수정
	public Map<String, String> updateComment(int id, Comment comment);

	// 댓글 삭제
	public int deleteComment(int id);
	
	// 첨부파일 상세 정보 조회
	public Attach getAttachDetail(int id);
	
	// 첨부파일 리스트 조회
	public List<Attach> getAttachListByBoardId(int boardId);
	
	// 첨부파일 삭제
	public int deleteAttach(int fileId);
	
	
	
}
