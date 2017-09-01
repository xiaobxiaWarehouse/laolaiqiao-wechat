var defaultOpts = {
        totalPages: 20,
        visiblePages: 5,
        first: "首页",  
        prev: "上一页",  
        next: "下一页",  
        last: "未页",
        initiateStartPageClick:false
    };
function searchUser(pageIndex) {
	$.ajax('../user/search',{
    	data:{'keyword': $('#keyword').val(), 'pageIndex': pageIndex},
		dataType:'json', //服务器返回json格式数据
		success:function(result){
			//服务器返回响应
			if (result.success) {
				renderTable(result);
				var userCount = result.rowCount ? result.rowCount : 0;
				$('#userCount').text(userCount);
			} else {
				alert(result.errorMessage);
			}
		},
		error:function(xhr,type,errorThrown) {
			console.log("网络异常");
		}
	});
}

function renderTable(result) {
	var users = result.users;
	//用jquery获取模板
    var tpl =  $("#newsListTmpl").html(),
    	//预编译模板
     	template = Handlebars.compile(tpl),
     	//匹配json内容
		html = template({users: users});
    //输入模板
    $('#tableList').html(html);
    var currentPage = $('#pagination').twbsPagination('getCurrentPage');
    $('#pagination').twbsPagination('destroy');
    $('#pagination').twbsPagination($.extend({}, defaultOpts, {
        startPage: currentPage,
        totalPages: result.pageCount,
        onPageClick:function (page, pageNo) {
        	searchUser(pageNo);
        }
    }));
}

$(function() {
	$('#searchBtn').click(function(e) {
		e.preventDefault();
		searchUser(1);
	});
	searchUser(1);
});