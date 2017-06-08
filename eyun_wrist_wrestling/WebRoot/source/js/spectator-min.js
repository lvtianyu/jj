/**
 * Created by lvtianyu on 16/7/9.
 */
var controller = (function ($) {
    var $firstSupportBtn=$("#first-support-btn"),$secondSupportBtn=$("#second-support-btn"),$firstHeadImg=$("#first-head-img"),$secondHeadImg=$("#second-head-img"),$firstSupportNum=$("#first-support-num"),$secondSupportNum=$("#second-support-num"),firstPeopleObj={},secondPeopleObj={},$listTitle=$("#list-title"),g_id,dataForPay={userId:userId,totalFee:1,applyType:0},savePayData,$pay=$("#pay"),$paySure=$("#pay-sure"),$backGame=$(".back-game"),$listShowView=$("#list-show-view"),$backgroundFefresh=$("#backgroundFefresh"),userId,suportBtn=$("#list-title-div"),s_id,firstPeopleOrSecond=1,$listShowUl=$("#list-show-ul");function initParam(){var b=locache.session.get("getGameInfoBySid");s_id=locache.session.get("dress-s_id")||oGetVars.s_id;if(b){getParticipantInfor(b)}else{getParticipantInforAjax()}g_id=b.g_id;dataForPay.gameId=g_id;firstPeopleObj=b.a;secondPeopleObj=b.b;savePayData={g_id:g_id,g_a:b.a.u_id,g_b:b.b.u_id};userId=sessionStorage.getItem("userId");dataForPay.userId=sessionStorage.getItem("userId");$listTitle.html(firstPeopleObj.nickname);getPropagandaInforAjax(savePayData)}function getParticipantInforAjax(){$.ajax({type:"get",url:nodeurl+"game/getGameInfoBySid.do",data:{s_id:s_id},dataType:dataType,beforeSend:function(b){$backgroundFefresh.show()},success:function(e){$backgroundFefresh.hide();if(e.result=="ok"){var f=e.values;locache.session.set("getGameInfoBySid",f);return getParticipantInfor(f)}else{var d=e.errormsg;return noticeSetTimeoutPublick(d)}},complete:function(b){},error:function(){$backgroundFefresh.hide()}})}function getParticipantInfor(d){var f=d.a,e=d.b;$firstHeadImg.attr("src",f.head_portrait);$("#first-nick-name").html(f.nickname);$secondHeadImg.attr("src",e.head_portrait);$("#second-nick-name").html(e.nickname)}function getPropagandaInforAjax(b){$.ajax({type:"get",url:nodeurl+"game/getThumbUpInfo.do",data:b,dataType:dataType,beforeSend:function(a){$backgroundFefresh.show()},success:function(a){$backgroundFefresh.hide();if(a.result=="ok"){var e=a.values;return analyticPropagandaInfor(e)}else{var f=a.errormsg;return noticeSetTimeoutPublick(f)}},complete:function(a){},error:function(){$backgroundFefresh.hide()}})}function analyticPropagandaInfor(i){var n="",s,p,m,l,t,o,q=i.list,r=q.length-1;if(r>-1){do{o=q[r];s=o.head_portrait;l=o.nickname||"无名氏";n+='<li><div class="head-img-spectator"><img src='+s+' alt=""></div><ul><li class="nick-name-spectator">'+l+'</li><li class="support-money">1元</li></ul></li>'}while(--r>=0);suportBtn.show()}else{n='<li class="notice-none">支持比赛的观众,结束比赛后可得到相应的红包,细节咨询店家!</li>';suportBtn.hide()}if(firstPeopleOrSecond==1){$firstSupportNum.html(i.a_num);$secondSupportNum.html(i.b_num)}else{$firstSupportNum.html(i.b_num);$secondSupportNum.html(i.a_num)}$listShowUl.html(n);$listShowView.show()}function judgePeoplePublick(f){console.log(this);var e={g_id:g_id},d;if(f.search("first")!=-1){e.g_a=firstPeopleObj.u_id;e.g_b=secondPeopleObj.u_id;firstPeopleOrSecond=1;d=firstPeopleObj.nickname}else{e.g_a=secondPeopleObj.u_id;e.g_b=firstPeopleObj.u_id;firstPeopleOrSecond=2;d=secondPeopleObj.nickname}$listTitle.html(d);return e}function getPropagandaInforAgain(){var d=$(this).attr("id"),c=judgePeoplePublick(d);getPropagandaInforAjax(c)};

    function payAjax(savePayData) {
        var _notive,
            data;
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
                    data ={tradeNo:vj.tradeNo} ;
                    WeixinJSBridge.invoke(
                        'getBrandWCPayRequest', {
                            appId: vj.appId,     //公众号名称，由商户传入
                            timeStamp: vj.timeStamp,         //时间戳，自1970年以来的秒数
                            nonceStr: vj.nonceStr, //随机串
                            package: vj.package,
                            signType: "MD5",         //微信签名方式：
                            paySign: vj.paySign //微信签名
                        },
                        function (res) {
                            var resMsg = res.err_msg;
                            // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                            if (resMsg == "get_brand_wcpay_request:ok") {

                                return getPropagandaInforAjax(savePayData);

                            }else if(resMsg == "get_brand_wcpay_request:cancel"){
                                cancelPay(data)
                            }else{
                                cancelPay(data);
                                _notive="微信付款出现问题(网络或平台原因)";
                                return noticeSetTimeoutPublick(_notive);
                            }
                        }
                    );

                } else {
                     _notive = json.errormsg;
                    return noticeSetTimeoutPublick(_notive);

                }
            }
        });
    }
    function cancelPay(a){$.ajax({type:"get",url:nodeurl+"wechat/payCancel.do",data:a,dataType:dataType,beforeSend:function(b){$backgroundFefresh.show()},success:function(d){$backgroundFefresh.hide();if(d.result=="ok"){}else{var c=d.errormsg;return noticeSetTimeoutPublick(c)}},complete:function(b){},error:function(){$backgroundFefresh.hide()}})}$firstSupportBtn.on("click",function(){dataForPay.sportUserId=firstPeopleObj.u_id;var b=$(this).attr("id");savePayData=judgePeoplePublick(b);$pay.show()});$secondSupportBtn.on("click",function(){dataForPay.sportUserId=secondPeopleObj.u_id;var b=$(this).attr("id");savePayData=judgePeoplePublick(b);$pay.show()});$paySure.on("click",function(){payAjax(savePayData);$backgroundFefresh.show()});$backGame.on("click",function(){$pay.hide()});$("#first-people").on("click",getPropagandaInforAgain);$("#second-people").on("click",getPropagandaInforAgain);$("#to-record").on("click",function(){locationSelf.href="record-page.html?s_id="+s_id});

    var doInit = function () {
        initParam()
    };

    return {
        doInit: doInit
    }
})(Zepto);

$(function () {
    controller.doInit();
});
