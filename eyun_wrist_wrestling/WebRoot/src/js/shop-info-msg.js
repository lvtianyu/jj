/**
 * Created by lvtianyu on 16/7/9.ok
 */
var urlContent;
var controller = (function ($, window, document) {

    var $backgroundFefresh = $("#backgroundFefresh"),
        $list = $("#list"),
        $shopList=$("#shop-list"),
        $toEntryApplication=$("#to-entry-application"),
        s_id;

// 初始化数据
    function initParam() {

        // var _data = locache.session.get("getGameInfoBySid");
        s_id =oGetVars.s_id || locache.session.get("s_id");
    }

    function getShopAjax() {
        var data = {
                s_id: s_id
            },
            url = "shop/getShopInfo.do",
            getShopInfo = new PublickFn(url, data, analysisShopAjax);
        getShopInfo.ajaxFn();
        getShopInfo = null;

    }

    function analysisShopAjax(data) {
        var tpls = "",
            image,
            shopName = data.s_name,
            content=data.content,
            pic=data.pic,
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

        }

        $list.html(tpls);
        if(content){
            urlContent = {
                h: nodeurl + "pages/shop-info-msg.html?s_id="+s_id,
                title: "只有分享到朋友圈才能参与本活动",
                content: content,
                pic: pic
            };

        }else{
            urlContent = {
                h: nodeurl + "pages/shop-info-msg.html?s_id="+s_id,
                title: "掰手腕、赢免单、拿好礼、乐生活",
                content: "参与店内掰手腕活动,为生活添乐趣,参与比赛有机会赢(奖品、免单券、优惠券等)。",
                pic: nodeurl+"source/img/shop-info-msg-sharedefault.min.png"
            }
        }
        initWxJSSDK();

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
        

    }
//点击跳转到记录页
    $('#to-record').on("click", function () {
        var newPage="record-page.html";
        herfUrl(newPage)
    });

//点击跳转到报名页面
    $toEntryApplication.on("click",function () {
        var newPage="entry-application.html";
        herfUrl(newPage)
    });

    function herfUrl(newPage) {
        locationSelf.href=newPage+"?s_id="+s_id;
    }

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
