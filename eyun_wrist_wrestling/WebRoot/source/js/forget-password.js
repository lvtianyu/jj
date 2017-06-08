/**
 * Created by lvtianyu on 16/8/15.
 */
var controller =(function ($,window,document) {
    var
        $telephone =$("#telephone"),
        $testNumber =$("#test-number"),
        $testBtn =$("#test"),
        data ={},
        $sureBtn =$("#sure");

// 初始化数据
    function initParam() {

    }

    //验证码
    $testBtn.on("click",sendTestAjax);
    function sendTestAjax() {

        var val =$telephone.val(),
            _notive ="请录入您真实的手机号码!",
            num =12,
            RE =/^1[3|4|5|7|8]\d{9}$/g;
        data.phone =testInput(val,RE,_notive,num,$telephone);
        if (data.phone) {
            //todo 直接将值传给请求的接口;

        } else {
            return;
        }
    }

    function analyticSendTestAjax(data) {
        if (data.code ==200) {
            $testBtn.attr("class","next-btn-deactivate").html("发送成功").off();
        } else {
            noticeSetTimeoutPublick(data.message);
        }

    }

    //确定
    $sureBtn.on('click',sendSureAjax);

    function sendSureAjax() {

        var val =$testNumber.val(),
            isture,
            RE =/\d{6}/g,
            testNum =val.match(RE);
        isture =testNum ? data.phone ?  false:"请填商铺注册人手机号,获取验证码!":"请先填写发到您手上的验证码";
        if (!isture) {
            // todo 请求

        } else {
            return noticeSetTimeoutPublick(isture)
        }
    }

    function hrefUrl() {
        var newPage;
        localStorage.setItem("type",data.id);
        locache.session.set("phone",data.phone);
    }

    var doInit =function () {
        initParam();//初始化页面变量
    };

    return {
        doInit:doInit
    }
})(Zepto,window,document);

$(function () {
    controller.doInit();
});
