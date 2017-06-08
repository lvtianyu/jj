/**
 * Created by lvtianyu on 16/8/15.
 */
/**
 * Created by lvtianyu on 16/7/9.ok
 */
var controller = (function ($, window, document) {
    var $telephone = $("#telephone"),
        $lognIn = $("#logn-in"),
        data = {},
        $password = $("#password");

    function initParam() {

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

    $telephone.on('blur', telephoneFn);

    $lognIn.on('click', lognInFn);

    function lognInFn() {
        var
            password = $password.val(),
            isture,
            RE = /\d{6}/g;
        data.password = password.match(RE);
        isture = data.telephone ? data.password ? false : "请填正确的写密码!" : "请填写注册的手机号";
        if (!isture) {

            // todo 请求
            
        } else {
            $password.val("");
            return noticeSetTimeoutPublick(isture)
        }

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