<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<button class="btn btn-secondary" data-back="btnBack">뒤로 가기</button>
	<c:if test="${board.userId eq username}">
		<a href="/board/${board.id}/update" class="btn btn-warning">수정</a>
		<button id="btn-delete" class="btn btn-danger">삭제</button>
	</c:if>
	<br /><br />
	<div>
		글 번호 : <span id="id"><i>${board.id}</i></span>
	</div>
	<br />
	<div>
		<h3>${board.title}</h3>
	</div>
	<hr />
	 <div>
		<div>${board.content}</div>
	 </div>
	<hr />
</div>
<br /><br />

<div class="container">
	<c:if test="${!empty fileList}" >
		<label for="inp-type-4" class="col-lg-2 control-label">File</label>
		<div class="col-lg-10">
			<div class="file_list">
				<ul>
					<c:forEach var="file" items="${fileList}">
						<li>
							<a class="download-link" href="/board/download.do/${file.id}">
								<i class="fa fa-file-o"></i>
								${file.originName}
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</c:if>
</div>

<!-- 댓글 작성 -->
<div class="container">
	<form id="commentForm" name="commentForm" method="post">
		<br><br>
		<div class="form-group">
			<div>
				<span><b>댓글</b></span>
			</div>
			<div>
				<table class="table">
					<tr>
						<td>
							<textarea class="form-control" rows="3" cols="30" id="content" name="content" placeholder="댓글을 입력하세요"></textarea>
							<br>
							<div>
								<button type="button" id="comment-save" class="btn pull-right btn-success">댓글 등록</button>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<input type="hidden" id="boardId" name="boardId" value="${board.id}" />
	</form>

	<!-- 댓글 리스트 -->
	<div class="container">
		<c:forEach var="comment" items="${comments}">
			<form id="commentListForm" name="commentListForm" method="post">
				<div class="row">	
			 		<div class="col-lg-6 contentInput">
			 			<c:if test="${comment.commentDepth > 1}"><div class="fab fa-replyd"></div></c:if>
			 			${comment.content}
						<button type="button" id="recomment-openclose" data-open-close="openCloseRecomment" class="btn btn-outline-success btn-sm">댓글 달기</button>
						
						<!-- 대댓글 달기 -->
						<div data-recomment="recomment" class="recomment" style="display: none">
							<div class="col-lg-12" style="padding:5px">
								<input type="hidden" id="commentDepth" name="commentDepth" value="${comment.commentDepth}">
								<textarea class="w-100" rows="3" cols="30" id="content" name="content" placeholder="댓글을 입력하세요"></textarea>
							</div>
							<div class="col-lg-3">
								<input type="hidden" name="_method" value="post"/>
								<button type="button" id="recomment-save" data-save-recomment="saveRecomment" class="btn btn-success btn-sm">댓글 등록</button>
							</div>
						</div>
					</div>
					<div class="col-lg-2">${comment.createdDate}</div>
					<div class="col-lg-2">${comment.userId}</div>
					<c:if test="${comment.userId eq username}">
						<div class="col-lg-1 editInput">
							<button type="button" id="openEditBtn" data-open-edit-btn="${comment.id}" class="openEditBtn btn btn-outline-success btn-sm">댓글 수정</button>
						</div>
						<div class="col-lg-1">
							<button type="button" id="comment-delete" class="btn btn-outline-success btn-sm">댓글 삭제</button>
						</div>
					</c:if>
					
					<!-- 댓글 수정 -->
					<div class="row editComment" id="editComment" data-edit-recomment="${comment.id}" style="display: none">
						<input type="hidden" id="commentId" value="${comment.id}"/>
						<div class="form-group col-lg-12">
							<textarea class="form-control summernote" rows="5" id="editContent" name="editContent" placeholder="댓글을 입력하세요">${comment.content}</textarea>
						</div>
						<button id= "comment-edit" type="button" class="btn btn-success">수정 완료</button>
					</div>

					<input type="hidden" id="boardId" name="boardId" value="${board.id}" />
					<input type="hidden" id="commentId" name="commentId" value="${comment.id}" />
				</div>
			</form>
		</c:forEach>
	</div>
</div>

<script src="/js/board.js"></script>
<script src="/js/comment.js"></script>
<div id="footer"></div>
<%@ include file="../layout/footer.jsp"%>