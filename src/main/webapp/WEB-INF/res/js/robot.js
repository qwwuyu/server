$(document).ready(function() {
	$('#qqopen').bind("click", function(event) {
		var auth = Cookies.get('auth');
		if ('string' != typeof (auth)) {
			showErr("请先登录");
		} else {
			openRobot(auth);
		}
		return false;
	});
	$('#qqclose').bind("click", function(event) {
		var auth = Cookies.get('auth');
		if ('string' != typeof (auth)) {
			showErr("请先登录");
		} else {
			closeRobot(auth);
		}
		return false;
	});
});
function openRobot(auth) {
	var request = $.ajax({
		url : '/robot/open',
		data : {
			"auth" : auth
		},
		beforeSend : function() {
			$('#qqopen').attr("disabled", "disabled").text("操作中...");
		},
		complete : function() {
			$('#qqopen').removeAttr("disabled").text("开启");
		}
	});
	request.then(function(data) {
		if (1 == data.state) {
			showSucc("操作成功", 5000);
		} else {
			showErr(data.info);
		}
	}, function(jqXHR, textStatus, errorThrown) {
		showErr(textStatus);
	});
}
function closeRobot(auth, tag) {
	var request = $.ajax({
		url : '/robot/close',
		data : {
			"auth" : auth
		},
		beforeSend : function() {
			$('#qqclose').attr("disabled", "disabled").text("操作中...");
		},
		complete : function() {
			$('#qqclose').removeAttr("disabled").text("关闭");
		}
	});
	request.then(function(data) {
		if (1 == data.state) {
			showSucc("操作成功", 5000);
		} else {
			showErr(data.info);
		}
	}, function(jqXHR, textStatus, errorThrown) {
		showErr(textStatus);
	});
}