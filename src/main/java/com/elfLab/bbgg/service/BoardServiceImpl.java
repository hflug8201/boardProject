package com.elfLab.bbgg.service;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.elfLab.bbgg.mapper.AttachMapper;
import com.elfLab.bbgg.mapper.BoardMapper;
import com.elfLab.bbgg.mapper.CommentMapper;
import com.elfLab.bbgg.model.Attach;
import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Comment;
import com.elfLab.bbgg.model.Pagination;
import com.elfLab.bbgg.model.Search;
import com.elfLab.bbgg.util.CommonUtils;
import com.elfLab.bbgg.util.FileUtils;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private AttachMapper attachMapper;

	@Autowired
	private FileUtils fileUtils;

	@Transactional
	@Override
	public List<Board> getBoardList(Search search) throws Exception {
		return boardMapper.getBoardList(search);
	}

	@Transactional
	@Override
	public int getTotalListCnt(Search search) throws Exception {
		return boardMapper.getTotalListCnt(search);
	}


	// 게시글 상세 조회
	@Transactional
	@Override
	public Board getBoardById(int id){
		boardMapper.increaseHit(id);
		return boardMapper.getBoardById(id);
	}


	// 게시글 작성
	@Transactional
	@Override
	public Map<String, String> createBoard(Board board) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = (UserDetails)principal;

		board.setUserId(userDetails.getUsername());

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "게시글 작성이 완료되었습니다." );
		
		if(CommonUtils.isNotEmpty(board.getTitle()) == false || CommonUtils.isNotEmpty(board.getContent()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "게시글 제목 또는 내용을 입력해주세요");
			return resultMap;
		}
		
		boardMapper.createBoard(board);
		return resultMap;
	}


	// 파일 업로드
	@Transactional
	@Override
	public int uploadFiles(int boardId, MultipartFile[] files) {
		List<Attach> fileList = fileUtils.uploadFiles(files, boardId);
		int resultCnt = 0;
		if(CollectionUtils.isEmpty(fileList) == false) {
			for (Attach attach : fileList) {
				resultCnt += attachMapper.insertAttach(attach);
			}
		}
		return resultCnt;
	}


	// 게시글 수정
	@Transactional
	@Override
	public Map<String, String> updateBoard(int id, Board requestBoard){
		Board board = boardMapper.getBoardById(id);
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "게시글 수정이 완료되었습니다");
		
		if(CommonUtils.isNotEmpty(board.getTitle()) == false || CommonUtils.isNotEmpty(board.getContent()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "게시글 제목 또는 내용을 입력해주세요");
			return resultMap;
		}
		boardMapper.updateBoard(board);
		return resultMap;
	}


	// 게시글 삭제
	@Transactional
	@Override
	public int deleteBoard(int id){
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = (UserDetails)principal;

		Board board = new Board();
		board.setId(id);
		board.setUserId(userDetails.getUsername());

		return boardMapper.deleteBoard(board);
	}


	// 댓글 리스트 조회
	@Transactional
	@Override
	public List<Comment> getCommentListByBoardId(int boardId) {
		return commentMapper.getCommentListByBoardId(boardId);
	}


	// 댓글 정보 가져오기
	@Transactional
	@Override
	public Comment getCommentById(int id) {
		return commentMapper.getCommentById(id);
	}


	// 모댓글 작성
	@Transactional
	@Override
	public Map<String, String> createComment(Comment requestComment) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = (UserDetails)principal;

		requestComment.setUserId(userDetails.getUsername());
		requestComment.setCommentDepth(1);
		requestComment.setCommentOrd(1);

		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "댓글 작성이 완료되었습니다");
		
		if(CommonUtils.isNotEmpty(requestComment.getContent()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "댓글을 입력해주세요");
			return resultMap;
		}
		
		int boardId = requestComment.getBoardId();

		if (commentMapper.checkFirstComment(boardId) == 0){  // 1. 해당 board_id 가진 데이터 = 0이면 첫 댓글.
				requestComment.setGroupNo(1);
		 } else {		// 2. 기존 댓글 있으면, groupNo은 기존 댓글 개수 +1
			 	requestComment.setGroupNo(commentMapper.checkFirstComment(boardId)+1);
		 };
		 
		commentMapper.createComment(requestComment);
		return resultMap;
	};
	

	// 대댓글 작성
	@Transactional
	@Override
	public Map<String, String> createRecomment(int id, Comment requestComment) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = (UserDetails)principal;

		requestComment.setUserId(userDetails.getUsername());
		
		//모댓글 정보 호출
		Comment parentComment = commentMapper.getCommentById(id);
		
		requestComment.setBoardId(parentComment.getBoardId());		
		requestComment.setGroupNo(parentComment.getGroupNo());
		requestComment.setCommentOrd(parentComment.getCommentOrd() + 1);
		requestComment.setCommentDepth(parentComment.getCommentDepth() + 1);

		//하위 댓글 순서 미루기
		commentMapper.updateCommentBelow(parentComment.getGroupNo(), parentComment.getCommentOrd());
		
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "댓글 작성이 완료되었습니다");
		
		if(CommonUtils.isNotEmpty(requestComment.getContent()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "댓글을 입력해주세요");
			return resultMap;
		}
		
		commentMapper.createComment(requestComment);
		return resultMap;
	};

	
	// 댓글 수정
	@Transactional
	@Override
	public Map<String, String> updateComment(int id, Comment requestComment) {
		Comment comment = commentMapper.getCommentById(id);
		comment.setContent(requestComment.getContent());
		
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("messageType", "success");
		resultMap.put("message", "댓글 수정이 완료되었습니다");
		
		if(CommonUtils.isNotEmpty(requestComment.getContent()) == false) {
			resultMap.put("messageType", "failure");
			resultMap.put("message", "댓글을 입력해주세요");
			return resultMap;
		}
		
		commentMapper.updateComment(comment);
		return resultMap;
	};


	// 댓글 삭제
	@Transactional
	@Override
	public int deleteComment(int id) {
		return commentMapper.deleteComment(id);
	};
	
	
	// 첨부파일 상세 정보 조회
	@Transactional
	@Override
	public Attach getAttachDetail(int id) {
		return attachMapper.getAttachDetail(id);
	};

	
	// 첨부파일리스트 조회
	@Transactional
	@Override
	public List<Attach> getAttachListByBoardId(int boardId) {
		return attachMapper.getAttachListByBoardId(boardId);
	};
	
	// 첨부파일 삭제
	@Transactional
	@Override
	public int deleteAttach(int fileId) {
		return attachMapper.deleteAttach(fileId);
	}
	
}
