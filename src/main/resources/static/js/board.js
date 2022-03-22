
var board = {
	init: function(){
		$(document).on('submit', 'form#boardCreateForm', function(e) {
			e.preventDefault();
			e.stopPropagation();
			board.save();	
		});
		$(document).on('submit', 'form#boardUpdateForm', function(e) {
			e.preventDefault();
			board.edit()
		});
		$(document).on('click', '#btn-delete', function(e) {
			e.preventDefault();
			e.stopPropagation();
			board.deleteById();	
		});
		$(document).on('click', '[data-back]', function(e) { 
			board.historyBack();
		});
		$(document).on('click', '[data-add-file]', function(e) {
			e.preventDefault();
			e.stopPropagation();
			board.addFile();	
		});
		$(document).on('click', '[data-remove-file]', function(e) {
			e.preventDefault();
			board.removeFile($(this));	
		});
		$(document).on('click', '[data-delete-file]', function(e) {
			e.preventDefault();
			var $this = $(this),
				$deleteFileId = $this.parent().find('input[name="deleteFileId[]"]'),
				newAttachId = $this.data('delete-file')
			;
			$deleteFileId.val(newAttachId);
			board.deleteAttach($this);
		});
	},
	
	
	addFile: function(){
		var fileHtml = '\t\t\t<div data-attach-file="attachFile" class="form-group filebox">\n' +
		 	'\t\t\t\t<lable for="attach"></lable>\n' +
		 	'\t\t\t\t<div class="col-lg-10">\n' +
		 	'\t\t\t\t\t<input type="file" name="files" id="attach" multiple>\n' +
		 	'\t\t\t\t\t<!-- 파일 삭제 버튼 -->\n' +
		 	'\t\t\t\t\t<button type="submit" name="removeFile" data-remove-file="removeFile">\n' +
		 	'\t\t\t\t\t\t<i class="fa fa-minus"></i>' +
		 	'\t\t\t\t\t</button>\n' +
		 	'\t\t\t\t</div>\n' +
		 	'\t\t\t</div>';
		$('#btn-save').before(fileHtml);
		$('#btn-edit').before(fileHtml);
	},
	
	
	removeFile: function(btnRemove){
		var beforeTag = btnRemove.prev().prop('tagName');
		
		if(beforeTag === 'BUTTON'){
			var firstFile = btnRemove.prevAll('input[type="file"]');
			firstFile.val('');
			return;
		}
		var otherFile = btnRemove.closest('[data-attach-file]');
		otherFile.remove();
	},
	
	
	historyBack: function() {
		history.back();
	},

	
	save: function(){
		var saveFormData = new FormData($("#boardCreateForm")[0]);
		var createFiles = $("#boardCreateForm").find('input[name="files[]"]')[0].files;
		for (var i = 0; i < createFiles.length; i++) {
			saveFormData.append("files", createFiles[i]);
		}
		
		if(saveFormData.title == ""){
			alert("제목을 입력해주세요.");
			return;
		}
		
		if(saveFormData.content == ""){
			alert("내용을 입력해주세요.");
			return;
		}
		
		$.ajax({
			type : "POST",
			url : "/api/board",
			data : saveFormData,
			processData: false,
			contentType: false,
		}).done(function(resp){
			alert(resp.message);
			if(resp.messageType !== "success"){
				console.log('resp', resp);	
				return;
			}
				console.log('resp', resp);	
				location.href = "/board";
		}).fail(function(error){ 
			alert(JSON.stringify(error));
		}); 
		
	},
	
	
	edit: function (){ 
		var updateFormData = new FormData($("#boardUpdateForm")[0]);
		var updateFiles = $("#boardUpdateForm").find('input[name="files[]"]')[0].files;
		for (var i = 0; i < updateFiles.length; i++) {
			updateFormData.append("files", updateFiles[i]);
		}
/*
		if (updateFormData.get('title') == '') {
			alert("제목을 입력해주세요.");
			return;
		}

		if (updateFormData.get('content') == '') {
			alert("내용을 입력해주세요.");
			return;
		}
*/
			$.ajax({
				type : "POST",
				url : "/api/board/"+updateFormData.get('id'),
				data : updateFormData,
				dataType : "json",
				processData: false,
				contentType: false,
			}).done(function(resp){
				alert(resp.message);
				if(resp.messageType !== "success"){
					console.log('resp', resp);	
					return;
				}
					console.log('resp', resp);	
					location.href = "/board";
			}).fail(function(error){
				alert(JSON.stringify(error));
				reject();
			});
	},
	
	
	deleteAttach: function(){
		var fileId = $('input[name="deleteFileId[]"]').val();
		var boardId = $('#id').val();
		console.log('fileId : ' +fileId);
		$.ajax({
			type: "POST",
			url: "/attach/delete/" +fileId, 
			processData: false,
			contentType: false,
		}).done(function(resp){
				if(resp.messageType !== "success"){
					console.log('resp', resp);	
					alert("첨부파일 삭제에 실패했습니다.");
					return;
				}
				console.log('resp', resp);	
				alert("첨부파일 삭제가 완료되었습니다.");
				location.href = "/board/" + boardId + "/update";
		 }).fail(function(error){
				alert(JSON.stringify(error));
		});
	},
	
	
	deleteById: function(){
		let id = $("#id").text();
 
 		if(!confirm("삭제하시면 복구할 수 없습니다. \n 삭제하시겠습니까?")){
			return;
		}
		
		$.ajax({
			type : "DELETE",
			url : "/api/board/"+id,
			dataType : "json"
		}).done(function(resp){
			if(resp.messageType !== "success"){
				console.log('resp', resp);	
				alert('게시글 삭제에 실패했습니다.');
				return;
			}
				alert('게시글을 삭제했습니다.');
				location.href = "/board";
		}).fail(function(error){ 
			alert(JSON.stringify(error));
		});
	},
}

board.init();
