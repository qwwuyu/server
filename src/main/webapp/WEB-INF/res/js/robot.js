$(document).ready(function () {
    $('#qqopen').bind("click", function (event) {
        const token = Cookies.get('token');
        if ('string' != typeof (token)) {
            showErr("请先登录");
        } else {
            openRobot(token);
        }
        return false;
    });
    $('#qqclose').bind("click", function (event) {
        const token = Cookies.get('token');
        if ('string' != typeof (token)) {
            showErr("请先登录");
        } else {
            closeRobot(token);
        }
        return false;
    });
});

function openRobot(token) {
    let request = $.ajax({
        url: '/robot/open',
        data: {
            "token": token
        },
        beforeSend: function () {
            $('#qqopen').attr("disabled", "disabled").text("操作中...");
        },
        complete: function () {
            $('#qqopen').removeAttr("disabled").text("开启");
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            showSucc("操作成功", 5000);
        } else {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        showErr(textStatus);
    });
}

function closeRobot(token, tag) {
    let request = $.ajax({
        url: '/robot/close',
        data: {
            "token": token
        },
        beforeSend: function () {
            $('#qqclose').attr("disabled", "disabled").text("操作中...");
        },
        complete: function () {
            $('#qqclose').removeAttr("disabled").text("关闭");
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            showSucc("操作成功", 5000);
        } else {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        showErr(textStatus);
    });
}