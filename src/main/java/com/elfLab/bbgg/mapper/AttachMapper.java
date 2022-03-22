package com.elfLab.bbgg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.elfLab.bbgg.model.Attach;

@Mapper
public interface AttachMapper {

	// 파일 정보 저장
	public int insertAttach(Attach attach);
	
	// 파일 상세 정보 조회
	public Attach getAttachDetail(int boardid);
	
	// 파일 삭제
	public int deleteAttach(int id);
	
	// 게시글의 파일 목록 조회
	public List<Attach> getAttachListByBoardId(int boardId);
	
}
