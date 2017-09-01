$(function() {
	$('#turnToModify').click(function() {
		var openId = $('#openId').val();
		window.location.href = "../user/turnToModify?openId=" + openId;  
	});
	
});