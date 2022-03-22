package com.elfLab.bbgg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {

	private int listSize; //초기값으로 목록개수를 10으로 셋팅
	
	private int rangeSize = 10; //초기값으로 페이지범위를 10으로 셋팅
	
	private int page; // 현재 목록 페이지 번호
	
	private int range;	//  각 페이지 범위 시작 번호
	
	private int listCnt; // 총 게시물 개수 
	
	private int pageCnt; // 전체 페이지 범위 개수 
	
	private int startPage;
	
	private int startList;
	
	private int endPage;
	
	private boolean prev;
	
	private boolean next;

	public void pageInfo(int page, int range, int listCnt, int listSize) {
		this.page = page;
		this.range = range;
		this.listCnt = listCnt;
		this.listSize = listSize;
		
		//전체 페이지수 
		this.pageCnt = (int) Math.ceil((double)listCnt/listSize);
		
		//시작 페이지
		this.startPage = (range - 1) * rangeSize + 1 ;
		
		//끝 페이지
		this.endPage = range * rangeSize;
				
		//게시판 시작번호
		this.startList = (page - 1) * listSize;
		
		//이전 버튼 상태
		this.prev = range != 1;
		
		//다음 버튼 상태
		this.next = pageCnt > endPage;
		if (this.endPage > this.pageCnt) {
			this.endPage = this.pageCnt;
			this.next = false;
		}
	}
}
