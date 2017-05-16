$(function() {
	$('#testme').click(function() {
		var bcrypt = dcodeIO.bcrypt;
		var salt = "$2a$10$qwwuyu3000000000000000";
		var hash = bcrypt.hashSync("123456", salt);
		alert(hash);
	});
});