/**
 * Created by lvtianyu on 16/8/15.
 */
 var controller =(function ($,window,document) {
    var
    $telephone =$("#telephone"),
    $testNumber =$("#test-number"),
    $testBtn =$("#test"),
    data ={},
    $sureBtn =$("#logn-in");

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
        var params = {};
        params.phoneNumber = val;
        params.templateID = 0;
        if (data.phone) {
            //todo 直接将值传给请求的接口;
            $.ajax({
                type: "post",
                url: nodeurl + "sendSMSCaptchaCode.do",
                data: params,
                dataType: "json",
                beforeSend: function (XMLHttpRequest) {
                    $("#backgroundFefresh").show();
                },
                success: function (data) {
                    $("#backgroundFefresh").hide();
                    if (data.errorcode == "000028") {
                      showtips("短信验证码发送成功，请等待!")
                  }else{
                     console.log(data);
                     showtips("短信验证码发送失败");
                 }
             },
             error: function (err) {
                $("#backgroundFefresh").hide();
                alert(err);
            }
        });
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

    //点击确定按钮验证短信验证码
    $sureBtn.on('click',sendSureAjax);
    function sendSureAjax() {
        var val =$testNumber.val(),
        isture,
        RE =/\d{6}/g,
        testNum =val.match(RE);
        isture =testNum ? data.phone ?  false:"请填商铺注册人手机号,获取验证码!":"请先填写发到您手上的验证码";
        if (!isture) {
            // todo 请求
            var params = {};
            params.r_phone = $telephone.val();
            params.code = $testNumber.val();
            $.ajax({
                type: "post",
                url: nodeurl + "referee/resetPasswordCheckPhoneNumber.do",
                data: params,
                dataType: "json",
                beforeSend: function (XMLHttpRequest) {
                    $("#backgroundFefresh").show();
                },
                success: function (data) {
                    window.location.href = "findpwd.html?r_phone="+params.r_phone;

                    $("#backgroundFefresh").hide();
                    if(data.errorcode == "000000"){
                        window.location.href = "findpwd.html?r_phone="+params.r_phone;
                    }else if(data.errorcode == "000025"){
                        showtips("你的手机号尚未注册");
                    }else if(data.errorcode == "000030"){
                        showtips("短信验证码无效");
                    }else if(data.errorcode == "000032"){
                        showtips("验证码校验失败");
                    }
                },
                error: function (err) {
                    $("#backgroundFefresh").hide();
                    alert(err);
                }
            }); 

        } else {
            return noticeSetTimeoutPublick(isture)
        }
    }

    function hrefUrl() {
        var newPage;
        localStorage.setItem("type",data.id);
        locache.session.set("phone",data.phone);
    }

    function showtips(str) {
        $(".entry-mark-tips").html(str);   
        $("#entry_mark").show();
        $("#entry_mark").on('click', function() {
            $(this).hide();
        });
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
