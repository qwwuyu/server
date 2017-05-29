//打印
function L(str) {
	console.log(str);
}
// 提醒
function showErr(err, time) {
	var alertTxt = $('<div class="alert"></div>').text(err);
	$('.alert-list').append(alertTxt);
	setTimeout(function() {
		alertTxt.remove();
	}, 'number' == typeof (time) ? time : 3000);
}
// 获取url参数
function GetRequest() {
	var url = location.search;
	var theRequest = new Object();
	if (url.indexOf("?") != -1) {
		var str = url.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
		}
	}
	return theRequest;
}
// 获取认证信息
function authInfo() {
	var auth = Cookies.get('auth');
	if ('string' == typeof (auth)) {
		var info = JSON.parse(BASE64.decode(auth));
		return info;
	}
	return JSON.parse("{}");
}
// 分页模版
template(
		'temp_page',
		'<ul><li class="main-page-list">{{if select != 1}}<a id="{{select - 1}}" href="/card?page={{select - 1}}" class="page-a">上一页</a>{{/if}}{{each pages}}{{if select != $value}}<a id="{{$value}}" href="/card?page={{$value}}" class="page-a">{{$value}}</a>{{else if select == $value}}<span class="page-span">{{$value}}</span>{{/if}}{{/each}}{{if select != page}}<a id="{{select + 1}}" href="/card?page={{select + 1}}" class="page-a">下一页</a>{{/if}}</li><li class="main-page-num"><span>{{count}}</span>条，共<span>{{page}}</span>页</li></ul>');
// 内容模版
template(
		'temp_card',
		'<ul>{{each datas}}<li><span class="main-content-title">{{$value.title}}</span><div><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span></div></li>{{/each}}</ul>');
template(
		'temp_note',
		'<ul>{{each datas}}<li><span class="main-content-title">{{$value.title}}</span><div><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span></div></li>{{/each}}</ul>');