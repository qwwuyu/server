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
function L(str) {
	console.log(str);
}
function authInfo() {
	var auth = Cookies.get('auth');
	if ('string' == typeof (auth)) {
		var info = JSON.parse(BASE64.decode(auth));
		return info;
	}
	return JSON.parse("{}");
}