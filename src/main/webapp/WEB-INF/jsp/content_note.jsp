<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>note详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<link href="/res/img/qwwuyu.ico" type="image/x-icon" rel="shortcut icon" />
<meta name="keywords" content="qwwuyu,www.qwwuyu.com" />
<meta name="description" content="qwwuyu" />
<link href="/res/css/base.css" type="text/css" rel="stylesheet" media="screen" />
<script src="/res/js/jquery-3.2.1.min.js"></script>
<script src="/res/js/template-web.js"></script>
<script src="/res/js/js.cookie.js"></script>
<script src="/res/js/util.js"></script>
</head>
<body>
	<div class="header">
		<div id="note_content" class="w960 center"></div>
	</div>
	<div class="alert-list"></div>
	<div class="alert-list-succ"></div>
	<script type="text/javascript">
		$.ajaxSetup({
			timeout : 5000,
			type : 'POST',
			cache : false,
			dataType : "json",
		});
		var params = GetRequest();
		var request = $.ajax({
			url : "/i/note/content",
			data : {
				"id" : params.id,
			},
		});
		request.then(function(data) {
			L(data);
			if (1 == data.statu) {
				var note = data.data.note;
				var sysTime = data.data.sysTime;
				note.time = showTime(sysTime, note.time);
				var temp_page = template('temp_note_content', {
					note : note,
				});
				$('#note_content').html(temp_page);
			} else {
				showErr(data.info);
			}
		}, function(jqXHR, textStatus, errorThrown) {
			handErr(textStatus);
		});
	</script>
</body>
</html>