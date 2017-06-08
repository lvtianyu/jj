var
    dataType = "json",   //正式

    nodejsServerIp = "http://camp.liver-cloud.com",//正式

    contextPath = "/eyun_wrist_wrestling/",

    nodeurl = nodejsServerIp + contextPath,

//公共代码
    
    locationSelf = location,

    locationSelfSearch = locationSelf.search,

    viewWidth = localStorage.getItem("viewWidth"),

    html = document.documentElement,

    oGetVars={},

    u_id = locache.session.get("userId");

if (!viewWidth) {
    // document.addEventListener('DOMContentLoaded', function () {
    if (html) {
        var windowWidth = html.clientWidth / 7.5;
        viewWidth = windowWidth + 'px';
        localStorage.setItem("viewWidth", viewWidth);
        html.style.fontSize = viewWidth;
    }
    // 等价于html.style.fontSize = windowWidth / 640 * 100 + 'px';

    // }, false);
} else {
    html.style.fontSize = viewWidth;
}

var ua = navigator.userAgent.toLowerCase();


if (ua.match(/MicroMessenger/i) == "micromessenger") {


} else {
    // documentSelf.write("请在微信中访问次页")
}

function checkWxLogin(userType) {
    u_id = locache.session.get("userId");
    console.log(u_id)
    if (!u_id || undefined == u_id || null == u_id || "" == u_id) {
        locationSelf.href = nodeurl + "wechat/redirectWechatLogin.do?userType=" + userType + "&g_id=" + list[1];
    }
}

(function (sSearch) {
    var rNull = /^\s*$/, rBool = /^(true|false)$/i;

    function buildValue(sValue) {
        if (rNull.test(sValue)) {
            return null;
        }
        if (rBool.test(sValue)) {
            return sValue.toLowerCase() === "true";
        }
        if (isFinite(sValue)) {
            return parseFloat(sValue);
        }
        if (isFinite(Date.parse(sValue))) {
            return new Date(sValue);
        }
        return sValue
    }

    if (sSearch.length > 1) {
        for (var aItKey, nKeyId = 0, aCouples = sSearch.substr(1).split("&"); nKeyId < aCouples.length; nKeyId++) {
            aItKey = aCouples[nKeyId].split("=");
            oGetVars[decodeURIComponent(aItKey[0])] = aItKey.length > 1 ? buildValue(decodeURIComponent(aItKey[1])) : null;
            //此处将unescape()替换了
        }
    }
})(locationSelfSearch);


