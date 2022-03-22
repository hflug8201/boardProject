
var comment = {
	init: function(){		
		$(document).on('click', '#comment-save', function(e) {
			e.preventDefault();
			comment.saveComment();	
		});
		
		$(document).on('click', '#comment-edit', function(e) {
			e.preventDefault();
			e.stopPropagation();
			comment.edit($(this));
		});
		
		$(document).on('click', '[data-open-edit-btn]', function(e) {
			e.preventDefault();
			comment.openEditBtn($(this).data('open-edit-btn'));
		});
		
		$(document).on('click', '#comment-delete', function(e) {
			e.preventDefault();
			comment.deleteById($(this));
		});
		
		$(document).on('click', '[data-open-close]', function(e) {
			e.preventDefault();
			e.stopPropagation();
			comment.openClose($(this));		
		});
		
		$(document).on('click', '[data-save-recomment]', function(e) {
			e.preventDefault();
			comment.saveRecomment($(this));
		});
	},

	openClose: function(btnObject){ 
		var $parent = btnObject.closest('.contentInput');
		var $recomment = $parent.find('[data-recomment]');
		$recomment.is(':visible') ? $recomment.hide() : $recomment.show();
	},
	

	saveComment: function(){
		var data = $("#commentForm").serializeObject();
		
		if(data.content == ""){
			alert("댓글을 입력해주세요.");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "/board/comment",
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8" ,
			dataType : "json" 
		}).done(function(resp){
				alert(resp.message);
				if(resp.messageType !== "success"){
					console.log('resp', resp);	
					return;
				}
				console.log('resp', resp);
				location.href = "/board/"+data.boardId;
		}).fail(function(error){
				alert(JSON.stringify(error));
		});	
	},
	
	
	saveRecomment: function(btnRecomment){
		var $form = btnRecomment.closest('form');
		var data = $form.serializeObject();
		delete data.editContent;
		
		if(data.content == ""){
			alert("댓글을 입력해주세요.");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "/board/recomment/"+data.commentId,
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8" ,
			dataType : "json" 
		}).done(function(resp){
				alert(resp.message);
				if(resp.messageType !== "success"){
					console.log('resp', resp);	
					return;
				}
				console.log('resp', resp);
				location.href = "/board/"+data.boardId;
		}).fail(function(error){
				alert(JSON.stringify(error));
		});
	},
	
	
	openEditBtn: function(commentId){
		var $editRecomment = $('[data-edit-recomment="'+commentId+'"]');
		$editRecomment.is(':visible') ? $editRecomment.hide() : $editRecomment.show();
	},
	
	
	edit: function(btnEdit){ 
		var $form2 = btnEdit.closest('form');
		var data = $form2.serializeObject();
		
		delete data.content;
		data.content = data.editContent;
		delete data.editContent;

		if(data.content == ""){
			alert("댓글을 입력해주세요.");
			return;
		}

		$.ajax({
			type : "PUT",
			url : '/board/'+data.boardId+ '/comment/' + data.commentId,
			data : JSON.stringify(data), 
			contentType : "application/json; charset=utf-8" ,
			dataType : "json" 
		}).done(function(resp) {
				alert(resp.message);
				if(resp.messageType !== "success"){
					console.log('resp', resp);
					return;
				}
				console.log('resp', resp);	
				location.href = "/board/"+data.boardId;
		}).fail(function(error){
				alert(JSON.stringify(error));
		});
	},
	
	
	deleteById: function(btnDelete){
		
		var $form2 = btnDelete.closest('form');
		var data = $form2.serializeObject(); 
 		console.log(data);
 		
 		if(!confirm("삭제하시면 복구할 수 없습니다. \n 삭제하시겠습니까?")){
			return;
		}
		
		$.ajax({
				type : "DELETE",
				url : '/board/'+data.boardId+ "/comment/" + data.commentId,
				dataType : "json"
		}).done(function(resp){
				if(resp.messageType !== "success"){
					console.log('resp', resp);	
					alert("댓글 삭제에 실패했습니다.");
					return;
				}
				alert("댓글이 삭제되었습니다.");
				location.href = "/board/"+data.boardId;
		}).fail(function(error){ 
				alert(JSON.stringify(error));
		}); 
	},
}

comment.init();
