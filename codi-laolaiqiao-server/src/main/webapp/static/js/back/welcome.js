$(function() {
	$('#submit').click(function(e) {
		e.preventDefault();
		if (!isValid()) {
			return;
		}
		$.blockUI({ message: '保存中...' });
	    ajaxSubmit('../news/saveWelcome', $('form').serializeArray(), function(result) {
	    	if (result.success) {
	    		alert("保存成功");
	    		$.unblockUI();
	    	} else {
	    		alert(result.errorMessage);
	    		$.unblockUI();
	    	}
	    });
	});
});

function isValid() {
	var title = $('#title').val();
	if (!title) {
		alert('请输入标题');
		return false;
	}
	return true;
}