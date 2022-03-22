package com.elfLab.bbgg.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.elfLab.bbgg.mapper.AttachMapper;
import com.elfLab.bbgg.mapper.BoardMapper;
import com.elfLab.bbgg.mapper.CommentMapper;

@ExtendWith(MockitoExtension.class)
@Transactional
class BoardServiceImplTest {

	@Mock
    private BoardMapper boardMapper; // 의존하고 있는 mapper를 @Mock으로 선언
	
	@Mock
    private AttachMapper attachMapper;
	
	@Mock
	private CommentMapper commentMapper;
	
	@InjectMocks
	private BoardServiceImpl boardServiceImpl; // @Mock으로 선언된 가짜 객체들을 의존한 Service 객체 생성
	
	@Test
	void test() {
		
	}

}
