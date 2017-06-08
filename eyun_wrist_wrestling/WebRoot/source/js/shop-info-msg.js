/**
 * Created by lvtianyu on 16/7/9.ok
 */
var controller = (function ($, window, document) {

    var $backgroundFefresh = $("#backgroundFefresh"),
        $list = $("#list"),
        $shopList=$("#shop-list"),
        s_id;


// 初始化数据
    function initParam() {

        var _data = locache.session.get("getGameInfoBySid");
        s_id =oGetVars.s_id;
    }

    function getShopAjax() {
        var data = {
                s_id: s_id
            },
            url = "shop/getShopInfo.do",
            getShopInfo = new PublickFn(url, data, analysisShopAjax);
        getShopInfo.ajaxFn();
        getShopInfo = null;

        // $.ajax({
        //     type: "get",
        //     url: nodeurl + "game/getShopInfo.do",
        //     data: {
        //         s_id: s_id
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
        //             return analysisShopAjax(_data);
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

    function analysisShopAjax(data) {
        var tpls = "",
            image,
            shopName = data.s_name,
            title,
            shopAddress = data.s_address,
            dataList = data.pictures,
            i = dataList.length - 1;
        if (i > -1) {
            do {
                image = dataList[i] || "../source/img/img-defaule-head.png";

                tpls += '<li><img src=' + image + '></li>';

            } while (--i >= 0);

            $("#shopName").html(shopName);
            $("#shop-deress").html(shopAddress);

        } else {

            tpls += '<li class="notice-none">暂无支持者!</li>';
        }

        $list.html(tpls)
    }
    
    function analysisGetGameInfoBySid() {
        var newPage = "spectator.html?s_id="+s_id,
            getGameInfoBySid = json.values;
        locache.session.set("getGameInfoBySid", getGameInfoBySid);
        locache.session.set("dress-s_id", s_id);
        locationSelf.href = newPage;
    }
    
    //观看本店的比赛
    function comeInShop() {
        // $gameOver.hide();

        if (!s_id) {
            return;
        }
        var data = {
                s_id: s_id
            },
            url = "game/getGameInfoBySid.do",
            getGameInfoBySid = new PublickFn(url, data, analysisGetGameInfoBySid);
        getGameInfoBySid.ajaxFn();
        getGameInfoBySid = null;
        
/*        
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
                }
            },
            complete: function (XMLHttpRequest) {
            },
            error: function () {
                $backgroundFefresh.hide();
            }
        });*/
        
    }
//点击跳转到记录页
    $('#to-record').on("click", function () {

        locationSelf.href = "record-page.html?s_id=" + s_id;

    });

    $shopList.on("click",comeInShop);

    var doInit = function () {
        initParam();
        getShopAjax()
    };

    return {
        doInit: doInit
    }
})(Zepto, window, document);

$(function () {
    controller.doInit();
});
