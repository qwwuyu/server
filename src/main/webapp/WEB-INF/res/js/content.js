var isHistoryApi = !!(window.history && history.pushState);
var path = (location.pathname == "/" ? "card" : location.pathname.substr(1));

var opt = $('#' + path).addClass("nav-active");
$(document).ready(function() {
	$('#card,#note,#flag,#tool').bind("click", function(event) {
		if (opt.attr("id") == $(this).attr("id"))
			return false;
		opt.removeClass("nav-active");
		opt = $(this).addClass("nav-active");
		return false;
	});
});
function handContent() {

}
handContent();