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
public class Board {
	
	private int id; // key값, 글번호 
	
	private String title; // 제목 
	
	private String content; // 내용 
	
	private int viewHits; // 조회수 
	
	private Timestamp createdDate; //작성일
	
	private String userId;	// 작성자

	private String[] files;

}


