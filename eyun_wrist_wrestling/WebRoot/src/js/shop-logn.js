var controller = (function ($, window, document) {
    var $telephone = $("#telephone"),
    $lognIn = $("#logn-in"),
    data = {},
    $password = $("#password");

    var user = null, pwd = null, shopName = null;

    function initParam() {
        user = window.localStorage.getItem("userphone");
        pwd = window.localStorage.getItem("userpwd");
        shopName = window.localStorage.getItem("s_name");
        $telephone.val(user);
        $password.val(pwd);
    }

    function telephoneFn() {
        var telephone = $telephone.val(),
        // b = telephone.replace(/^1[3|4|5|7|8]\d{9}$/g,'$1""');
        _notive = "请录入您真实的手机号码!",
        num = 12,
        RE = /^1[3|4|5|7|8]\d{9}$/g;
        data.telephone = testInput(telephone, RE, _notive, num, $telephone);

        // $telephone.html(b);
    }
    
//校验手机号
$telephone.on('blur', telephoneFn);

//点击登录按钮
$lognIn.on('click', lognInFn);

function lognInFn() {
    var parmas = {},
    password = $password.val(),
    isture;
    parmas.r_phone = data.telephone;
    parmas.r_password = hex_md5(password);
    isture = data.telephone ? password ? false : "请填正确的写密码!" : "请填写注册的手机号";

    if(pwd && user && $telephone.val() == user && password == pwd){
        console.log($telephone.val());
        $(".login-mark-tips").html("当前所在的店铺是" +shopName);
        $("#login-mark").show();
        $("#makesure").on('click', function() {
            hrefPage(window.localStorage.getItem("s_id"));
        });
        $("#close").on('click', function() {
            $("#login-mark").hide();
        });
    }else{
        console.log("这里表示buchenggon");
        if (!isture) {
            // todo 请求
            $.ajax({
             type: "post",
             url: nodeurl + "referee/refereeLogin.do",
             data: parmas,
             dataType: "json",
             beforeSend: function (XMLHttpRequest) {
                $("#backgroundFefresh").show();
            },
            success: function (data) {
                console.log(parmas);
                console.log(data);
                $("#backgroundFefresh").hide();
                if(data.errorcode == "000000"){
                    console.log("成功了");
                    window.localStorage.userphone = parmas.r_phone;
                    window.localStorage.userpwd = password;
                    window.localStorage.s_id = data.values.s_id;
                    window.localStorage.s_name = data.values.s_name;
                    $(".login-mark-tips").html("当前所在的店铺是" +data.values.s_name);
                    $("#login-mark").show();
                    $("#makesure").on('click', function() {
                        hrefPage(data.values.s_id);
                    });
                    $("#close").on('click', function() {
                        $("#login-mark").hide();
                    });
                }else if(data.errorcode=="000025"){
                    $(".entry-mark-tips").html("尚未注册");    
                    $("#entry_mark").show();
                    $("#entry_mark").on('click', function() {
                        $(this).hide();
                    });  
                }else if(data.errorcode=="000027"){             
                    $(".entry-mark-tips").html("用户名或者密码错误");    
                    $("#entry_mark").show();  
                    $("#entry_mark").on('click', function() {
                        $(this).hide();
                    });  
                }
            },
            error: function (err) {
                console.log(parmas);
                console.log(err);
                $("#backgroundFefresh").hide();
            }
        });
        } else {
            $password.val("");
            return noticeSetTimeoutPublick(isture)
        }
    }
}
//登录成功调转制定比赛页;
function hrefPage(s_id) {
    var newPages = "referee-design-game.html?s_id="+s_id;
    locationSelf.replace(newPages);
}
var doInit = function () {
    initParam();
};
return {
    doInit: doInit
}
})
(Zepto, window, document);

$(function () {
    controller.doInit();
});