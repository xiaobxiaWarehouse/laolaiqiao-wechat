$(function() {
	$('#searchBtn').click(function(e) {
		e.preventDefault();
		if (!isValid()) {
			return;
		}
		
	    $.ajax('../news/getNewsByName',{
	    	data:{'keyword': $('#keyword').val()},
			dataType:'json', //服务器返回json格式数据
			success:function(result){
				//服务器返回响应，根据响应结果，分析是否登录成功；
				if (result.success) {
					renderTable(result.newsItems)
				} else {
					alert(result.errorMessage);
				}
				
			},
			error:function(xhr,type,errorThrown) {
				console.log("网络异常");
			}
		});
	});
});

function isValid() {
	var keyword = $('#keyword').val();
	if (!keyword) {
		alert('请输入名称');
		return false;
	}
	return true;
}

function renderTable(newsItems) {
	//用jquery获取模板
    var tpl =  $("#newsListTmpl").html(),
    	//预编译模板
     	template = Handlebars.compile(tpl),
     	//匹配json内容
		html = template({newsItems: newsItems});
    //输入模板
    $('#tableList').html(html);
}