var expiresValue = 7 * 86400;

$(document).ready(function () {
    // 注册or登录
    $('#complete').bind("click", function (event) {
        login();
        return false;
    });
    $('#offline').bind("click", function (event) {
        offline();
    });
});

// 登录请求
function login() {
    var acc = $('#acc').val();
    var pwd = $('#pwd').val();
    if (!check(acc, pwd))
        return;
    pwd = bcrypt(acc, pwd);
    var request = $.ajax({
        url: '/i/login',
        data: {
            "acc": acc,
            "pwd": pwd
        },
        beforeSend: function () {
            $('#complete').attr("disabled", "disabled").text("登录中...");
        },
        complete: function () {
            $('#complete').removeAttr("disabled").text("登录");
        }
    });
    request.then(function (data) {
        handLogin(data);
    }, function (jqXHR, textStatus, errorThrown) {
        handErr(textStatus);
    });
}

// 检查数据
function check(acc, pwd) {
    if (!new RegExp("[\\w]{4,}").test(acc)) {
        showErr("帐号4~20位单词字符");
    } else if (pwd.length < 6) {
        showErr("密码6~20位");
    } else {
        return true;
    }
    return false;
}

// 处理登录消息
function handLogin(data) {
    if (1 == data.state) {
        Cookies.set('token', data.data, {
            expires: expiresValue
        });
        if (location.pathname.lastIndexOf("/login") == location.pathname.length - "/login".length) {
            window.open(location.pathname.substr(0, location.pathname.length - 6), "_self");
        } else {
            location.reload();
        }
    } else if (typeof (data.info) != "undefined") {
        showErr(data.info);
    }
}

function offline() {
    Cookies.remove('token');
    location.reload();
}

// BCrypt固定格式加密密码
function bcrypt(acc, pwd) {
    var salt = acc;
    for (var i = acc.length; i < 22; i++) {
        salt = salt + "0";
    }
    salt = "$2a$10$" + salt.replace("_", "/");
    return dcodeIO.bcrypt.hashSync(pwd, salt);
}

// 处理认证
function handToken() {
    var token = Cookies.get('token');
    if ('string' == typeof (token)) {
        var info = JSON.parse(BASE64.decode(token));
        $('.header-anth-y').css("display", "block");
        $('#user_nick').text(info.nick);
        var request = $.ajax({
            url: '/i/checkToken',
            data: {
                "token": token
            }
        });
        request.then(function (data) {
            if (1 == data.state) {
                Cookies.set('token', token, {
                    expires: expiresValue
                });
            } else if (2 == data.state || 3 == data.state) {
                showErr(data.info, 5000);
                $('.header-anth').css("display", "block");
                $('.header-anth-y').css("display", "none");
                Cookies.remove('token');
            }
        }, function (jqXHR, textStatus, errorThrown) {
        });
    } else {
        $('.header-anth').css("display", "block");
    }
}

handToken();