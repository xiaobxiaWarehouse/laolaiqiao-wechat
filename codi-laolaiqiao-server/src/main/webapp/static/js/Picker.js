var Picker = (function(){
	/**
	 * 创建省市县选择器
	 * @param prop
	 * @param picker
	 */
	var createPicker = function (prop, picker) {
		var showCityPickerButton = document.getElementById(prop);
		// 用户点击控件
		showCityPickerButton.addEventListener('tap', function(event) {
			// 关闭键盘
			var value = $('#' + prop + 'Hidden').val();
			// 设置选中的值
			if (value) {
				picker.pickers[0].setSelectedValue(value);
			}
			// 打开选择面板
			picker.show(function(items) {
				var $prop = $('#' + prop),
					$propHidden = $('#' + prop + 'Hidden'),
					oldValue = $propHidden.val(),
					value = items[0].value;
				// 如果选择的值跟上次一样
				if (oldValue == value) {
					return true;
				}
				// 选择的值变了，下级默认选择第一个
				$prop.val(items[0].text);
				$propHidden.val(value);
				if (prop == 'street') {
					return true;
				}
				// 省份变了，重新取得市数据
				if (prop == 'province') {
					getCityData();
				}
				// 省，市数据变了，重新取得县数据
				if (prop != 'country') {
					getCountryData();
				}
				// 县数据变了，重新取得镇/街道数据
				if (prop != 'street') {
					getStreetData();
				}
			});
		}, false);
	}

	/**
	 * 市级数据
	 */
	var getCityData = function(userId) {
		var provinceCode = $('#provinceHidden').val();
		if (!provinceCode) {
			mui.alert('请先选择省');
		}
		$.each(cityData3, function(n, item){
			if (item.value == provinceCode) {
				cityData = item.children;
			}
	    });
		cityPicker.setData(cityData);
		if (!userId) {
			$('#cityHidden').val(cityData[0].value);
			$('#city').val(cityData[0].text);
		}
	}

	/**
	 * 县级数据
	 */
	var getCountryData = function(userId) {
		var cityCode = $('#cityHidden').val();
		if (!cityCode) {
			mui.alert('请先选择市');
		}
		$.each(cityData, function(n, item){
			if (item.value == cityCode) {
				countryData = item.children;
			}
	    });
		countryPicker.setData(countryData);
		if (!userId) {
			$('#countryHidden').val(countryData[0].value);
			$('#country').val(countryData[0].text);
		}
		
	}

	var getStreetData = function(userId) {
		var countryCode = $('#countryHidden').val();
		$.ajax({
			url:'../static/data/town/' + countryCode + '.json',
			dataType:'json',
			success:function(town){
				var value = '',
					text = '',
					streetData = '';
				if (town) {
					streetData = town.children;
					value = streetData[0].value
					text = streetData[0].text
				}
				streetPicker.setData(streetData);
				if (!userId) {
					$('#streetHidden').val(value);
					$('#street').val(text);
				}
			}
		});
	}
	
	var initDatePicker = function() {
		var btns = $('.js-date');
		btns.each(function(i, btn) {
			btn.addEventListener('tap', function() {
				var optionsJson = this.getAttribute('data-options') || '{}';
				var options = JSON.parse(optionsJson);
				var id = this.getAttribute('id');
				
				var picker = new mui.DtPicker(options);
				var date = $('.js-date').val();
				if (date) {
					picker.setSelectedValue(date);
				}
				picker.show(function(rs) {
					btns.val(rs.value);
					picker.dispose();
				});
			}, false);
		});
	}
	
	
	return {
		createPicker : createPicker,
		getCityData : getCityData,
		getCountryData : getCountryData,
		getStreetData : getStreetData,
		initDatePicker : initDatePicker
	};
})();