
 $(document).keydown(function(){
	 if(event.keyCode == 13){
		    login();
		}
 })
 
function login(){
	if (window.XMLHttpRequest) 
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	     xmlhttp=new XMLHttpRequest(); 
	  } 
	else {// code for IE6, IE5 
	   xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
	} 
	xmlhttp.open("post", "login.do", true);
	userid=document.getElementById("userid").value;
	pwd=document.getElementById("pwd").value;
	if(userid == ""||userid == null){
		alert('用户名不能为空');
		return ;
	}
	if(pwd == "" || pwd == null){
		alert('密码不能为空');
		return ;
	}
		
	 xmlhttp.onreadystatechange=function() 
	 {    
		 
		 if (xmlhttp.readyState==4 && xmlhttp.status==200) 
	     { 
			 
			 json = eval("("+xmlhttp.responseText+")");
			 if("success" != json.msg)
				 {
				 document.getElementById("pwd").value="";
				 }
			else {
				if(json.roleid == "1")
					window.location.href = "admin.html";
				else if(json.roleid == "2"){
					window.location.href = "user.html";
				}
			}
				 
		 }
	     } 	
	 
	 xmlhttp.setRequestHeader("Content-type",
	 "application/x-www-form-urlencoded"); 

	 
xmlhttp.send("userid="+userid+"&pwd="+pwd);
}