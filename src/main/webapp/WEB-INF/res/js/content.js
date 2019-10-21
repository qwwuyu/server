const isHistoryApi = !!(window.history && history.pushState);
const info = tokenInfo();
const token = Cookies.get('token');

let opt = getOpt().addClass("nav-active");

function getOpt() {
    let path = location.pathname == "/" ? "card" : location.pathname.substr(1);
    return $('#' + path);
}

$(document).ready(function () {
    if (isHistoryApi) {
        $(window).on("popstate", function (event) {
            opt.removeClass("nav-active");
            opt = getOpt().addClass("nav-active");
            let path = opt.attr("id");
            document.title = path;
            handContent(path);
        });
    }
    $('#card,#note,#flag,#tool').bind("click", function (event) {
        if (!isHistoryApi) {
            return true;
        }
        if (opt.attr("id") == $(this).attr("id")) {
            return false;
        }
        opt.removeClass("nav-active");
        opt = $(this).addClass("nav-active");
        let path = opt.attr("id");
        document.title = path;
        history.pushState(null, path, path);
        handContent(path);
        return false;
    });
    $('body').on('click', '.page-a', function (e) {
        if (!isHistoryApi) {
            return true;
        }
        let type = opt.attr("id");
        let path = type + "?page=" + $(this).attr("id");
        history.pushState(null, path, path);
        handContent(type);
        return false;
    }).on('click', '.main-content-rm', function (e) {
        rmPosts($(this));
        return false;
    });
});
// 加载内容
handContent(opt.attr("id"));

function handContent(path) {
    let params = getRequest();
    let request = $.ajax({
        url: "/i/" + path + "/get",
        data: {
            "token": token,
            "page": params.page
        },
        beforeSend: function () {
        },
        complete: function () {
        }
    });
    request.then(function (data) {
        if (1 == data.state) {
            handPage(path, data.data);
            handData(path, data.data);
        } else if (typeof (data.info) != "undefined") {
            showErr(data.info);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        handErr(textStatus);
    });
}

// 处理分页
function handPage(path, data) {
    let send = false;// card|note
    if ("card" == path) {
        send = true;
    } else if ("note" == path && info.auth == 5) {
        send = true;
    } else if ("flag" == path && info.auth == 5) {
        send = true;
    }
    let page = data.page;
    let count = data.count;
    let select = data.select;
    let start = select < 5 ? 1 : select - 4;
    let end = select > page - 5 ? page : select + 4;
    if (select < 5) {
        end = end + 5 - select;
        end = end > page ? page : end;
    } else if (select > page - 4) {
        start = start - select + page - 4;
        start = start < 1 ? 1 : start;
    }
    let pages = [];
    for (let i = start; i <= end; i++) {
        pages[i - start] = i;
    }
    let temp_page = template('temp_page', {
        path: path,
        send: send,
        page: page,
        count: count,
        select: select,
        pages: pages
    });
    $('.main-page').html(temp_page);
}

// 处理列表数据
function handData(path, data) {
    let list = data.datas;
    let sysTime = data.sysTime;
    for (let i = 0; i < list.length; i++) {
        list[i].time = showTime(sysTime, list[i].time);
    }
    let temp_page = template('temp_' + path, {
        datas: data.datas,
        userId: info.id,
        auth: info.auth
    });
    $('.main-content').html(temp_page);
}

// 删除帖子
function rmPosts(ts) {
    let path = opt.attr("id");
    let id = ts.attr("id");
    let request = $.ajax({
        url: "/i/" + path + "/rm",
        data: {
            "token": token,
            "id": id
        },
        beforeSend: function () {
            ts.attr("disabled", "disabled");
        },
        complete: function () {
            ts.removeAttr("disabled");
        },
    });
    request.then(function (data) {
        if (1 == data.state) {
            handContent(path);
        } else {
            showSucc(data.info, 5000);
        }
    }, function (jqXHR, textStatus, errorThrown) {
        handErr(textStatus);
    });
}