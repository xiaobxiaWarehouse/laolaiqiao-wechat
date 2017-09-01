var ajaxSubmit = function(url, data, callback) {
	// AJAX请求提交表单
	$.ajax(url, {
		data: data,
		dataType:'json', //服务器返回json格式数据
		type:'post', //HTTP请求类型
		success: callback,
		error:function(xhr,type,errorThrown) {
			alert('网络异常');
			$.unblockUI();
		}
	});
}

var handleHelper = Handlebars.registerHelper("addOne",function(index){
	//返回+1之后的结果
     return index+1;
});