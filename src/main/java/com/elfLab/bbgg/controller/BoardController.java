package com.elfLab.bbgg.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.elfLab.bbgg.dto.ResponseDto;
import com.elfLab.bbgg.model.Attach;
import com.elfLab.bbgg.model.Board;
import com.elfLab.bbgg.model.Comment;
import com.elfLab.bbgg.model.Pagination;
import com.elfLab.bbgg.model.Search;
import com.elfLab.bbgg.model.User;
import com.elfLab.bbgg.service.BoardService;

@Controller
public class BoardController {
 
	
	@Autowired 
	private BoardService boardServiceImpl;
	

	// 메인 페이지 조회 
	@GetMapping({"", "/"})
	public String main() {
		return "main";
	}
	

	// 게시글 목록 조회 페이지 접속
	@GetMapping({"board", "board?page={page}&range={range}&listSize={listSize}", "board?searchType={searchType}&keyword={keyword}", "board?page={page}&range={range}&listSize={listSize}&searchType={searchType}&keyword={keyword}"})
	public String getBoardList(Model model
			, @RequestParam(value = "page", required = false, defaultValue = "1") int page
			, @RequestParam(value = "range", required = false, defaultValue = "1") int range
			, @RequestParam(value = "listSize", defaultValue = "10") int listSize
			, @RequestParam(value = "searchType", required = false, defaultValue = "title") String searchType
			, @RequestParam(value = "keyword", required = false) String keyword
			) throws Exception {
		
		Search search = new Search();
		search.setSearchType(searchType);
		search.setKeyword(keyword);
		
		// 전체 게시글 개수
		int listCnt = boardServiceImpl.getTotalListCnt(search);
		
		search.pageInfo(page, range, listCnt, listSize);
				
		model.addAttribute("pagination", search);
		model.addAttribute("boards", boardServiceImpl.getBoardList(search));
		
		return "board/boardList";
	}


	// 게시글 상세 조회
	@GetMapping("/board/{boardId}")
	public String boardDetail(Model model, @PathVariable("boardId") int boardId) {	
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = (UserDetails)principal;

		model.addAttribute("username", userDetails.getUsername());
		model.addAttribute("board", boardServiceImpl.getBoardById(boardId));
		
		List<Attach> fileList = boardServiceImpl.getAttachListByBoardId(boardId);
		model.addAttribute("fileList", fileList);
		
		model.addAttribute("comments", boardServiceImpl.getCommentListByBoardId(boardId));
		
		return "board/boardDetail";
	}
	
	
	// 게시글 작성 페이지 접속
	@GetMapping("/board/create") 
	public String boardCreate() {
		return "board/boardCreate";
	}
	
	
	// 게시글 수정 페이지 접속
	@GetMapping("/board/{id}/update") 
	public String boardUpdate(Model model, @PathVariable int id) {
		model.addAttribute("board", boardServiceImpl.getBoardById(id));
		model.addAttribute("fileList", boardServiceImpl.getAttachListByBoardId(id));
		
		return "board/boardUpdate";
	}
	
	
	// 게시글 작성 + 파일 업로드
	@ResponseBody
	@PostMapping("/api/board")
	public ResponseDto createBoardWithFiles(@RequestParam Map<String, Object> params, MultipartFile[] files) {
		Board board = new Board();
		board.setTitle((String) params.get("title"));
		board.setContent((String) params.get("content"));
		
		if(files != null && files.length != 0) {
			boardServiceImpl.uploadFiles(board.getId(), files);
		}
		
		return ResponseDto.sendData(boardServiceImpl.createBoard(board));
	}


	// TODO file 갯수 체크 통해서 파일 업로드 체크하는 로직 정상 동작하는지 확인
	// 게시글 수정 실행 + 파일업로드
	@ResponseBody
	@PostMapping("/api/board/{id}")
	public ResponseDto updateBoard(@PathVariable("id") int id, @RequestParam Map<String, Object> params, MultipartFile[] files) throws Exception {
		Board board = new Board();
		board.setId(id);
		board.setTitle((String) params.get("title"));
		board.setContent((String) params.get("content"));

//		int fileCnt = 0;
		if(files != null && files.length != 0) {
			//fileCnt += boardServiceImpl.uploadFiles(board.getId(), files);
			boardServiceImpl.uploadFiles(board.getId(), files);
		}

//		System.out.println(fileCnt);
//		if(files.length+1 != fileCnt){
//			return ResponseDto.sendError("파일 업로드에 실패했습니다.");
//		}

		return ResponseDto.sendData(boardServiceImpl.updateBoard(id, board));
	}
	
	
	// 게시글 삭제
	@ResponseBody
	@DeleteMapping("/api/board/{id}") 
	public ResponseDto deleteBoard(@PathVariable("id") int id) throws Exception{
		int result = boardServiceImpl.deleteBoard(id);
		
		return ResponseDto.sendData(result);
	}
	

	// 모댓글 작성
	@ResponseBody
	@PostMapping("board/comment")
	public ResponseDto createComment(@RequestBody Comment comment) {	
		return ResponseDto.sendData(boardServiceImpl.createComment(comment));
	}
	
	
	// 대댓글 작성
	@ResponseBody
	@PostMapping("board/recomment/{id}")
	public ResponseDto createRecomment(@PathVariable("id") int id, @RequestBody Comment comment) {
		return ResponseDto.sendData(boardServiceImpl.createRecomment(id, comment));
	}
		
		
	// 댓글 수정 실행
	@ResponseBody
	@PutMapping("board/{boardId}/comment/{id}") 
	public ResponseDto updateComment(@PathVariable("boardId") int boardId, @PathVariable("id") int id, @RequestBody Comment comment) throws Exception {
		return ResponseDto.sendData(boardServiceImpl.updateComment(id, comment));
	}
		
		
	// 댓글 삭제
	@ResponseBody
	@DeleteMapping("board/{boardId}/comment/{id}") 
	public ResponseDto deleteComment(@PathVariable("boardId") int boardId, @PathVariable("id") int id) throws Exception{
		int result = boardServiceImpl.deleteComment(id);

		return ResponseDto.sendData(result);
	}
	
	
	// 첨부파일 다운로드
	@GetMapping("/board/download.do/{id}")
	public void downloadAttachFile(@PathVariable(required = false)int id, Model model, HttpServletResponse response) {

//		if (id == null) throw new RuntimeException("올바르지 않은 접근입니다.");

		Attach fileInfo = boardServiceImpl.getAttachDetail(id);

		String uploadPath = Paths.get("C:", "Temp").toString();

		String filename = fileInfo.getOriginName();
		File file = new File(uploadPath, fileInfo.getSaveName());

		try {
			byte[] data = FileUtils.readFileToByteArray(file);
			response.setContentType("application/octet-stream");
			response.setContentLength(data.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(filename, "UTF-8") + "\";");

			response.getOutputStream().write(data); // 다운로드 시작 
			response.getOutputStream().flush(); // 파일 다운로드 완료
			response.getOutputStream().close(); // 버퍼를 정리하고 닫아준다.

		} catch (IOException e) {
			throw new RuntimeException("파일 다운로드에 실패하였습니다.");

		} catch (Exception e) {
			throw new RuntimeException("시스템에 문제가 발생하였습니다.");
		}
	}
	
	
	// 첨부파일 삭제
	@ResponseBody
	@PostMapping("/attach/delete/{fileId}")
	public ResponseDto deleteAttach(@PathVariable("fileId") int id) throws Exception{
		int result = boardServiceImpl.deleteAttach(id);
		return ResponseDto.sendData(result);
	}

}
