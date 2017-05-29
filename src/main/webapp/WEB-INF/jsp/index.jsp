<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>card</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<link href="/res/img/qwwuyu.ico" type="image/x-icon" rel="shortcut icon" />
<meta name="keywords" content="qwwuyu,www.qwwuyu.com" />
<meta name="description" content="qwwuyu" />
<link href="/res/css/ionicons.min.css" type="text/css" rel="stylesheet" media="screen" />
<link href="/res/css/base.css" type="text/css" rel="stylesheet" media="screen" />
<script src="/res/js/jquery-3.2.1.min.js"></script>
<script src="/res/js/template-web.js"></script>
<script src="/res/js/js.cookie.js"></script>
<script src="/res/js/jsencrypt.min.js"></script>
<script src="/res/js/bcrypt.min.js"></script>
<script src="/res/js/base64.min.js"></script>
<script src="/res/js/util.js"></script>
</head>
<body>
	<div style="position: fixed; width: 100%; height: 100%; background-color: rgba(0, 0, 0, .05); z-index: -1000"></div>
	<div class="header">
		<div class="header-mask-wrp">
			<div class="header-mask-bg"></div>
			<div class="header-mask"></div>
		</div>
		<div class="w960 center">
			<span class="top-h">...</span>
			<div class="header-anth top-h">
				<ul>
					<li id="login"><span>登录</span></li>
					<li id="register"><span>注册</span></li>
				</ul>
			</div>
			<div class="header-anth-y top-h">
				<ul>
					<li id="user"><span id="user_nick"></span>
						<div id="user-menu" class="user-menu">
							<ul>
								<li><span>暂未开放</span></li>
								<li id="offline"><span>注销</span></li>
							</ul>
						</div></li>
				</ul>
			</div>
		</div>
	</div>
	<div class="content w960 center">
		<div class="nav">
			<ul>
				<li><a id="card" href="/card" class="nav-a"><i class="nav-ion ion-compose"></i><span class="nav-span">card</span></a></li>
				<li><a id="note" href="/note" class="nav-a"><i class="nav-ion ion-clipboard"></i><span class="nav-span">note</span></a></li>
				<li><a id="flag" href="/flag" class="nav-a"><i class="nav-ion ion-social-facebook-outline"></i><span class="nav-span">flag</span></a></li>
				<li><a id="tool" href="/tool" class="nav-a"><i class="nav-ion ion-settings"></i><span class="nav-span">tool</span></a></li>
			</ul>
		</div>
		<div class="content-main">
			<div class="main-page"></div>
			<div class="main-content"></div>
		</div>
	</div>
	<div class="footer">
		<div></div>
	</div>
	<div class="dialog dialog_anth">
		<form class="content auth-form">
			<i class="close ion-close-round"></i>
			<div class="panel">
				<span class="title">登录</span>
				<div class="input-group">
					<div class="input-box">
						<input id="acc" class="input" maxlength="20" placeholder="帐号" />
					</div>
					<div class="input-box" style="display: none;">
						<input id="nick" class="input" maxlength="20" placeholder="昵称" />
					</div>
					<div class="input-box">
						<input id="pwd" class="input" maxlength="20" type="password" placeholder="密码" />
					</div>
					<div class="input-box" style="display: none;">
						<input id="pwd2" class="input" maxlength="20" type="password" placeholder="重复密码" />
					</div>
				</div>
				<button id="complete" class="btn">登录</button>
				<div class="prompt-box">
					<span>没有账号？</span> <span id="switch" class="clickable">注册</span> <a id="forget" class="right clickable">忘记密码</a>
				</div>
			</div>
		</form>
	</div>
	<div class="alert-list"></div>
	<script src="/res/js/auth.js"></script>
	<script src="/res/js/content.js"></script>
</body>
</html>