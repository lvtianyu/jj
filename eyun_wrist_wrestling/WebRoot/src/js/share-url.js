//
// var urlContent = {};
// function url() {
//     var s_id=oGetVars.s_id;
//         urlContent = {
//             h: nodeurl + "/pages/shop-info-msg.html?s_id="+s_id,
//             title: "只有分享到朋友圈才能参与本活动",
//             content: "我正在参与XXX店的掰手腕活动，集齐50个赞可获赠￥:48元的大盘鸡一份,谢谢亲们~",
//             pic: pic
//         };
//
// }

function initWxJSSDK() {
    var thisPageUrl = location.href;
    var data1 = {
            pageUrl: thisPageUrl
        },
        url1 = "wechat/initJSSDK.do",
        public1 = new PublickFn(url1, data1, shareFn);
    public1.ajaxFn();
    public1 = null;

    // $.ajax({
    //     type: "post",
    //     url: nodeurl + 'tenpayApiTicketShare.htm',
    //     data: {
    //         sign: 'c48d421b4364182263376e7b9d905067',
    //         url: thisPageUrl
    //     },
    //     dataType: dataType,
    //     beforeSend: function (XMLHttpRequest) {
    //     },
    //     success: function (json) {
    //         console.log(json);
    //
    //
    //
    //
    //
    //     },
    //     error: function () {
    //         //请求出错处理
    //         //console.log("error");
    //     },
    //     complete: function (XMLHttpRequest, textStatus) {
    //         //HideLoading();//关闭进度条
    //         //alert("complete");
    //         //console.log("complete");
    //

}

function shareFn(json) {     //var signJson = eval("(" + json.values + ")");

    var signJson = json,
        appid = signJson.appid,
        timestamp = signJson.timestamp,
        noncestr = signJson.noncestr,
        signature = signJson.signature;


    if (urlContent.pic) {
        //初始化微信sdk
        wx.config({
            debug: false,
            appId: appid,
            timestamp: timestamp,
            nonceStr: noncestr,
            signature: signature,
            jsApiList: [
                'onMenuShareTimeline',
                'onMenuShareAppMessage',
                ''
            ]
        });
    } else {
        wx.hideOptionMenu();
    }

    wx.ready(function () {
            //朋友圈
            wx.onMenuShareTimeline({
                title: urlContent.content, // 分享标题
                link: urlContent.h, // 分享链接
                imgUrl: urlContent.pic, // 分享图标
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                trigger: function (res) {
                    //shareLog()
                    //alert('用户点击分享到朋友圈');
                },
                success: function (res) {

                    //shareLog()
                    alert('已分享');
                },
                cancel: function (res) {
                    //alert('已取消');
                },
                fail: function (res) {
                    alert(JSON.stringify(res));
                }
            });

            //分享给朋友
            wx.onMenuShareAppMessage({
                title: urlContent.title, // 分享标题
                desc: urlContent.content, // 分享描述
                link: urlContent.h, // 分享链接
                imgUrl: urlContent.pic, // 分享图标
                type: 'link', // 分享类型,music、video或link，不填默认为link
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                success: function () {
                    //alert("成功")
                    // 用户确认分享后执行的回调函数
                    //shareLog()
                    alert('已分享');
                },
                cancel: function () {
                    // 用户取消分享后执行的回调函数
                    //alert('已取消');
                },
                fail: function (res) {
                    alert(JSON.stringify(res));
                }
            });



    });
}


// wx.error(function (res) {
//     alert("微信接口出现错误: " + res.errMsg);
// });

// $(function () {
//
// });

