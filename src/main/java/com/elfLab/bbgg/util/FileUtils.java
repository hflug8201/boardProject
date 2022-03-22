package com.elfLab.bbgg.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.elfLab.bbgg.model.Attach;

@Component
public class FileUtils {
	
	// 업로드 경로
	private final String uploadPath = Paths.get("C:", "Temp").toString();
	
	/*
	 * 서버에 저장할 파일명 처리
	 * @return 랜덤 문자열
	 */ 
	private final String getRandomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/*
	 * 서버에 첨부파일 생성하고 업로드 파일 목록 리턴
	 * @param files - 업로드 할 파일의 정보가 담겨 있음
	 * @param boardId - 게시글 번호
	 * @return 업로드 파일 목록
	*/
	public List<Attach> uploadFiles(MultipartFile[] files, int boardId){
		List<Attach> attachList = new ArrayList<>();
		
		File dir = new File(uploadPath);
		if(dir.exists() == false) {
			dir.mkdirs();
		}
		
		for(MultipartFile file : files) {
			if(files[0].getSize() < 1) {
				 continue;
			}
			try {
				// 파일 확장자
				final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
				
				// 서버에 저장할 파일명(랜덤 문자열 + 확장자)
				final String saveName = getRandomString() + "." + extension;

				File targetFile = new File(uploadPath, saveName);
				file.transferTo(targetFile);
				
				// 파일 정보 저장
				Attach attach = new Attach();
				attach.setBoardId(boardId);
				attach.setOriginName(file.getOriginalFilename());
				attach.setSaveName(saveName);
				attach.setSize(file.getSize());
				
				// 파일 정보 추가
				attachList.add(attach);
				
			} catch(IOException e){
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return attachList;
	}

}
