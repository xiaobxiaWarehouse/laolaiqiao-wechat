var _CONST_TITLE_NEW = '新增一级菜单',
	_CONST_TITLE_UPDATE = '修改一级菜单';
$(function() {
	// 新增按钮
	$('#addBtn').click(function() {
		getMenuData('', _menuTypeClick,'','请选择一条图文消息', 'click');
		$('#myModalLabel').text(_CONST_TITLE_NEW);
	});
	
	$('.js-modify').click(function() {
		var $tr = $(this).closest('tr'),
			menuName = $tr.find('td[name]').attr('name'),
			type = $tr.find('td[type]').attr('type'),
			typeKey = $tr.find('td[type]').attr('typeKey'),
			url = $tr.find('td[url]').attr('url'),
			item = $tr.find('td[item]').attr('item');
		getMenuData(menuName, type, url, item ? item : '请选择一条图文消息', typeKey);
		$('#myModalLabel').text(_CONST_TITLE_UPDATE);
	});
	
	$('.js-delete').click(function(e) {
		e.preventDefault();
		deleteMenu($(this));
	});
	
	// 点击是否跳转外部链接
	$('#myModal').on('click', '#chkThirdParty', function(e) {
		$(this).find('input[type="checkbox"]').click();
	});
	$('#myModal').on('click', '#chkThirdParty > input[type="checkbox"]', function(e) {
		e.stopPropagation();
		thirdPartyClick();
	});
	
	// 保存
	$('.js-save').click(function(e) {
		e.preventDefault();
		saveMenu();
	});
});


function getMenuData(menuName, type, url, item, typeKey) {
	// 取得数据
	$.ajax('../menu/getMenuData',{
		dataType:'json', //服务器返回json格式数据
		async:'false',
		success:function(result){
			//服务器返回响应，根据响应结果，分析是否登录成功；
			if (result.success) {
				renderData(menuName, result.newsItems, type, url, item, typeKey);
				// 跳转到用户详情
				$('#myModal').modal('show');
			} else {
				alert(result.errorMessage);
			}
			
		},
		error:function(xhr,type,errorThrown) {
			console.log("网络异常");
		}
	});
}

function renderData(menuName, newsItems, type, url, item, typeKey) {
	//用jquery获取模板
    var tpl   =  $("#tpl").html(),
    	//预编译模板
     	template = Handlebars.compile(tpl);
    // 
    if (typeKey == 'view' && newsItems) {
    	$.each(newsItems, function(index, newsItem) {
    		if (url == newsItem.url) {
    			item = newsItem.title;
    			$('#chkThirdParty > input[type="checkbox"]').prop('checked', false);
    			return false;
    		}
    	})
    }
    
     	//匹配json内容
	var html = template({ 
	    	menuName: menuName, 
	    	newsItems: newsItems, 
	    	type:type, 
	    	url:url, 
	    	item:item, 
	    	typeKey:typeKey
	    	});
    //输入模板
    $('#menuForm').html(html);
	
	initDropDown("dropdownType");
	initDropDown("dropdownNews");
	renderByMenuType(type);
}

function initDropDown(name) {
	var $dropdown = $('#' + name);
	$dropdown.dropdown();
	
	$('ul[aria-labelledby="' + name + '"] > li').click(function() {
		var $tag = $(this).find('a'),
			typeText = $tag.text();
		$dropdown.find('.js-text').text(typeText);
		$('input[hiddenType="' + name + '"]').val($tag.attr("value"));
		
		if (name == 'dropdownType') {
			renderByMenuType(typeText);
		}
	});
}

function renderByMenuType(type) {
	// 点击事件
	if (type == _menuTypeClick) {
		$('.js-view').addClass('hidden');
		return;
	}
	$('.js-view').removeClass('hidden');
	// 跳转页面
	thirdPartyClick();
}

function thirdPartyClick() {
	var isChk = $('#chkThirdParty > input[type="checkbox"]').is(':checked');// 是否外部链接
	if (isChk) {
		$('#newItemArea').addClass('hidden');// 图文区
		$('#urlArea').removeClass('hidden');// 外部链接输入框
	} else {
		$('#newItemArea').removeClass('hidden');// 图文区
		$('#urlArea').addClass('hidden');// 外部链接输入框
	}
}

function deleteMenu($this) {
	if(confirm('确认要删除这个菜单吗？')) {
        var $tr = $this.closest('tr'),
			menuName = $tr.find('td[name]').attr('name');
        // 删除
        ajaxSubmit('../menu/deleteTopMenu', {'menuName': menuName}, function(result) {
        	if (result.success) {
        		alert("删除成功");
        		location.reload();
        	} else {
        		alert(result.errorMessage);
        	}
        });
    };
}

function saveMenu() {
	if (!isValid()) {
		return false;
	}
	
	var url = '../menu/updateTopMenu';
	if ($('#myModalLabel').text() == _CONST_TITLE_NEW) {
		url = '../menu/createTopMenu';
	}
	
	// 外链
	var isChk = $('input[type="checkbox"]', $('#chkThirdParty')).is(':checked');// 是否外部链接
	if ($('input[name="type"]').val() == 'view' && isChk) {
		$('input[name="url"]').val($('#url').val());
	}
	
	// 保存
	$.blockUI({ message: '保存中...' });
    ajaxSubmit(url, $('#menuForm').serializeArray(), function(result) {
    	if (result.success) {
			$('#myModal').modal('hide');
			location.reload();
    	} else {
    		alert(result.errorMessage);
    		$.unblockUI();
    	}
    });
}

function isValid() {
	var name = $('#name', $('#menuForm')).val();
	if (!name) {
		alert('菜单名称不能为空');
		return false;
	}
	
	var type = $('input[name="type"]').val();
	if (type == 'click') {
		return true;
	}
	var isChk = $('input[type="checkbox"]', $('#chkThirdParty')).is(':checked');// 是否外部链接
	// 外链
	if (isChk && !$('#url').val()) {
		alert('请填写外链地址');
		return false;
	}
	// 图文
	if (!isChk && !$('input[name="url"]').val()) {
		alert('请选择一个图文消息');
		return false;
	}
	return true;
}


