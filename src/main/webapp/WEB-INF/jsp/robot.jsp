<%@page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html>
<head>
    <title>qq机器人</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link href="/tres/img/qwwuyu.ico" type="image/x-icon" rel="shortcut icon"/>
    <meta name="keywords" content="qwwuyu,www.qwwuyu.com"/>
    <meta name="description" content="qwwuyu"/>
    <link href="/tres/css/ionicons.min.css" type="text/css" rel="stylesheet" media="screen"/>
    <link href="/res/css/comm.css" type="text/css" rel="stylesheet" media="screen"/>
    <script src="/tres/js/jquery-3.2.1.min.js"></script>
    <script src="/tres/js/template-web.js"></script>
    <script src="/tres/js/js.cookie.js"></script>
    <script src="/tres/js/jsencrypt.min.js"></script>
    <script src="/tres/js/bcrypt.min.js"></script>
    <script src="/tres/js/base64.min.js"></script>
    <script src="/res/js/util.js"></script>
    <script src="/res/js/robot.js"></script>
</head>
<body>
<div style="position: fixed; width: 100%; height: 100%; background-color: rgba(0, 0, 0, .05); z-index: -1000"></div>
<div class="center">
    <div class="header-anth-y top-h" style="float: left; display: block;">
        <ul>
            <li id="user"><span id="user_nick">未登录</span></li>
        </ul>
    </div>
    <button id="offline" class="btn" style="width: 100px; margin-left: 12px;">注销</button>
    <form class="header-anth" style="float: none;">
        <input id="acc" class="input" maxlength="20" placeholder="帐号" style="margin-top: 12px;"/>
        <input id="pwd" class="input" maxlength="20" type="password" placeholder="密码" style="margin-top: 12px;"/>
        <div style="margin-top: 12px;">
            <button id="complete" class="btn" style="width: 100px; margin-left: 12px;">登录</button>
        </div>
    </form>
    <form>
        <div style="margin-top: 12px;">
            <button id="qqopen" class="btn" style="width: 100px; margin-left: 12px;">开启</button>
            <button id="qqclose" class="btn" style="width: 100px; margin-left: 12px;">关闭</button>
        </div>
    </form>
</div>
<div class="alert-list"></div>
<div class="alert-list-succ"></div>
<script src="/res/js/auth.js"></script>
</body>
</html>