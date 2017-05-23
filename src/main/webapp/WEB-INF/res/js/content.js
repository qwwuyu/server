var isHistoryApi = !!(window.history && history.pushState);

$(document).ready(function() {
	$('#card,#note,#flag,#tool').bind("click", function(event) {
		$(this).addClass("nav-active");
		return false;
	});
});
function handContent() {

}
handContent();