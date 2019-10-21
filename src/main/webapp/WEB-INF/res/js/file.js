const isHistoryApi = !!(window.history && history.pushState);
// temp_file模版
template(
    'temp_file',
    '<ul> {{each datas}} <li> {{if $value.dir == true}} <a class="file-folder flex-center" data-name="{{$value.name}}"> <i class="ion-icon ion-folder"></i> <span class="file-text file-text-folder">{{$value.name}}</span> </a> {{else if $value.dir != true}} <div class="flex-center"> <i class="ion-icon ion-document"></i> <span class="file-text file-text-file">{{$value.name}}</span> </div> <div data-name="{{$value.name}}" class="flex-center file-ctrl"> <span class="file-open">打开</span> <span class="file-download">下载</span> <span class="file-delete">删除</span> </div> {{/if}} </li> {{/each}}</ul>');

$(document).ready(function () {
    initUpload(getParam("path"));
    if (isHistoryApi) {
        $(window).on("popstate", function (event) {
            let path = getParam("path");
            initUpload(path);
        });
    }
    $('body').on('click', '.file-folder', function (e) {
        if (!isHistoryApi) {
            return true;
        }
        let oldPath = getParam("path");
        let name = $(this).data("name");
        let path = oldPath + ("" != oldPath ? "/" : "") + name;
        let url = location.pathname + "?path=" + path;
        history.pushState(null, name, url);
        initUpload(path);
        return false;
    }).on('click', '.file-open', function (e) {
        let oldPath = getParam("path");
        let name = $(this).parent().data("name");
        let path = oldPath + ("" != oldPath ? "/" : "") + name;
        let url = location.pathname + "/open/" + name + "?path=" + path;
        let win = window.open(url, '_blank');
        win.focus();
    }).on('click', '.file-download', function (e) {
        let oldPath = getParam("path");
        let name = $(this).parent().data("name");
        let path = oldPath + ("" != oldPath ? "/" : "") + name;
        let url = location.pathname + "/download" + "?path=" + path;
        window.open(url, "_self");
    }).on('click', '.file-delete', function (e) {
        let oldPath = getParam("path");
        let name = $(this).parent().data("name");
        let path = oldPath + ("" != oldPath ? "/" : "") + name;
        if (confirm("你确认要删除" + name + "?")) {
            deleteFile(path, $(this))
        }
    }).on('click', '#back', function (e) {
        let oldPath = getParam("path");
        if (oldPath != "") {
            let path;
            let url;
            if (oldPath.lastIndexOf("/") != -1) {
                path = oldPath.substring(0, oldPath.lastIndexOf("/"));
                url = location.pathname + "?path=" + path;
            } else {
                path = "";
                url = location.pathname;
            }
            history.pushState(null, name, url);
            initUpload(path);
        }
    });
});

function requestFile(path) {
    let request = $.ajax({
        url: location.pathname + "/query",
        data: {
            "path": path
        },
        beforeSend: function () {
        },
        complete: function () {
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            handFileData(data.data);
        } else {
            handFileData({});
        }
        if (1 != data.state && data.info) {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        handErr(textStatus);
        handFileData({});
    });
}

// 处理列表数据
function handFileData(list) {
    let temp_file = template('temp_file', {
        datas: list
    });
    $('.content').html(temp_file);
}

function initUpload(path) {
    if (path == "") {
        $("#back").hide();
        $("#all").show();
    } else {
        $("#back").show();
        $("#all").hide();
    }
    requestFile(path);
    //初始化上传插件
    $('#fileupload').fileupload({
        url: location.pathname + '/upload',
        formData: {
            "path": getParam("path")
        },
        dataType: 'text',
        progressall: function (e, data) {
            $('#progress').text(data.loaded + "/" + data.total);
        },
        done: function (e, data) {
            $('#result').text("done:" + data.result);
            let path = getParam("path");
            requestFile(path);
        },
        fail: function (e, data) {
            $('#result').text("fail:" + data.result);
        }
    });
}

function deleteFile(path, obj) {
    let params = getRequest();
    let request = $.ajax({
        url: location.pathname + "/delete",
        data: {
            "path": path
        },
        beforeSend: function () {
        },
        complete: function () {
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            showSucc("删除成功");
            obj.parent().parent().remove()
        } else if (data.info) {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        handErr(textStatus);
    });
}