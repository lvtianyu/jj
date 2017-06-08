$(document).ready(function(){
    
    $("pay-sure").click(function(){
    	payAjax(savePayData)
        $("p").slideToggle();
      });
    
    function payAjax(savePayData) {
        $.ajax({
            type: "get",
            url: nodeurl + "wechat/payForGame.do",
            data: dataForPay,
            dataType: dataType,
            success: function (json) {
                if (json.result == "ok") {

                    var vj = json.values;

                    wx.chooseWXPay({
                        timestamp: vj.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                        nonceStr: vj.nonceStr, // 支付签名随机串，不长于 32 位
                        package: vj.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                        signType: "MD5", // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                        paySign: vj.paySign, // 支付签名
                        success: function (res) {
                            // 支付成功后的回调函数，转向预览页
                        	alert(res);
                        }

                    });
                } else {
                    alert(json.errormsg);


                }
            }
        });
    }
	
});
