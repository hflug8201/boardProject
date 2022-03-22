<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form id="boardCreateForm" name="boardCreateForm" enctype="multipart/form-data" >
		<div class="form-group">
			<label for="title">제목</label>
			<input type="text" class="form-control" id="title" name="title" placeholder="제목을 입력해주세요.">
		</div>

		<div class="form-group">
			<label for="content">내용</label>
			<textarea class="form-control" rows="5" id="content" name="content" placeholder="내용을 입력해주세요."></textarea>
		</div>
		
		<div data-attach-file="attachFile" class="form-group filebox">
			<lable for="attach">파일 추가</lable>
			<div class="col-lg-10">
				<input type="file" name="files[]" id="attach" multiple>
				
				<!-- 파일 추가 버튼 -->
				<button type="submit" name="addFile" data-add-file="addFile">
					<i class="fa fa-plus"></i>
				</button>
				
				<!-- 파일 삭제 버튼 -->
				<button type="submit" name="removeFile" data-remove-file="removeFile">
					<i class="fa fa-minus"></i>
				</button>
			</div>
		</div>
		<button type="submit" id="btn-save" class="btn btn-primary">게시글 작성 완료</button>
	</form>
	<br/><br/><br/>
</div>
<br/><br/>

<script src="/js/board.js"></script>

<%@ include file="../layout/footer.jsp"%> 

