var mask = mui.createMask();
$(function() {
	$('.mui-badge').click(function() {
		$(this).toggleClass('mui-badge-selected');
	});
})

/**
 * 提交表单，保存用户信息
 */
function saveUser($form) {
	
	if ($('#hobby').length && !validateHobby()) {
		return;
	}
	mask.show();//显示遮罩
	// AJAX请求提交表单
	$.ajax('../user/saveUser',{
		data: $form.serializeArray(),
		dataType:'json', //服务器返回json格式数据
		type:'post', //HTTP请求类型
		success:function(result){
			//服务器返回响应，根据响应结果，分析是否登录成功；
			if (result.success) {
				mui.alert("保存成功");
				// 跳转到用户详情
				window.location.href = "../user/getUserInfo?openId=" + result.user.openId;  
			} else {
				mui.alert(result.errorMessage);
				mask.close();//关闭遮罩
			}
			
		},
		error:function(xhr,type,errorThrown){
			console.log("网络异常");
			mask.close();//关闭遮罩
		}
	});
}

function validateHobby() {
	if ($('.mui-badge-selected').length == 0) {
		mui.alert("请选择兴趣爱好");
		return false;
	}
	// 提交前，先设置设置兴趣爱好
	var hobbies = '';
	
	$('.mui-badge-selected').each(function(index) {
		hobbies += $(this).text() + ',';
	});
	$('#hobby').val(hobbies.substring(0, (hobbies.length - 1)));
	
	return true;
}