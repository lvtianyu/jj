/**
 * Created by lvtianyu on 16/7/9.ok
 */
var controller = (function ($, window, document) {


    var $recordPageBtn = $("#record-page-btn"),
        $QRCODE = $("#QR-code-rgp"),
        userName,
        g_id,
        $gameOver = $("#game-over"),
        $backGame = $("#back-game"),//back page
        $ok = $("#ok"),//come in new game
        $cancelGame = $("#cancel-game"),
        $backgroundFefresh = $("#backgroundFefresh"),
        isToRefereeView = false,//判断是否可以准备成功;
        s_id;//店铺地址链接

    //初始化数据
    function initParam() {

        //根据s_id去生成相应的二维码;将本店的地址保存链接中没有就用保存中的
        var _shopId = locache.session.get("s_id");
        s_id =  oGetVars.s_id || _shopId;
        g_id = oGetVars.g_id;

        //如果链接中的店铺ID和本地存储的不一致就重新更新保存
        if (s_id != _shopId) {
            locache.session.set("s_id", s_id);
        }

        if (g_id) {
            return codeFn(g_id);//有就直接用
        } else {
            return createGameId();//没有就调接口
        }
    }

    //掉接口生成参赛二维码
    function createGameId() {

        $.ajax({
            type: "get",
            url: nodeurl + "game/generateCompetitionQrCode.do",
            data: {
                s_id: s_id
            },
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
                $backgroundFefresh.show();

            },
            success: function (json) {
                $backgroundFefresh.hide();
                if (json.result == "ok") {

                    g_id = json.values;
                    return codeFn(g_id);

                } else {
                    var _notive = json.errormsg;
                    return noticeSetTimeoutPublick(_notive)
                }
            },

            error: function () {
                $backgroundFefresh.hide();
            }
        });

    }

    //刷新接口
    function isToRefereeViewFn() {
        console.log(g_id)
        if (!g_id) {
            return
        }
        $.ajax({
            type: "get",
            url: nodeurl + "game/getCompetitorsInfo.do",
            data: {
                g_id: g_id
            },
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
                // $backgroundFefresh.show();
            },
            success: function (json) {
                if (json.result == "ok") {
                    isToRefereeView = true;
                    var _data = json.values;
                    locache.session.set("refereeViewInfor", _data);
                    $("#firstName").html(_data.a.nickname);
                    $("#secondName").html(_data.b.nickname);
                    $gameOver.show();

                } else {
                    var _notive = json.errormsg;
                    noticeSetTimeoutPublick(_notive)
                }
            },
            complete: function (XMLHttpRequest) {
                // $backgroundFefresh.hide();
            },
            error: function () {
                //myalert("error");
            }
        });
    }

    //定时器刷新页面
    function isToRefereeViewSetIntervalFn() {
        var time = setTimeout(function () {
            if (isToRefereeView) {
                clearTimeout(time)
            } else {
                isToRefereeViewFn();

            }
        }, 10000)
    }

//生成二维吗方法
    function codeFn(g_id) {
        var codeText = nodeurl + "pages/participant-view.html?gameId=" + g_id + "=" + s_id;

        $QRCODE.qrcode({
            width: 160,
            height: 160,
            text: codeText
        });

        return isToRefereeViewSetIntervalFn();
    }

    //跳转到记录列表页
    $recordPageBtn.on("click", function () {
        var newPage = "record-page.html";
        locationSelf.href = newPage + "?s_id=" + s_id;

    });

    //cancel game function
    function cancelgameFn() {
        $gameOver.hide();
        var data = {
                s_id: s_id,
                g_id: g_id
            },

            url = "game/cancelTheGame.do",
            cancelTheGame = new PublickFn(url, data, analysisAjax);
        cancelTheGame.ajaxFn();
        cancelTheGame = null;
    }

    function analysisAjax(_data) {
         g_id = _data.g_id;
        console.log(g_id);
        $("canvas").remove();
        codeFn(g_id)
    }

    //调转到裁判页
    $("#to-referee-btn").on("click",function () {
        console.log(1)
        return isToRefereeViewFn();
    });

    //关闭层
    $backGame.on("click", function () {
        $gameOver.hide()
    });

    //come in game
    $ok.on("click", function () {
        $gameOver.hide();
        if (isToRefereeView) {
            var newPage = "referee-view.html";
            return replaceUrl(newPage);
        }
    });

    //cancel Game
    $cancelGame.on("click", function () {
        cancelgameFn();
    });

    function replaceUrl(newPage) {
        //不能跳转返回
        var _data = newPage + "?g_id=" + g_id;
        locationSelf.replace(_data);
    }


    var doInit = function () {
        initParam();
    };
    return {
        doInit: doInit
    }
})(Zepto, window, document);

$(function () {
    controller.doInit();
});