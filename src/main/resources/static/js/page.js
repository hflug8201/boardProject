	
function getListSize() {
	return $("#listSize option:selected").val();
}
	
var currentListSize = $("#listSize option:selected").val();
	
$("#listSize").change(function(){
	currentListSize = $(this).find("option:selected").val();
});

// n개씩 보기 버튼
function fn_pageSize(page,range){
  	var listSize = getListSize();
  	var url = "/board";
	url = url + "?page=" + page;
	url = url + "&range=" + range;
	url = url + "&listSize=" + (listSize == 10 || listSize == 20 || listSize == 30 ? listSize : 10);
	location.href = url;
};

//이전 버튼 이벤트
function fn_prev(page, range, rangeSize) {
	var page = ((range - 2) * rangeSize) + 1;
	var range = range - 1;
		
	var url = "/board";
	url = url + "?page=" + page;
	url = url + "&range=" + range;
	url = url + "&listSize=" + getListSize();
		
	location.href = url;
}

//페이지 번호 클릭
function fn_pagination(page, range, rangeSize, searchType, keyword) {
	var url = "/board";
	url = url + "?page=" + page;
	url = url + "&range=" + range;
	url = url + "&listSize=" + getListSize();

	location.href = url;	
}

//다음 버튼 이벤트
function fn_next(page, range, rangeSize) {
	var page = parseInt((range * rangeSize)) + 1;
	var range = parseInt(range) + 1;
	
	var url = "/board" 
	       + "?page=" + page
 		   + "?listSize=" + getListSize();
	location.href = url;
}


// 검색 기능 
$(document).on('click', '#btnSearch', function(e){
	e.preventDefault();
			
	var qsList = [];	
	qsList.push("searchType=" + $('#searchType').val());
	qsList.push("keyword=" + $('#keyword').val());
	qsList.push("range=" + getListSize());
		
	var url = "/board?"+qsList.join("&");
	
	location.href = url;
	console.log(url);
});	