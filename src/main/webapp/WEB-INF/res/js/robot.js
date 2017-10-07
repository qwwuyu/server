$(document).ready(function() {
	$('#qqrefresh').bind("click", function(event) {
		var tag = $('#qqtag').val();
		$('#qrimg').attr("src", "/sres/img/" + tag + ".png");
		return false;
	});
	$('#qqopen').bind("click", function(event) {
		var tag = $('#qqtag').val();
		var pwd = $('#qqpwd').val();
		var auth = Cookies.get('auth');
		if ('string' != typeof (auth)) {
			showErr("请先登录");
		} else {
			openRobot(auth, tag, pwd);
		}
		return false;
	});
	$('#qqclose').bind("click", function(event) {
		var tag = $('#qqtag').val();
		var auth = Cookies.get('auth');
		if ('string' != typeof (auth)) {
			showErr("请先登录");
		} else {
			closeRobot(auth, tag);
		}
		return false;
	});
	$('#qqdelete').bind("click", function(event) {
		var tag = $('#qqtag').val();
		var auth = Cookies.get('auth');
		if ('string' != typeof (auth)) {
			showErr("请先登录");
		} else {
			deleteRobot(auth, tag);
		}
		return false;
	});
});
function openRobot(auth, tag, pwd) {
	var request = $.ajax({
		url : '/robot/open',
		data : {
			"auth" : auth,
			"tag" : tag,
			"pwd" : pwd
		},
		beforeSend : function() {
			$('#qqopen').attr("disabled", "disabled").text("操作中...");
		},
		complete : function() {
			$('#qqopen').removeAttr("disabled").text("挂机");
		}
	});
	request.then(function(data) {
		if (1 == data.statu) {
			$('#qrimg').attr("src", "/sres/img/" + tag + ".png");
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
			"auth" : auth,
			"tag" : tag
		},
		beforeSend : function() {
			$('#qqclose').attr("disabled", "disabled").text("操作中...");
		},
		complete : function() {
			$('#qqclose').removeAttr("disabled").text("停止");
		}
	});
	request.then(function(data) {
		if (1 == data.statu) {
			$('#qrimg').attr("src", "");
			showSucc("操作成功", 5000);
		} else {
			showErr(data.info);
		}
	}, function(jqXHR, textStatus, errorThrown) {
		showErr(textStatus);
	});
}
function deleteRobot(auth, tag) {
	var request = $.ajax({
		url : '/robot/delete',
		data : {
			"auth" : auth,
			"tag" : tag
		},
		beforeSend : function() {
			$('#qqdelete').attr("disabled", "disabled").text("操作中...");
		},
		complete : function() {
			$('#qqdelete').removeAttr("disabled").text("删除");
		}
	});
	request.then(function(data) {
		if (1 == data.statu) {
			$('#qrimg').attr("src", "");
			showSucc("操作成功", 5000);
		} else {
			showErr(data.info);
		}
	}, function(jqXHR, textStatus, errorThrown) {
		showErr(textStatus);
	});
}