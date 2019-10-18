//打印
function L(str) {
    console.log(str);
}

// 提醒
function showErr(err, time) {
    var alertTxt = $('<div class="alert"></div>').text(err);
    $('.alert-list').append(alertTxt);
    setTimeout(function () {
        alertTxt.remove();
    }, 'number' == typeof (time) ? time : 3000);
}

// 提醒
function showSucc(err, time) {
    var alertTxt = $('<div class="alert"></div>').text(err);
    $('.alert-list-succ').append(alertTxt);
    setTimeout(function () {
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
function tokenInfo() {
    var token = Cookies.get('token');
    if ('string' == typeof (token)) {
        var info = JSON.parse(BASE64.decode(token));
        return info;
    }
    return JSON.parse("{}");
}

Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '')
            .substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? date[k]
                : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}

function showTime(sysTime, time) {
    var now = new Date(sysTime);
    var date = new Date(time);
    var diff = sysTime - time;
    if (diff < 180000) {
        return "刚刚";
    } else if (diff < 3600000) {
        return Math.ceil(diff / 60000) + "分钟前";
    } else if (diff < 86400000) {
        return Math.ceil(diff / 3600000) + "小时前";
    } else if (now.getFullYear() == date.getFullYear()) {
        return date.format('MM-dd hh:mm:ss');
    } else {
        return date.format('yyyy-MM-dd hh:mm:ss');
    }
}
// 分页模版
template(
		'temp_page',
		'<ul><li class="main-page-list">{{if select != 1}}<a id="{{select - 1}}" href="/{{path}}?page={{select - 1}}" class="page-a">上一页</a>{{/if}}{{each pages}}{{if select != $value}}<a id="{{$value}}" href="/{{path}}?page={{$value}}" class="page-a">{{$value}}</a>{{else if select == $value}}<span class="page-span">{{$value}}</span>{{/if}}{{/each}}{{if select != page}}<a id="{{select + 1}}" href="/{{path}}?page={{select + 1}}" class="page-a">下一页</a>{{/if}}</li><li class="main-page-num"><span>{{count}}</span>条，共<span>{{page}}</span>页</li></ul>{{if send}}<a href="/{{path}}/send" target="_blank" class="page-send">发帖</a>{{/if}}');
// card模版
template(
		'temp_card',
		'<ul>{{each datas}}<li><span class="main-content-title">{{$value.title}}</span><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span>{{if $value.userId == userId || auth == 5}}<span id="{{$value.id}}" class="main-content-rm">删除</span>{{/if}}</li>{{/each}}</ul>');
// note模版
template(
		'temp_note',
		'<ul>{{each datas}}<li><a href="/note/content?id={{$value.id}}" target="_blank" class="main-content-click"> <span class="main-content-title">{{$value.title}}</span><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span>{{if $value.userId == userId || auth == 5}}<span id="{{$value.id}}" class="main-content-rm">删除</span>{{/if}}</a></li>{{/each}}</ul>');
// flag模版
template(
		'temp_flag',
		'<ul>{{each datas}}<li><span class="main-content-title">{{$value.title}}</span><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span></li>{{/each}}</ul>');
// note_content模版
template(
		'temp_note_content',
		'<span class="main-content-title">{{note.title}}</span><span class="main-content-info">{{note.nick}}</span><span class="main-content-info">{{note.time}}</span><span class="main-content-title">{{note.content}}</span>');
