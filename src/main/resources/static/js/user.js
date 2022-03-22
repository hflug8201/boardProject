
let index = {
	init: function(){
		$("#joinUsername").blur(() => {
			this.check();
		});
		$("#btn-save").on("click" , ()=> { 
			this.save();
		});
	},

	check: function(){
		var username = $('#joinUsername').val();
		var username_check = $("#username_check");
		
		$.ajax({
			// 회원가입 시 아이디 중복 체크
			type : "GET",
			url : "/auth/checkUsername?username=" + username,
			data : JSON.stringify(username), 
			contentType : "application/json; charset=utf-8"
		}).done(function(resp){
				username_check.text(resp.message);			
				if(resp.messageType == 'success') {
					$("#username_check").css("color", "blue");
					$("#btn-save").attr("disabled", false);
				}
				if(resp.messageType == 'failure'){
					$("#username_check").css("color", "red");
					$("#btn-save").attr("disabled", true);
				}
			}).fail(function(error){
					console.log("실패");
			})
		},
	
	save: function(){ 
		let data = {
				username: $("#joinUsername").val(),
				password: $("#password").val(),
				email: $("#email").val(),
		}
		
		$.ajax({
			// 회원가입 수행 요청
			type : "POST",
			url : "/auth/joinProc", 
			data : JSON.stringify(data),
			contentType : "application/json; charset=utf-8" ,
			dataType : "json"
		}).done(function(resp){
			alert(resp.message);	
			if(resp.messageType == "success") {
				location.href = "/";				
			}
		}).fail(function(error){ 
				alert(JSON.stringify(error));
		});
	}
}

index.init();