var isHistoryApi = !!(window.history && history.pushState);
var params = GetRequest();

var opt = getOpt().addClass("nav-active");

function getOpt() {
	var path = location.pathname == "/" ? "card" : location.pathname.substr(1);
	return $('#' + path);
}
$(document).ready(function() {
	if (isHistoryApi) {
		$(window).on("popstate", function(event) {
			opt.removeClass("nav-active");
			opt = getOpt().addClass("nav-active");
			var path = opt.attr("id");
			document.title = path;
			handContent(path);
		});
	}
	$('#card,#note,#flag,#tool').bind("click", function(event) {
		if (!isHistoryApi) {
			return true;
		}
		if (opt.attr("id") == $(this).attr("id")) {
			return false;
		}
		opt.removeClass("nav-active");
		opt = $(this).addClass("nav-active");
		var path = opt.attr("id");
		document.title = path;
		history.pushState(null, path, path);
		handContent(path);
		return false;
	});
});
handContent(opt.attr("id"));
function handContent(path) {
	$(".content-main").text(path);
}