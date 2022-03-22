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
public class Comment {

	private int id;

	private int boardId;

	private int groupNo;

	private int commentOrd;

	private int commentDepth;

	private String content;
	
	private Timestamp createdDate;
	
	private String userId;
}
