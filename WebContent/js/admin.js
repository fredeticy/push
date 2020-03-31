function setTbody(){
	
}

function ajaxGetList(id){
	if(id == "user"){
		
	}else if(id == "role"){
		
	}else if(id == "msg"){
		
	}
	
}
/*function ajaxGetUserList(){
	$.ajax({
		type:"get",
		url:"/admin/user/list",
		dataType:"json",
		data:{
			pageNum:
			pageSize:
		},
		success: function(data){
			
		},
		error:function(data){
			
		}
	
	})
}*/
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

	$.ajax({
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
	})
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
	}
	html += "</hr>";	
	$('#thead').html(html);
}
function settbody(data,id){
	var html = '';
	$.each(data,function(index,value){
		html +="<tr>"
		$.each(value,function(key,val){
			
			if(val==null||val=="")
				return true;
			html += "<td>" + val + "</td>";
		})
		var button = setButton(id,value.id);
		html += button;
		html += "</tr>"
	});
	html +="</tr>"
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
	})
	
}

