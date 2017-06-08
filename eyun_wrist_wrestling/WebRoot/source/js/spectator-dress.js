/**
 * Created by lvtianyu on 16/7/9.ok
 */
var controller = (function ($, window, document) {
    var $recordPageBtn = $("#record-page-btn"),
        $QRCODE = $("#QR-code-rgp"),
        isToRefereeView = false,//判断是否可以准备成功;
        // $gameOver = $("#game-over"),
        // $ok = $("#ok"),
        // $backGame = $("#back-game"),
        latitude,
        longitude,
        $backgroundFefresh = $("#backgroundFefresh"),
        s_id,//相应店铺的ID
        $logoWin=$("#logo-win"),
        shopAddressId;//店铺地址链接

    function initWxJSSDK() {
        var thisPageUrl = location.href;
        $.ajax({
            type: "get",
            url: nodeurl+"wechat/initJSSDK.do",
            data: {
                pageUrl: thisPageUrl
            },
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
            },
            success: function (json) {
                if (json.result == "ok") {
                    var signJson = json.values,
                        appid = signJson.appid,
                        timestamp = signJson.timestamp,
                        noncestr = signJson.noncestr,
                        signature = signJson.signature;
                    alert(signJson);
                    alert(signature);
                    if(wx != undefined){
                        //初始化微信sdk
                        wx.config({
                            debug: true,
                            appId: appid,
                            timestamp: timestamp,
                            nonceStr: noncestr,
                            signature: signature,
                            jsApiList: [
                                "getLocation",
                                "openLocation",
                                'chooseWXPay',
                                'checkJsApi'
                            ]
                        });
                        $backgroundFefresh.show();
                    }


                } else {
                    alert(json.errormsg);
                }
            },
            error: function () {
                //请求出错处理
                alert("error");
            },
            complete: function (XMLHttpRequest, textStatus) {
                //HideLoading();//关闭进度条
                //alert("complete");
                //console.log("complete");
            }
        });

    }

    wx.ready(function () {
        //判断当前客户端版本是否支持指定JS接口
        wx.getLocation({
            type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
            success: function (res) {
                latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                // var speed = res.speed; // 速度，以米/每秒计
                // var accuracy = res.accuracy; // 位置精度
                return locationAjax();//将地理位置信息给后台
            }
        });

    });

    //将地理位置信息给后台
    function locationAjax() {

        var data = {
                longitude: longitude,
                latitude: latitude,
                u_id: u_id
            },
            url = "shop/getShopList.do",
            getShopList = new PublickFn(url, data, analyticLocationAjax);
        getShopList.ajaxFn();
        getShopList = null;

        //
        // $.ajax({
        //     type: "get",
        //     url: nodeurl + "shop/getShopList.do",
        //     data: {
        //         longitude: longitude,
        //         latitude: latitude,
        //         u_id: u_id
        //     },
        //     dataType: dataType,
        //     beforeSend: function (XMLHttpRequest) {
        //
        //     },
        //     success: function (json) {
        //         $backgroundFefresh.hide();
        //         if (json.result == "ok") {
        //             var _data = json.values;
        //             return analyticLocationAjax(_data);
        //         } else {
        //             var _notive = json.errormsg;
        //             return noticeSetTimeoutPublick(_notive);
        //         }
        //
        //     },
        //     error: function () {
        //         $backgroundFefresh.hide();
        //
        //     }
        // });
        //
    }

//解析后台返回的列表
    function analyticLocationAjax(data) {
        var tpls = "",
            image,
            shopName,
            shopContent,
            s_id,
            activity,
            activityText,
            s_distance,
            data_i,
            i = data.length - 1;
        
        if (i > -1) {
            do {
                data_i = data[i];
                image = data_i.s_url || "../source/img/shop-back.png";
                shopName = data_i.s_name;
                shopContent = data_i.s_address || "无";
                s_id = data_i.s_id;
                activity = data_i.activity;
                s_distance = data_i.s_distance || 0;
                if (activity == 1) {
                    activityText = "活动进行中"
                } else {
                    activityText = "没有活动"
                }
                tpls += '<li class="list-li-border-bottom" id=' + s_id + '><div >' +
                    '<img src=' + image + ' alt="" id="shop-img"></div>' +
                    '<ul><li><div class="shop-name">' + shopName + '</div>' +
                    '<div class="shop-position">' + s_distance + '米</div></li>' +
                    '<li> <div class="ctivity-content">' + shopContent + '</div>' +
                    '<div class="is-shop"><span class="activity">' + activityText + '</span></div></li></ul></li>';
            
            } while (--i >= 0);

        } else {
            tpls = '<li class="notice-none">暂无店铺!</li>';
        }

        $("#list-shop-show").html(tpls);
        $backgroundFefresh.hide();
        return bindIdFn();
    }

    //绑定游戏相应的id
    function bindIdFn() {
        $("#list-shop-show>li").on("click", function () {
            s_id = $(this).attr("id");
            // $gameOver.show();
            comeInShop(s_id);
        })
    }

    //观看本店的比赛
    function comeInShop(s_id) {
        // $gameOver.hide();

        if (!s_id) {
            return;
        }

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

                    var newPage = "spectator.html?s_id="+s_id,
                        getGameInfoBySid = json.values;
                    locache.session.set("getGameInfoBySid", getGameInfoBySid);
                    locache.session.set("dress-s_id", s_id);
                    locationSelf.href = newPage;

                } else {
                    var _notive = json.errormsg;
                    noticeSetTimeoutPublick(_notive);
                    if (json.errorcode == "000022") {
                        var _id = "#" + s_id;
                        $(_id).find(".activity").html("没有活动");
                        locationSelf.href = "shop-info-msg.html?s_id="+s_id;
                    }
                }
            },
            complete: function (XMLHttpRequest) {
            },
            error: function () {
                $backgroundFefresh.hide();
            }
        });
    }

    //游戏确定
    // $ok.on("click", comeInShop);

    //游戏取消
    // $backGame.on("click", function () {
    //     $gameOver.hide();
    // });
    $logoWin.on("click",function () {
        locationSelf.href = "company-QR.html";
    });
    wx.error(function (res) {
        alert("微信接口出现错误: " + res.errMsg);
    });
    
    var doInit = function () {
        initWxJSSDK() ;

    };

    return {
        doInit: doInit
    }

})
(Zepto, window, document);

$(function () {
    controller.doInit();
});