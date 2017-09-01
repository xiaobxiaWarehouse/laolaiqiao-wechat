var cityDate = {},
	countryData = {},
	countryData = {},
	streetPicker = null,
	countryPicker = null,
	cityPicker = null,
	provincePicker = null;
(function($, _) {
	$('.mui-btn').click(function(e) {
		e.preventDefault();
		var step = $(this).attr('step');
		if (step == 1) {
			if (!validateStep1()) {
				return;
			}
			switchClass('.reg-step1', '.reg-step2', 'mui-hidden');
			$('#backgroundImage').addClass('mui-hidden');
			$(this).attr('step', 2);
		} else if (step == 2) {
			if ($('.mui-badge-selected').length == 0) {
				_.alert("请选择兴趣爱好");
				return;
			}
			switchClass('.reg-step2', '.reg-step3', 'mui-hidden');
			$(this).attr('step', 3).text("保存");
			$('#backgroundImage').addClass('mui-hidden');
			initPicker();
		} else if (step == 3) {
			saveUser($('form'));
		}
	});
	
	_.init();
	Picker.initDatePicker();
	
})(jQuery, mui);

/**
 * 转换Class
 * @param class1 需要加上class的对象
 * @param class2 需要去掉class的对象
 * @param className class名
 */
function switchClass(class1, class2, className) {
	$(class1).addClass(className);
	$(class2).removeClass(className);
}

function validateStep1() {
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
	
	// 省
	provincePicker.setData(cityData3);
	$('#provinceHidden').val(cityData3[0].value);
	$('#province').val(cityData3[0].text);
	Picker.createPicker('province', provincePicker);
	
	// 市
	Picker.createPicker('city', cityPicker);
	Picker.getCityData();
	
	// 县
	Picker.createPicker('country', countryPicker);
	Picker.getCountryData();
	
	// 街道
	Picker.createPicker('street', streetPicker);
	Picker.getStreetData();
}


