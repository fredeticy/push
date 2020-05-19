
$(function(){
	var type = 'type';
	var reg = new RegExp("(^|&)"+ type +"=([^&]*)(&|$)"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r!=null) {
		var par = unescape(r[2]);
		getData(par);
		return ;
	}
		 
	return null; 
	
})

/*$.ajax({
	type:"GET",
	url:"/admin/user/list.do",
	success:function(data){
		$('#paginate').pagination({
			dataSource:data,
			callback:function(data,pagination){
				var content_title = setContentTitle("user");
				var thead = setthead("user");
				var tbody = settbody(data,"user");
				$('#content_title').html(content_title);
				$('#thead').html(thead);
				$('#tbody').html(tbody);
			}
		})
		
	}
})*/
$(".sidebar-menu a").click(function(){
	id = $(this).attr('id');
	getData(id);
})
function setContentTitle(id){
	let content_title = '';
	if(id == "user"){
		content_title = "用户<small>列表</small>";
	}else if(id == "role"){
		content_title = "角色<small>列表</small>";
	}else if(id == "msg"){
		content_title = "信息<small>列表</small>";
	}else if(id == "censor"){
		content_title = "未通过推送<small>列表</small>";
	}
	return content_title;
}
function setthead(id){
	
	let html = "<tr>";
	if(id == "user"){
	html += "<th>#</th>" + "<th>用户名</th>" + "<th>角色</th>" + "<th>手机号</th>" 
	+ "<th>邮箱</th>" + "<th>地址</th>" +"<th>操作</th>";
	}else if(id == "role"){
		html += "<th>#</th>" + "<th>类型</th>" + "<th>创建日期</th>" +"<th>操作</th>";
	}else if(id == "msg"){
		html += "<th>#</th>" + "<th>标题</th>" + "<th>内容</th>" + "<th>创建日期</th>" + "<th>创建人</th>" + "<th>操作</th>";
	}else if(id=="censor"){
		html += "<th>#</th>" + "<th>敏感词个数</th>" + "<th>敏感词</th>" + "<th>原文长度</th>" 
		+ "<th>创建人</th>" + "<th>创建日期</th>"; 
	}
	html += "</hr>";	
	return html;
}
function settbody(data,id){
	var html = '';
	$.each(data,function(index,value){
		html +="<tr>";
		$.each(value,function(key,val){
			
			if(val==null||val=="")
				return true;
			if(key == 'roleid'){
				if(val == '1')
					html += "<td>" + "管理员" + "</td>";
				else 
					html += "<td>" + "普通用户" + "</td>";
				return true;
			}
			html += "<td>" + val + "</td>";
		})
		var button = setButton(id,value.id);
		html += button;
		html += "</tr>";
	});
	return html;
}
function setButton(id,key){
	let html = "";
	if(id == "user"){
		html += "<td>" + "<a href='/admin/user/edit.html?id="+ key + "'class='btn btn-xs btn-warning'>编辑</a>&nbsp&nbsp" 
		+ "<button class='btn btn-xs btn-danger' onclick='deleteUser("+ key +")'>删除</button>" + "</td>";
	}else if(id == "role"){
		html += "<td>" + "<a href='/admin/role/edit.html?id=" + key + "' class='btn btn-xs btn-warning'>编辑</a>&nbsp&nbsp" 
		+ "<button class='btn btn-xs btn-danger' onclick='deleteRole("+ key +")'>删除</button>" + "</td>";
	}else if(id == "msg"){
		html+= "<td>" + "<a href='/admin/msg/edit.html?id="+ key + "'class='btn btn-xs btn-warning'>编辑</a>&nbsp&nbsp" 
		+ "<button class='btn btn-xs btn-danger' onclick='deleteMsg("+ key +")'>删除</button>" + "</td>";
	}
	return html;
}
function getData(id){
	$.ajax({
		type:"get",
		url:"/admin/" + id + "/list.do",
		success:function(data){
			if(id == 'censor')
				$('#add_data').css('display','none')
			renderData(data,id);
		}
	})
	
}
function getParam(id){
	var reg = new RegExp("(^|&)"+ id +"=([^&]*)(&|$)"); 
		var r = window.location.search.substr(1).match(reg); 
		if (r!=null) return unescape(r[2]); return null; 
}
$('#add_data').click(function(){
	let id = getParam('type');
	console.log(id);
	if(id == 'user')
		window.location.href="/admin/user/add.html";
	else if(id == 'role')
		window.location.href="/admin/role/add.html";
	else if(id == 'msg')
		window.location.href="/admin/msg/add.html";
})

function deleteUser(id){
	if(!confirm('确认要删除该用户吗?'))
		return ;
	$.ajax({
		type:'post',
		url:'/admin/user/delete.do',
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		data:{
			'id':id
		},
		success:function(data){
			alert('删除成功');
			window.location.href ="/admin.html?type=user";
		},
		error:function(data){
			alert('删除失败');
		}
	})
}

function deleteRole(id){
	if(!confirm('确认要删除该角色吗?'))
		return ;
	$.ajax({
		type:'post',
		url:'/admin/role/delete.do',
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		data:{
			'id':id
		},
		success:function(data){
			alert('删除成功');
			window.location.href ="/admin.html?type=role";
		},
		error:function(data){
			alert('删除失败');
		}
	})
}

function deleteMsg(id){
	if(!confirm('确认要删除该消息吗?'))
		return ;
	$.ajax({
		type:'post',
		url:'/admin/msg/delete.do',
		contentType: 'application/x-www-form-urlencoded;charset=utf-8',
		data:{
			'id':id
		},
		success:function(data){
			alert('删除成功');
			window.location.href ="/admin.html?type=msg";
		},
		error:function(data){
			alert('删除失败');
		}
	})
}
$(function(){
	type = getParam('type');
	if(type == 'user')
		$('#search').attr('placeholder','用户名');
	else if(type == 'role')
		$('#search').attr('placeholder','类型');
	else if(type == 'msg')
		$('#search').attr('placeholder','标题');
	else if(type == 'censor')
		$('#search').attr('placeholder','创建人id');
})
$('#searchButton').click(function(){
	type = getParam('type');
	var url = '';
	let val = $('#search').val();
	if(val == '')
		getData(type);
	var pdata ={};
	if(type == 'user'){			
		url = "/admin/user/search.do";
		pdata = {'userid':val};
	}
	else if(type == 'role'){
		url = "/admin/role/search.do";
		pdata = {'type':val};
	}
	else if(type == 'msg'){
		url = "/admin/msg/search.do";
		pdata = {'title':val};
	}else if(type == 'censor'){
		url = "/admin/censor/search.do";
		pdata = {'createrid':val};
	}	
	if(val == "")
		return ;
	$.ajax({
		type:'post',
		contentType:'application/x-www-form-urlencoded;charset=utf-8',
		url:url,
		data:pdata,
		success:function(data){
			if(data == ''||data==null){
				$('#tbody').html("");
				return ;
			}
			let id = type;
			renderData(data,id);
		},
		error:function(data){
			
		}
	})
})
function renderData(data,id){
	$('#paginate').pagination({
		dataSource:data,
		callback:function(data,pagination){
			console.log(data)
			var thead = setthead(id);
			var tbody = settbody(data,id);
			var content_title = setContentTitle(id);
			$('#content_title').html(content_title);
			$('#thead').html(thead);
			$('#tbody').html(tbody);
		}
	})
}
$('#search').focus(function(){
	$(document).keydown(function(){
	 if(event.keyCode == 13){
		    $('#searchButton').click();
		}
 })
})