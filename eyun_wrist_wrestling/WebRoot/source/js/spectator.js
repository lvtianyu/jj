/**
 * Created by lvtianyu on 16/7/9.ok
 */
var controller = (function ($, window, document) {

    var $firstSupportBtn = $("#first-support-btn"),//第一个进来的人
        $secondSupportBtn = $("#second-support-btn"),//第二个进来的人
        $firstHeadImg = $('#first-head-img'),
        $secondHeadImg = $('#second-head-img'),
        $firstSupportNum = $("#first-support-num"),
        $secondSupportNum = $("#second-support-num"),
        firstPeopleObj = {},//保存第一个人的id
        secondPeopleObj = {},//保存第二个人的id
        $listTitle = $("#list-title"),
        g_id,
        dataForPay = {
            userId: userId,
            totalFee: 1, //元
            applyType: 0
        },
        savePayData,//保存支付后刷新的列表数组
        $pay = $("#pay"),
        $paySure = $("#pay-sure"),
        $backGame = $(".back-game"),
        $listShowView = $("#list-show-view"),
        $backgroundFefresh = $("#backgroundFefresh"),
        userId,
        suportBtn = $("#list-title-div"),//支持者层
        s_id,
        firstPeopleOrSecond = 1,
        $listShowUl = $('#list-show-ul');

// 初始化数据
    function initParam() {

        var _data = locache.session.get("getGameInfoBySid");
        s_id = locache.session.get("dress-s_id") || oGetVars.s_id;

        if (_data) {

            getParticipantInfor(_data);

        } else {
            getParticipantInforAjax();
        }

        g_id = _data.g_id;
        dataForPay.gameId = g_id;
        firstPeopleObj = _data.a;//保存第一个人的id
        secondPeopleObj = _data.b;//保存第二个人的id
        savePayData = {g_id: g_id, g_a: _data.a.u_id, g_b: _data.b.u_id};//先初始化一下

        userId = locache.session.get("userId");
        dataForPay.userId = locache.session.get("userId");//登录者ID

        $listTitle.html(firstPeopleObj.nickname);//第一次支持者

        getPropagandaInforAjax(savePayData);
    }

//初始化页面参赛者信息
    function getParticipantInforAjax() {
        $.ajax({
            type: "get",
            url: nodeurl + "game/getGameInfoBySid.do",
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
                    var _data = json.values;

                    locache.session.set("getGameInfoBySid", _data);
                    return getParticipantInfor(_data);

                } else {
                    var _notive = json.errormsg;
                    return noticeSetTimeoutPublick(_notive)
                }
            },
            complete: function (XMLHttpRequest) {
            },
            error: function () {
                $backgroundFefresh.hide();
            }
        });
    }


//解析参赛者信息到页面
    function getParticipantInfor(data) {
        var first = data.a,
            second = data.b;
        $firstHeadImg.attr("src", first.head_portrait);
        $("#first-nick-name").html(first.nickname);
        $secondHeadImg.attr("src", second.head_portrait);
        $("#second-nick-name").html(second.nickname);

    }

//初始化页面参赛者支持信息
    function getPropagandaInforAjax(data) {
        $.ajax({
            type: "get",
            url: nodeurl + "game/getThumbUpInfo.do",
            data: data,
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
                $backgroundFefresh.show();
            },
            success: function (json) {
                $backgroundFefresh.hide();

                if (json.result == "ok") {

                    var _data = json.values;
                    return analyticPropagandaInfor(_data);

                } else {
                    var _notive = json.errormsg;
                    return noticeSetTimeoutPublick(_notive)
                }
            },
            complete: function (XMLHttpRequest) {
            },
            error: function () {
                $backgroundFefresh.hide();
            }
        });
    }

