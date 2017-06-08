/**
 * Created by lvtianyu on 16/7/11.
 */
var controller = (function ($, window, document) {
    var s_id,
        isSameId,
        saveData = [],
        $backgroundFefresh = $("#backgroundFefresh"),
        $resultList = $('#result-list');
// 初始化数据
    function initParam() {
        s_id = locache.session.get("dress-s_id") || oGetVars.s_id;//在地址页登录的信息||裁判页面||
        // console.log(s_id)
    }

//发起的页面初始化的ajax信息
    function getShopRecordajax() {
        $.ajax({
            type: "get",
            url: nodeurl + "game/getShopRecord.do",
            data: {
                s_id: s_id
            },
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
                $backgroundFefresh.show();
            },
            success: function (json) {
                $backgroundFefresh.hide();

                if (json.result == "ok") {
                    var _data = json.values;
                    return analyticPropaganda(_data);
                } else {
                    var _notive = json.errormsg;
                    return noticeSetTimeoutPublick(_notive)
                }
            },
         
            error: function () {
                $backgroundFefresh.hide();
            }
        });
    }


//解析页面的图的信息
    function analyticPropaganda(data) {
        console.log(data);

        var tpls = "",
            first,
            second,
            first_image,
            second_image,
            first_nickname,
            second_nickname,
            first_support_num,
            second_support_num,
            data_i,
            Screening,//场次
            levelClass,
            g_id,
            screening,
            level,
            level_text,
            _savedata={},
            i = data.length - 1;

        //todo 此地将对人多时候做一个判断;

        if (i > -1) {

            do {

                data_i = data[i];
                first = data_i.a;
                second = data_i.b;
                first_image = first.head_portrait;
                second_image = second.head_portrait;
                first_support_num = first.num;
                second_support_num = second.num;
                first_nickname = first.nickname;
                second_nickname = second.nickname;
                level = data_i.level;
                screening = i + 1;
                g_id = data[i].g_id;

                switch (level) {
                    case 0:
                        levelClass = "vs_style0";
                        level_text = "70公斤以下级别";
                        break;
                    case 1:
                        levelClass = "vs_style1";
                        level_text = "70~85公斤级别";

                        break;
                    case 2:
                        levelClass = "vs_style2";
                        level_text = "85公斤以上级别";

                        break;
                    case 3:
                        levelClass = "vs_style3";
                        level_text = "女子级别";

                        break;
                }

                tpls += '<li id=' + g_id + '><div class="vs-style ' + levelClass + '"><ul><li class="frist-people"><div class="head-img"><div>' +
                    '<img src=' + first_image + ' alt="tou"></div></div><div class="nick-name">' +
                    first_nickname + '</div><div class="support-num">' + first_support_num +
                    '</div></li><li class="second-people"><div class="head-img"><div id="second-head-img">' +
                    '<img src=' + second_image + ' alt="tou"></div></div><div class="nick-name" id="second-nick-name">' +
                    second_nickname + '</div><div class="support-num" >' +
                    second_support_num + '</div></li></ul><div class="record-mark"><div class="level-mark">' +
                    level_text + '</div><div class="screening-mark">第<div>' + screening + '</div>场</div>' +
                    '<div class="make-win"><img src="../source/img/victory@2x.png" ></div></div></div>' +
                    '<div class="list-show-view hidden-style"><div class="title-support" >支持<span>' + first_nickname + '</span>人数</div>' +
                    '<ul class="list-ul"></ul></div></li>';


                _savedata[g_id] = {g_id: g_id, g_a: first.u_id, g_b: second.u_id};

                console.log(_savedata,screening)

            } while (--i >= 0);

            saveData = _savedata;

        }else {
            tpls='<li class="notice-none">暂无记录!</li>'
        }
        
        $resultList.append(tpls);

        return bindId()
    }
    
    function bindId() {

        $('#result-list>li').on("click", function () {
            var that = $(this),
             _id = that.attr("id"),
             _data;

            $(".active").hide().removeClass("active");
            if(isSameId != _id){
                _data = saveData[_id];
                isSameId = _id;//将ID保存给辨别者
                getThumbUpInfoajax(_data, that)
            }else{
                isSameId = "";
            }
        })
    }

//点击页面的场次获取对应下方的信息
    function getThumbUpInfoajax(data, that) {

        $.ajax({
            type: "get",
            url: nodeurl + "game/getThumbUpSum.do",
            data: data,
            dataType: dataType,
            beforeSend: function (XMLHttpRequest) {
                $backgroundFefresh.show();

            },
            success: function (json) {
                $backgroundFefresh.hide();

                if (json.result == "ok") {
                    var _data = json.values.list;
                    return analyticPropagandaInfor(_data, that);
                } else {
                    var _notive = json.errorcode;
                    return noticeSetTimeoutPublick(_notive)
                }
            },
       
            error: function () {
                $backgroundFefresh.hide();

            }
        });
    }

//解析
    function analyticPropagandaInfor(data, that) {

        var tpls = "",
            image,
            shopName,
            title,
            nickName,
            money,
            data_i,
            i = data.length - 1;

        //todo 此地将对人多时候做一个判断;

        if (i > -1) {

            do {
                data_i = data[i];
                image = data_i.head_portrait || "../source/img/img-defaule-head.png";
                nickName = data_i.nickname ||"无名氏";
                money = data_i.fee;

                tpls += '<li><div class="head-img-spectator"><img src=' + image + ' alt="">' +
                    '</div><ul><li class="nick-name-spectator">' + nickName + '</li><li class="support-money"> '+money+'元</li></ul></li>';


            } while (--i >= 0);
            
        }else {

            tpls += '<li class="notice-none">暂无支持者!</li>';
        }

        that.find(".list-ul").html(tpls);
        that.find(".list-show-view").show().addClass("active");
    }

    var doInit = function () {
        initParam();//初始化页面变量
        getShopRecordajax();
    };
    return {
        doInit: doInit
    }
})(Zepto, window, document);

$(function () {
    controller.doInit();
});