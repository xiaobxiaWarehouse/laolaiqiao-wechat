var cityDate = {},
	countryData = {},
	countryData = {},
	streetPicker = null,
	countryPicker = null,
	cityPicker = null,
	provincePicker = null,
	mask = mui.createMask();//callback为用户点击蒙版时自动执行的回调；
(function($) {
	$('.mui-btn').click(function(e) {
		e.preventDefault();
		if (!isValid()) {
			return;
		}
		saveUser($('form'));
	});
	
	$('#turnToHobby').click(function() {
		var openId = $('#openId').val();
		window.location.href = "../user/changeUserHobby?openId=" + openId;
	});
	
	mui.init();
	Picker.initDatePicker();
	initPicker();
	
	//普通示例
	var userPicker = new mui.PopPicker();
	userPicker.setData([{
		value: '男',
		text: '男'
	}, {
		value: '女',
		text: '女'
	}]);
	var showSexPickerButton = document.getElementById('sex');
	showSexPickerButton.addEventListener('tap', function(event) {
		var value = $('#sex').val();
		if (value) {
			userPicker.pickers[0].setSelectedValue(value);
		}
		userPicker.show(function(items) {
			$('#sex').val(items[0].value);
		});
	}, false);
})(jQuery);


function isValid() {
	if (!$('#userName').val()) {
		mui.alert("请输入您的姓名");
		return false;
	}
	
	var mobile = $('#mobile').val();
	if (!mobile) {
		mui.alert("请输入手机号码");
		return false;
	}
	
	var reg = /^1[0-9]{10}$/;
	if (!reg.test(mobile)) {
		mui.alert("请输入正确的手机号码");
		return false;
	}
	return true;
}



function initPicker() {
	streetPicker = new mui.PopPicker(),
	countryPicker = new mui.PopPicker(),
	cityPicker = new mui.PopPicker(),
	provincePicker = new mui.PopPicker();
	
	var userId = $('#userId').val();
	// 省
	provincePicker.setData(cityData3);
	if (!userId) {
		$('#provinceHidden').val(cityData3[0].value);
		$('#province').val(cityData3[0].text);
	}
	Picker.createPicker('province', provincePicker);
	
	// 市
	Picker.createPicker('city', cityPicker);
	Picker.getCityData(userId);
	
	// 县
	Picker.createPicker('country', countryPicker);
	Picker.getCountryData(userId);
	
	// 街道
	Picker.createPicker('street', streetPicker);
	Picker.getStreetData(userId);
}