//解析看客信息
    function analyticPropagandaInfor(json) {

        var tpls = "",
            image,
            shopName,
            title,
            nickName,
            klio,
            data_i,
            data = json.list,
            i = data.length - 1;

        //todo 此地将对人多时候做一个判断;

        if (i > -1) {

            do {
                data_i = data[i];
                image = data_i.head_portrait;
                nickName = data_i.nickname || "无名氏";

                tpls += '<li><div class="head-img-spectator"><img src=' + image + ' alt="">' +
                    '</div><ul><li class="nick-name-spectator">' + nickName + '</li><li class="support-money">1元</li></ul></li>';

            } while (--i >= 0);
            suportBtn.show();
        } else {
            tpls = '<li class="notice-none">支持比赛的观众,结束比赛后可得到相应的红包,细节咨询店家!</li>';
            suportBtn.hide();
        }
        if (firstPeopleOrSecond == 1) {
            $firstSupportNum.html(json.a_num);
            $secondSupportNum.html(json.b_num);
        } else {
            $firstSupportNum.html(json.b_num);
            $secondSupportNum.html(json.a_num);
        }


        $listShowUl.html(tpls);
        $listShowView.show();
    }

//判断参与者是那个
    function judgePeoplePublick(id) {

        var _data = {g_id: g_id},
            title;
        if (id.search("first") != -1) {
            _data.g_a = firstPeopleObj.u_id;
            _data.g_b = secondPeopleObj.u_id;
            firstPeopleOrSecond = 1;
            title = firstPeopleObj.nickname;

        } else {
            _data.g_a = secondPeopleObj.u_id;
            _data.g_b = firstPeopleObj.u_id;
            firstPeopleOrSecond = 2;
            title = secondPeopleObj.nickname;

        }

        $listTitle.html(title);

        return _data;
    }

//获取参与者信息
    function getPropagandaInforAgain() {
        var _id = $(this).attr("id"),
            _data = judgePeoplePublick(_id);
        getPropagandaInforAjax(_data);
    }

//make sure pay for game ajax

    function payAjax(savePayData) {
        $.ajax({
            type: "get",
            url: nodeurl + "wechat/payForGame.do",
            data: dataForPay,
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
                $backgroundFefresh.show();
            },
            success: function (json) {
                $backgroundFefresh.hide();
                $pay.hide();

                if (json.result == "ok") {
                    var vj = json.values;

                    wx.chooseWXPay({
                        timestamp: vj.timeStamp,
                        nonceStr: vj.nonceStr,
                        package: vj.package,
                        signType: "MD5",
                        paySign: vj.paySign,
                        success: function (res) {
                            // 支付成功后的回调函数，转向预览页
                            return getPropagandaInforAjax(savePayData);
                        },
                        fail: function (res) {
                            alert("fail"+res)
                        },
                        cancel: function () {
                            // 用户取消分享后执行的回调函数
                            alert("cancel"+res);
                        }
                    });
                } else {
                    var _notive = json.errormsg;
                    return noticeSetTimeoutPublick(_notive);

                }
            }
        });
    }

    $firstSupportBtn.on("click", function () {

        dataForPay.sportUserId = firstPeopleObj.u_id;
        var _id = $(this).attr("id");
        savePayData = judgePeoplePublick(_id);
        $pay.show();
    });

    $secondSupportBtn.on("click", function () {

        dataForPay.sportUserId = secondPeopleObj.u_id;
        var _id = $(this).attr("id");
        savePayData = judgePeoplePublick(_id);
        $pay.show();
    });

//确定支付btn
    $paySure.on("click", function () {
        payAjax(savePayData);
        $backgroundFefresh.show();
    });

//取消支付
    $backGame.on("click", function () {
        $pay.hide();
    });

//点击头像获取此人的支持者和列表
    $("#first-people").on("click", getPropagandaInforAgain);

    $("#second-people").on("click", getPropagandaInforAgain);

//点击跳转到记录页
    $('#to-record').on("click", function () {

        locationSelf.href = "record-page.html?s_id=" + s_id;
    });

    var doInit = function () {
        initParam()
    };

    return {
        doInit: doInit
    }
})(Zepto, window, document);

$(function () {
    controller.doInit();
});
