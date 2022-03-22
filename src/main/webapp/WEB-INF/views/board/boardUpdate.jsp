<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form id="boardUpdateForm" name="boardUpdateForm" enctype="multipart/form-data" method="post">
		<input type="hidden" id="id" name="id" value="${board.id}"/>
		<div class="form-group">
			<label for="title">제목</label>
			<input value="${board.title}" type="text" class="form-control" placeholder="Enter Title" id="title" name="title">
		</div>

		<div class="form-group">
			<label for="content">내용</label>
			<textarea class="form-control summernote" rows="5" id="content" name="content">${board.content}</textarea>
		</div>

		<div class="form-group">
			<c:if test="${!empty fileList}" >
				<label for="inp-type-4" class="col-lg-2 control-label">File</label>
				<div class="col-lg-10">
					<div class="form-group file_list">
						<c:forEach var="file" items="${fileList}">
							<li>
								<div data-attached-file="attachedFile" class="form-group filebox">
									<a class="download-link" href="/board/download.do/${file.id}">
										<input type="hidden" name="deleteFileId[]" value="">
										<i class="fa fa-file-o"></i>
										${file.originName}
										<!-- 파일 삭제 버튼 -->
										<button type="submit" name="deleteFile" data-delete-file="${file.id}">
											<i class="fa fa-minus"></i>
										</button>
									</a>
								</div>
							</li>
						</c:forEach>
					</div>
				</div>
			</c:if>
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
		<button type="submit" id="btn-edit" class="btn btn-primary">수정 완료</button>
	</form>
	<br/>
</div>
<br/><br/>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%> 