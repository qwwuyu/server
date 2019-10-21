$(document).ready(function () {
    $('#send_card').bind("click", function (event) {
        var title = $('#title').val();
        var token = Cookies.get('token');
        if ('string' != typeof (token)) {
            showErr("请先登录");
        } else if (!new RegExp(".{1,50}").test(title)) {
            showErr("内容不能为空");
        } else if (!new RegExp(".*[\\S]+.*").test(title)) {
            showErr("内容不能为空");
        } else {
            sendCard(token, title);
        }
        return false;
    });
    $('#send_note').bind("click", function (event) {
        var title = $('#title').val();
        var content = $('#content').val();
        var token = Cookies.get('token');
        if ('string' != typeof (token)) {
            showErr("请先登录");
        } else if (!new RegExp(".{1,50}").test(title)) {
            showErr("内容不能为空");
        } else if (!new RegExp(".*[\\S]+.*").test(title)) {
            showErr("内容不能为空");
        } else {
            sendNote(token, title, content);
        }
        return false;
    });
    $('#send_flag').bind("click", function (event) {
        var title = $('#title').val();
        var token = Cookies.get('token');
        if ('string' != typeof (token)) {
            showErr("请先登录");
        } else if (!new RegExp(".{1,50}").test(title)) {
            showErr("内容不能为空");
        } else if (!new RegExp(".*[\\S]+.*").test(title)) {
            showErr("内容不能为空");
        } else {
            sendFlag(token, title);
        }
        return false;
    });
});

function sendCard(token, title) {
    var request = $.ajax({
        url: '/i/card/send',
        data: {
            "token": token,
            "title": title
        },
        beforeSend: function () {
            $('#send_card').attr("disabled", "disabled").text("发表中...");
        },
        complete: function () {
            $('#send_card').removeAttr("disabled").text("发表");
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            $('#title').val("");
            showSucc("发表成功", 5000);
        } else {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        showErr(textStatus);
    });
}

function sendNote(token, title, content) {
    var request = $.ajax({
        url: '/i/note/send',
        data: {
            "token": token,
            "title": title,
            "content": content
        },
        beforeSend: function () {
            $('#send_note').attr("disabled", "disabled").text("发表中...");
        },
        complete: function () {
            $('#send_note').removeAttr("disabled").text("发表");
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            $('#title').val("");
            $('#content').val("");
            showSucc("发表成功", 5000);
        } else {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        showErr(textStatus);
    });
}

function sendFlag(token, title) {
    var request = $.ajax({
        url: '/i/flag/send',
        data: {
            "token": token,
            "title": title
        },
        beforeSend: function () {
            $('#send_flag').attr("disabled", "disabled").text("发表中...");
        },
        complete: function () {
            $('#send_flag').removeAttr("disabled").text("发表");
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            $('#title').val("");
            showSucc("发表成功", 5000);
        } else {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        showErr(textStatus);
    });
}