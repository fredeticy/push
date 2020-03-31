    var arr = [];
    
    for (var i=0;i<100;i++){
    	var person={name:"Bill",age:i,title:null};
    	
        arr.push(person)
    }
    console.log(arr);

    $('#wrapper').pagination({
        dataSource: arr,
    callback: function(data, pagination) { //回调函数 当我们点击页数执行的方法
    	console.log(data,pagination)
		var html = renderdata(data);
    	$('#box').html(html);
    }
    })
    function renderdata(data){
	var html="";
	$.each(data,function(index,value){
		html += "<tr>"
		$.each(value,function(key,val){
			html +="<td>"+val+"</td>";
		})
		html += "</tr>"
	})
	return html;
}
