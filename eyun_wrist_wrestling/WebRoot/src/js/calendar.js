var controller = (function ($, window, document) {
    var onedayTime = 86400000,//每天的秒数
        today,//今天是几号
        monthNumber,
        htmlTodayTime = document.getElementById("todayTime"),
        $contentCalendar = $('#content-calendar'),
        $addGameDay = $("#addGameDay"),//添加可预约比赛日期
        systime,//当前系统时间
        monthNumberInit,//用来做本月的参照点的
        monitorChangeMonth,//变动的月份;
        date = [],//上传到后台
        s_id,
        newDate;//当前的系统时间的值new Date值
// 初始化数据
    function initParam() {
        s_id = localStorage.getItem("s_id");
        systime = oGetVars.systime || new Date().getTime();
        newDate = new Date(systime);
        monthNumberInit = newDate.Format('yyyy-MM') + '-01';
        monitorChangeMonth = new Date(monthNumberInit);
        today = Number(newDate.Format('d')) - 1;
        monthNumber = newDate.getMonth() + 1;//当前是几月份
        todayFn(monitorChangeMonth);
    }

    function todayFn(month) {
        var
            nowNewDate = new Date(month),
            n = nowNewDate.getMonth() + 1,
            monthNow = nowNewDate.getTime(),
            prvMonthLen = nowNewDate.getDay(),
            nowMonthLen = new Date(nowNewDate.getFullYear(), (nowNewDate.getMonth() + 1), 0).getDate(),
            dayActive;
        htmlTodayTime.textContent = nowNewDate.Format('yyyy年M月');

        dayFn(prvMonthLen, monthNow, nowMonthLen, n, nowNewDate)
    }


    function dayFn(prvMonthLen, monthNow, nowMonthLen, n, nowNewDate) {
        var tpls = "";
        var todayMonthPrv;
        var len = nowMonthLen;
        var i = 0, j = 1, day, classStyle1, classStyle2;
        var yearMonth = nowNewDate.Format("yyyy-MM") + "-";
        var id;
        if (prvMonthLen >= 1) {
            do {
                day = new Date(todayMonthPrv).Format('d');

                tpls += ' <li ></li>'

            } while (--prvMonthLen > 0);
        }

        do {
            todayMonthPrv = monthNow + i * onedayTime;
            day = new Date(todayMonthPrv).Format('d');
            classStyle1 = today == i && n == monthNumber ? 'style="border:1px solid #B3B3B3"' : "";
            classStyle2 = today > i && n == monthNumber ? true : false;
            if (classStyle2) {
                tpls += ' <li class="backDay" >' + day + '</li>';
            } else {
                id = yearMonth + day;
                tpls += ' <li id=' + id + ' class="select" ' + classStyle1 + '>' + day + '</li>';
            }

        } while (++i < len);
        $contentCalendar.html(tpls);
        addStyle();
    }

//请求接口获取已选中日期
    function addStyle() {
        var date = new Date(monitorChangeMonth).Format("yyyy-MM-dd");
        var data = {
                s_id: s_id,
                date:date,
                flag:0
            },
            url = "shop/getAppointmentInfoByDate.do",
            cancelTheGame = new PublickFn(url, data, analysisGetAppointmentInfoByDate);
        cancelTheGame.ajaxFn();
        cancelTheGame = null;
        // analysisGetAppointmentInfoByDate(selectTime);

    }

    function analysisGetAppointmentInfoByDate(json) {
        var data = json.date;
        var len = data.length;
        var dateLen=date.length;
        var j=0;
        var i = 0;
        var id;
        if (len > 0) {
            do {
                 id = "#" + data[i];
                $(id).attr("class", "active");
            } while (++i < len);
        }
        if(dateLen>0){
            do {
                 id = "#" + date[j];
                console.log(id);
                $(id).attr("class", "isActive");
            } while (++j < len);
        }

        var selectList = $(".select");
        var activeList = $(".active");
        var isActiveList = $(".isActive");
        selectList.on("click", selectElementFn);//可选像
        activeList.on("click", activeElementFn);
        isActiveList.on("click", selectElementFn);
        // deleteAttr(i)
    }

    // function deleteAttr(i) {
    //     selectTime.date.splice(0,i);//改变原始数组
    //     console.log(selectTime,i)
    // }
    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };

    Array.prototype.indexOf = function (val) {
        for (var i = 0,len=this.length; i <len; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };

    //点击可选的时间时间

    function selectElementFn() {
        var content = this.id;
        var style = this.getAttribute("class");
        if (style == "isActive") {
            this.setAttribute("class", "");
            date.remove(content)
        } else {
            this.setAttribute("class", "isActive");
            date.push(content);
        }
    }


//已经激活的项目选完后跳回上一页
    function activeElementFn() {
        var id = this.id;
        locationSelf.href = "referee-design-game.html?nowTime=" + id;
    }

//^点击保存预约时间;

    $addGameDay.on("click", addGameDayAjax);

    function addGameDayAjax() {
       var a= date.join(',');
        console.log(a,date);
        if(date.length>0){
            var data = {
                    s_id: s_id,
                    date: a
                },

                url = "game/addAppointmentDate.do",
                cancelTheGame = new PublickFn(url, data, analysisAddGameDayAjax);
            cancelTheGame.ajaxFn();
            cancelTheGame = null;
        }else{
            return noticeSetTimeoutPublick("请选择添加日期");
        }

    }

    function analysisAddGameDayAjax(json) {
        noticeSetTimeoutPublick("成功添加日期");
        date=[];
        initParam();
        console.log(1)
    }

//////////////////////////////////////////////////$


    touch.on(window, 'swiperight', function (ev) {
        var n = new Date(monthNumberInit);
        //只有a大于当月时候才进行减月处理
        if (n < monitorChangeMonth) {
            monitorChangeMonth.setMonth(monitorChangeMonth.getMonth() - 1);
        }
        todayFn(monitorChangeMonth);
    });

    touch.on(window, 'swipeleft', function (ev) {
        monitorChangeMonth.setMonth(monitorChangeMonth.getMonth() + 1);
        todayFn(monitorChangeMonth);
    });

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