/**
 * Created by lvtianyu on 16/7/9.
 */
var controller = (function ($, window, document) {

    var $showKiloList = $("#show-kilo-list"),//选取公斤级列表
        $chooseBtn = $("#choose-btn"),
        $startGameBtn = $("#start-game"),
        $backGameOpen = $("#back-game-open"),
        $okOpenBtn = $('#ok-open'),
        $winBtn = $("win-btn"),
        isGameStart = false,//判断游戏是否开始
        startX,
        level = null,
        isdone,//是否完成了手势
        touchClient = true,
        firstPeopleObj = {},//保存第一个人的id
        secondPeopleObj = {},//保存第二个人的id
        firstOrSecondPeople,//第一个还是第二个人
        makeWin = document.getElementById("make-win"),
        target = document.getElementById("win-btn"),
        $gameOver = $("#game-over"),
        $ok = $("#ok"),
        $backGame = $("#back-game"),
        $backgroundFefresh = $("#backgroundFefresh"),
        $gameOpen = $("#game-open"),
        g_id;//选取公斤级按钮


    var isMobile = (function () {
        var u = navigator.userAgent, app = navigator.appVersion;
//            if (u.indexOf('Trident') > -1) {
//                addPrefix = "-ms-" //IE内核
//            } else if (u.indexOf('Presto') > -1) {
//                addPrefix = "-o-"//opera内核
//            } else if (u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1) {
//                addPrefix = "-moz-"//opera内核 //火狐内核
//            }
        //判断是否是移动终端
        if (u.indexOf('Mobile') > -1) {

        } else if (u.indexOf('iPad') > -1) {

        } else {
            touchClient = false;
        }

    })();

// 初始化数据
    function initParam() {
        var _data = locache.session.get("refereeViewInfor");
        //初始化页面参赛者信息
        firstPeopleObj = _data.a;
        secondPeopleObj = _data.b;
        if (_data) {
            getParticipantInfor(_data)
        } else {
            getParticipantInforAjax();//not information
        }
        g_id = oGetVars.g_id || locache.session.get("g_id");
    }


//初始化页面参赛者信息
    function getParticipantInforAjax() {

        var data = {
                g_id: g_id
            },
            url = "game/getCompetitorsInfo.do",
            getCompetitorsInfo = new PublickFn(url, data, getParticipantInfor);
        getCompetitorsInfo.ajaxFn();
        getCompetitorsInfo = null;

        //
        // $.ajax({
        //     type: "get",
        //     url: nodeurl + "game/getCompetitorsInfo.do",
        //     data: {
        //         g_id: g_id
        //     },
        //     dataType: dataType,
        //     beforeSend: function (XMLHttpRequest) {
        //         $backgroundFefresh.show();
        //     },
        //     success: function (json) {
        //
        //         $backgroundFefresh.hide();
        //
        //         if (json.result == "ok") {
        //
        //             var _data = json.values;
        //             return getParticipantInfor(_data);
        //
        //         } else {
        //             var _notive = json.errorcode;
        //             return noticeSetTimeoutPublick(_notive)
        //         }
        //     },
        //     complete: function (XMLHttpRequest) {
        //     },
        //     error: function () {
        //         $backgroundFefresh.hide();
        //     }
        // });
    }

    function analysisBeginTheGame(_data) {
        $("#second-support-num").html(_data.b);
        $("#frist-support-num").html(_data.a);
        $startGameBtn.off();//解绑开始按钮
        isGameStart = true;
    }

//开始游戏裁判点击游戏开始
    function starGameAjax() {

        if (!firstPeopleObj.u_id && !secondPeopleObj.u_id && !g_id) {
            return noticeSetTimeoutPublick("后台或者前台出现问题")
        }

        var data = {
                a: firstPeopleObj.u_id,
                b: secondPeopleObj.u_id,
                g_id: g_id
            },
            url = "game/beginTheGame.do",
            beginTheGame = new PublickFn(url, data, analysisBeginTheGame);
        beginTheGame.ajaxFn();
        beginTheGame = null;

        // $.ajax({
        //     type: "get",
        //     url: nodeurl + "game/beginTheGame.do",
        //     data: {
        //         a: firstPeopleObj.u_id,
        //         b: secondPeopleObj.u_id,
        //         g_id: g_id
        //     },
        //     dataType: dataType,
        //     beforeSend: function (XMLHttpRequest) {
        //         $backgroundFefresh.show();
        //     },
        //     success: function (json) {
        //         $backgroundFefresh.hide();
        //
        //         if (json.result == "ok") {
        //             var _data = json.values;
        //             console.log(_data, _data.b, _data.a);
        //             $("#second-support-num").html(_data.b);
        //             $("#frist-support-num").html(_data.a);
        //             $startGameBtn.off();//解绑开始按钮
        //             isGameStart = true;
        //
        //         } else {
        //             var _notive = json.errormsg;
        //             return noticeSetTimeoutPublick(_notive)
        //         }
        //
        //     },
        //     complete: function (XMLHttpRequest) {
        //     },
        //     error: function () {
        //         $backgroundFefresh.hide();
        //     }
        // });
    }

//解析参赛者信息到页面
    function getParticipantInfor(data) {
        var firstA = data.a,
            secondA = data.b,
            firstImg = firstA.head_portrait,
            secondImg = secondA.head_portrait;
        $("#frist-head-img").attr("src", firstImg);
        $("#frist-nick-name").html(firstA.nickname);
        $("#second-head-img").attr("src", secondImg);
        $("#second-nick-name").html(secondA.nickname);
        firstPeopleObj.u_id = firstA.u_id;//保存第一个人的id
        secondPeopleObj.u_id = secondA.u_id;//保存第二个人的id
    }

    touch.on('.vs-style', 'touchstart', function (ev) {
        ev.preventDefault();
    });
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);

    if (touchClient) {
        //游戏不开始是不能结束的;
        touch.on("#win-btn", 'touchstart', function (ev) {
            startX = ev.touches[0].clientX;
            touchStartFn();
        });
        touch.on("#win-btn", 'touchmove', function (ev) {
            var client = ev.touches[0].clientX;//手机版
            touchMoveFn(client);
        });

    } else {
        target.onmouseenter = function (ev) {
            startX = ev.clientX;
            touchStartFn();
        };

        target.onmousemove = function (ev) {
            var client = ev.clientX;//pc版
            touchMoveFn(client);
        }
    }

    //手指开始中
    function touchStartFn() {
        isdone = true;
    }

    //手指移动中
    var touchMoveFn = function (client) {
        if (isdone) {
            if (isGameStart) {
                if (client > startX) {

                    makeWin.setAttribute("style", "text-align:right");
                    firstOrSecondPeople = 2;
                    $gameOver.show();

                } else if (client < startX) {

                    makeWin.setAttribute("style", "text-align:left");
                    firstOrSecondPeople = 1;
                    $gameOver.show();

                }
                isdone = false;
                startX = client;
            } else {
                var _notive = "请先开始游戏!";
                return noticeSetTimeoutPublick(_notive)
            }
        }
    };

//游戏确定结束
    $ok.on("click", isGameOver);

//游戏结束取消
    $backGame.on("click", function () {
        $gameOver.hide();
        makeWin.setAttribute("style", "text-align:center");

    });

//是否要结束游戏
    function isGameOver() {
        $gameOver.hide();
        var data = {g_id: g_id, level: level};

        if (firstOrSecondPeople === 1) {
            data.winner = firstPeopleObj.u_id;
            data.g_b = secondPeopleObj.u_id;
        } else {
            data.winner = secondPeopleObj.u_id;
            data.g_b = firstPeopleObj.u_id;
        }
        // alert(data.g_b);
        // if(isGameStart) {
        return gameOverAjax(data);
        // }
    }

    function analysisDecideTheWinner() {
        var newPage = "referee-get-participant.html";
        locationSelf.href = newPage;
    }

//结束游戏
    function gameOverAjax(data) {
        if (!data.winner && !data.g_b) {
            return noticeSetTimeoutPublick("后台或者前台出现问题")
        }

        var url = "game/decideTheWinner.do",
            decideTheWinner = new PublickFn(url, data, analysisDecideTheWinner);
        decideTheWinner.ajaxFn();
        decideTheWinner = null;
        
        
        // $.ajax({
        //     type: "get",
        //     url: nodeurl + "game/decideTheWinner.do",
        //     data: data,
        //     dataType: dataType,
        //     beforeSend: function (XMLHttpRequest) {
        //         $backgroundFefresh.show();
        //     },
        //     success: function (json) {
        //         $backgroundFefresh.hide();
        //         if (json.result == "ok") {
        //             var newPage = "referee-get-participant.html";
        //             locationSelf.href = newPage;
        //         } else {
        //             var _notive = json.errormsg;
        //             return noticeSetTimeoutPublick(_notive)
        //         }
        //
        //     },
        //     complete: function (XMLHttpRequest) {
        //
        //     },
        //     error: function () {
        //         $backgroundFefresh.hide();
        //     }
        // });
    }

//选取公斤级按钮
    $chooseBtn.on("click", function () {

        $showKiloList.removeClass("hidden-style");

    });

//选取公斤级列表
    $("#show-kilo-list>ul>li").on("click", function () {
        var _text,
            that = $(this),
            id = that.attr("id");
        $showKiloList.addClass("hidden-style");
        $(".active").removeClass("active");
        that.addClass("active");
        _text = that.html();
        $chooseBtn.html(_text);
        level = id;
    });

//裁判点击取消游戏ajax
//     function cancelTheGameAjax() {
//         $.ajax({
//             type: "get",
//             url: nodeurl + "game/cancelTheGame.do",
//             data: {g_id: g_id},
//             dataType: dataType,
//             beforeSend: function (XMLHttpRequest) {
//                 $backgroundFefresh.show();
//             },
//             success: function (json) {
//                 $backgroundFefresh.hide();
//                 if (json.result == "ok") {
//                     var newPage = "referee-get-participant.html";
//                     locationSelf.replace(newPage);
//                 } else {
//                     var _notive = json.errormsg;
//                     return noticeSetTimeoutPublick(_notive)
//                 }
//             },
//             complete: function (XMLHttpRequest) {
//                 $backgroundFefresh.hide();
//             },
//             error: function () {
//                 $backgroundFefresh.hide();
//             }
//         });
//     }

//开始游戏按钮
    $startGameBtn.on("click", function () {
        $gameOpen.show();
    });

//确定开始层    
    $okOpenBtn.on("click", function () {
        $gameOpen.hide();

        if (!level) {
            return noticeSetTimeoutPublick("请你先选择体重级别,才能开始比赛")
        }

        starGameAjax();

    });

//返回
    $backGameOpen.on("click", function () {
        $gameOpen.hide();
    });

    function replaceUrl(newPage) {
        locationSelf.replace(newPage);
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