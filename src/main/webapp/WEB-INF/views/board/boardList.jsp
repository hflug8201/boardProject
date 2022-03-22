<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="../layout/header.jsp"%>

<div class="container">
	<select name="listSize" id="listSize" onchange="fn_pageSize(1,1)">
		<option value="10" <c:if test="${pagination.getListSize() == 10 }">selected="selected"</c:if> >10개씩 보기</option>
		<option value="20" <c:if test="${pagination.getListSize() == 20 }">selected="selected"</c:if> >20개씩 보기</option>
		<option value="30" <c:if test="${pagination.getListSize() == 30 }">selected="selected"</c:if> >30개씩 보기</option>
	</select>
	
 	<table class="table table-hover">
  		<thead class="thead-dark">
		<tr>
        	<th class="text-center">글 번호</th>
        	<th class="text-center">제목</th>
        	<th class="text-center">조회수</th>
        	<th class="text-center">작성일</th>
        	<th class="text-center">글쓴이</th>
      	</tr>
		</thead>
   
		<tbody>
	 		<c:forEach var="board" items="${boards}">
      			<tr>
	   				<td class="text-center">${board.id}</td>
	   				<td class="text-center"><a href="/board/${board.id}" class="btn">${board.title}</a></td>
	    			<td class="text-center">${board.viewHits}</td>
	    			<td class="text-center">${board.createdDate}</td>
	    			<td class="text-center">${board.userId}</td>
	 			</tr>
	 		</c:forEach>
		</tbody>
	</table>
  
	<!-- pagination{s} -->
	<div id="paginationBox">
		<ul class="pagination">
			<c:if test="${pagination.prev}">
				<li class="page-item">
					<a class="page-link" href="#" onClick="fn_prev('${pagination.page}', '${pagination.range}', '${pagination.rangeSize}')">Previous</a>
				</li>
			</c:if>
				
			<c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="idx">
				<li class="page-item <c:out value="${pagination.page == idx ? 'active' : ''}"/> ">
					<a class="page-link" href="#" onClick="fn_pagination('${idx}', '${pagination.range}', '${pagination.rangeSize}')"> ${idx} </a>
				</li>
			</c:forEach>
				
			<c:if test="${pagination.next}">
				<li class="page-item">
					<a class="page-link" href="#" onClick="fn_next('${pagination.range}', '${pagination.range}', '${pagination.rangeSize}')" >Next</a>
				</li>
			</c:if>
		</ul>
	</div>
	<!-- pagination{e} -->
	
	<!-- search{s} -->
	<div class="form-group row justify-content-center">
		<div class="w100" style="padding-right:10px">
			<select class="form-control form-control-sm" name="searchType" id="searchType">
				<option value="title">제목</option>
				<option value="content">본문</option>
				<option value="user_id">작성자</option>
			</select>
		</div>
		<div class="w300" style="padding-right:10px">
			<input type="text" class="form-control form-control-sm" name="keyword" id="keyword">
		</div>
		<div>
			<button class="btn btn-sm btn-primary" name="btnSearch" id="btnSearch">검색</button>
		</div>
	</div>
	<!-- search{e} -->
</div>


<script src="/js/page.js"></script>

<%@ include file="../layout/footer.jsp"%> 