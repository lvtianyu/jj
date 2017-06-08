/**
 * Created by lvtianyu on 16/8/19.
 */

var controller = (function ($, window, document) {
    var gameDate = document.getElementById("titlePage"),
        $calendar = $("#calendar"),
        sumPepole = document.getElementById("sum-pepole"),
        level1 = document.getElementById("level-1"),
        level2 = document.getElementById("level-2"),
        level3 = document.getElementById("level-3"),
        level4 = document.getElementById("level-4"),
        $createGame = $("#create-game"),
        nowTime,//某天的日期
        $download=$("#download"),
        s_id;

    function initParam() {
        // localStorage.setItem("s_id",4);
        s_id=oGetVars.s_id||sessionStorage.getItem("s_id");
        nowTime =oGetVars.nowTime? new Date(oGetVars.nowTime).Format("yyyy-MM-dd") : " ";

        getDay(nowTime);
    }


//请求接口获取已选中日期
    function getDay(nowTime) {
        var data = {
                s_id: s_id,
                date: nowTime,
                flag: 0
            },
            url = "shop/getAppointmentInfoByDate.do",
            cancelTheGame = new PublickFn(url, data, analysisGetAppointmentInfoByDate);
        cancelTheGame.ajaxFn();
        cancelTheGame = null;
        // var data1={sum:70,0:233,1:3,2:4,3:333,date:["2016-08-19"]};
        // analysisGetAppointmentInfoByDate(data1)
    }

    function analysisGetAppointmentInfoByDate(json) {
        var data=nowTime!=" "?nowTime : json.date[0];
        var newDate = new Date(data).Format('yyyy年M月d日') ;
        gameDate.textContent=newDate;//设置日期
        level1.textContent=json[0];
        level2.textContent=json[1];
        level3.textContent=json[2];
        level4.textContent=json[3];
        sumPepole.textContent=json.sum;
    }

//跳转到参赛者扫码页面
    $createGame.on("click",function () {
        var newPage = "referee-get-participant.html?s_id="+s_id;
        return hrefUrl(newPage)
    });

    //到日历页
    $calendar.on("click", function () {
        var newPage = "calendar.html";
        return hrefUrl(newPage);
    });

    //download
    $download.on("click",function () {
        var newPage = "download.html?s_id="+s_id;
        return hrefUrl(newPage);
    });

    function hrefUrl(newPage) {

        locationSelf.href = newPage;
    }

    var doInit = function () {
        initParam();
    };
    return {
        doInit: doInit
    }
})(Zepto, window, document);

$(function () {
    controller.doInit();
});