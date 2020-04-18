$(document).keydown(function(){
		 if(event.keyCode == 13){
			    login();
			}
	 })

 
function login(){
	userid=$("#userid").val();
	pwd=$('#pwd').val();
	if(userid == ""||userid == null){
		alert('用户名不能为空');
		return ;
	}
	if(pwd == "" || pwd == null){
		alert('密码不能为空');
		return ;
	}
	$.ajax({
		type:'post',
		data:{
			'userid':userid,
			'pwd':pwd
		},
		timeout:4000,
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		url:'login.do',
		success:function(data){
			for(var i=0;i<10;i++)
				{
				
				console.log(i);
				}
				
			if("success" != data.msg){
				alert(data.msg);
				$("#pwd").val("");
			}
			else {
			if(data.roleid == "1")
				window.location.href = "/admin.html?type=user";
			else if(data.roleid == "2")
				window.location.href = "/user/push_history.html";
			}
		},
		error:function(data){
			for(var i=100;i>90;i--)
			{
			
			console.log(i);
			}
			alert('登录错误');
		},
	})	
}