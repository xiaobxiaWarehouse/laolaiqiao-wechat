var _CONST_TITLE_NEW = '新增子菜单',
	_CONST_TITLE_UPDATE = '修改子菜单';
$(function() {
	// 新增按钮
	$('#addBtn').click(function() {
		getMenuData('','','请选择一条图文消息', '');
		$('#myModalLabel').text(_CONST_TITLE_NEW);
	});
	
	$('.js-modify').click(function() {
		var $tr = $(this).closest('tr'),
			menuName = $tr.find('td[name]').attr('name'),
			parentName = $tr.find('td[parentName]').attr('parentName'),
			url = $tr.find('td[url]').attr('url'),
			item = $tr.find('td[item]').attr('item');
		getMenuData(menuName, url, item ? item : '请选择一条图文消息', parentName);
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


function getMenuData(menuName, url, item, parentName) {
	// 取得数据
	$.ajax('../menu/getTopClickMenu',{
		dataType:'json', //服务器返回json格式数据
		async:'false',
		success:function(result){
			//服务器返回响应，根据响应结果，分析是否登录成功；
			if (result.success) {
				renderData(result.menuButtons, result.newsItems, menuName, url, item, parentName);
				// 跳转到菜单详情
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

/**
 * 
 * @param topMenus 父菜单列表
 * @param newsItems 图文消息列表
 * @param menuName 菜单名称
 * @param url URL
 * @param item 选中的图文
 * @param parentName 选中的父菜单名称
 */
function renderData(topMenus, newsItems, menuName, url, item, parentName) {
	//用jquery获取模板
    var tpl   =  $("#tpl").html(),
    	//预编译模板
     	template = Handlebars.compile(tpl),
     	isChecked = true;
    // 
    if (newsItems) {
    	$.each(newsItems, function(index, newsItem) {
    		if (url == newsItem.url) {
    			item = newsItem.title;
    			isChecked = false;
    			return false;
    		}
    	});
    }
    // 默认选中第一个父级菜单
    if (!parentName && topMenus) {
    	parentName = topMenus[0].name;
    }
    
    //匹配json内容
	var html = template({ 
	    	menuName: menuName, 
	    	newsItems: newsItems, 
	    	topMenus:topMenus,
	    	url:url, 
	    	item:item, 
	    	parentName:parentName
	    	});
    //输入模板
    $('#menuForm').html(html);
	
	initDropDown("dropdownParent");
	initDropDown("dropdownNews");
	if (url && isChecked) {
		$('input[type="checkbox"]').click();
	}
	
}

function initDropDown(name) {
	var $dropdown = $('#' + name);
	$dropdown.dropdown();
	
	$('ul[aria-labelledby="' + name + '"] > li').click(function() {
		var $tag = $(this).find('a'),
			typeText = $tag.text();
		$dropdown.find('.js-text').text(typeText);
		$('input[hiddenType="' + name + '"]').val($tag.attr("value"));
	});
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
        $.blockUI({ message: '删除中...' });
        ajaxSubmit('../menu/deleteSubMenu', {'menuName': menuName}, function(result) {
        	if (result.success) {
        		alert("删除成功");
        		location.reload();
        	} else {
        		alert(result.errorMessage);
        		$.unblockUI();
        	}
        });
    };
}

function saveMenu() {
	if (!isValid()) {
		return false;
	}
	
	var url = '../menu/updateSubMenu';
	if ($('#myModalLabel').text() == _CONST_TITLE_NEW) {
		url = '../menu/createSubMenu';
	}
	
	// 外链
	var isChk = $('input[type="checkbox"]', $('#chkThirdParty')).is(':checked');// 是否外部链接
	if (isChk) {
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
