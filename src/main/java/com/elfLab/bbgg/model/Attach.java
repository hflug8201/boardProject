package com.elfLab.bbgg.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attach {

	private int id; // 파일번호
	
	private int boardId; // 게시글 번호
	
	private String originName; // 원본 파일명
	
	private String saveName; // 저장 파일명
	
	private Long size; // 파일 크기
	
	private Timestamp createdDate; // 등록일
}
