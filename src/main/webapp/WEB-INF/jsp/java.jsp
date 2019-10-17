<%@page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link href="/tres/img/qwwuyu.ico" type="image/x-icon" rel="shortcut icon">
    <meta name="keywords" content="qwwuyu,www.qwwuyu.com"/>
    <meta name="description" content="qwwuyu"/>
    <link href="/tres/css/textarea.css" type="text/css" rel="stylesheet"/>
    <script src="/tres/js/jquery-3.2.1.min.js" type="text/javascript"></script>
    <script src="/tres/js/editor.js" type="text/javascript"></script>
    <script src="/tres/js/js-cookie.js" type="text/javascript"></script>
    <title>java程序</title>
</head>
<body>
<div class="container">
    <div id="body" class="span-22 last">
        <div id="submission">
            <form accept-charset="UTF-8" action="/java/result" method="post">
                <div class="field">
                    <div class="rfloat">
                        <input checked="checked" id="advanced_editor" name="advanced_editor" onchange="toggle_editor()"
                               onclick="toggle_editor()" type="checkbox" value="1"/> 使用高级编辑器
                    </div>
                    <label for="code">代码</label><br/>
                    <textarea id="code" name="code"></textarea>
                </div>
                <div class="actions">
                    <input name="commit" type="submit" value="提交代码"/>
                </div>
            </form>
        </div>
        <script type="text/javascript">
            $('input[name="commit"]').click(function (e) {
                var link = "link_" + window.location.pathname;
                var value = Cookies.getJSON(link);
                if (value && (Date.now() - value.last_submit) < 5000) {
                    alert('五秒内不得连续提交');
                    return false;
                }
                Cookies.set(link, {
                    last_submit: Date.now()
                });
                return true;
            });

            var editor;

            function toggle_editor() {
                var cm = $('.CodeMirror'), c = $('#code');
                if ($('#advanced_editor').prop('checked')) {
                    cm.show();
                    editor.setValue(c.val());
                    c.hide();
                } else {
                    c.val(editor.getValue()).show();
                    cm.hide();
                }
                ;
                return true;
            }

            $(document).ready(
                function () {
                    editor = CodeMirror.fromTextArea(document
                        .getElementById("code"), {
                        lineNumbers: true,
                    });
                    $('#code').blur(function () {
                        editor.setValue($('#code').val());
                    });
                    toggle_editor();
                });
        </script>
    </div>
</div>
</body>
</html>