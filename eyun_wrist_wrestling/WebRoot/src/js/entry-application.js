var controller = function ($,window,document) {
	var params = {};
	params.date = " ",
	params.s_id = window.location.search.substr(2).split("_id=")[1];
	params.flag = "1";
	var entryDate = "";
// 获取当前店铺有哪天参加了活动
$.ajax({
	type: "post",
	url: nodeurl + "shop/getAppointmentInfoByDate.do",
	data: params,
	dataType: "json",
	beforeSend: function (XMLHttpRequest) {
		$("#backgroundFefresh").show();
	},
	success: function (data) {
		$("#backgroundFefresh").hide();
		if(data.errorcode == "000000"){
			if (data.values.date.length>0) {
				$("#dates").html(new Date(data.values.date[0]).Format("yyyy年MM月dd日"));
				entryDate = data.values.date[0];
				var reportTime = data.values;
				console.log(reportTime);
				var temp = "";
				for(var i = 0; i < reportTime.date.length; i ++){
					temp+='<li data-temp="'+reportTime.date[i]+'">'+new Date(reportTime.date[i]).Format("yyyy年MM月dd日")+'</li>'
				}
				$("#reportTime").html(temp);
				refreshUI(data);
			}else{
				$("#dates").html(new Date().Format("yyyy年MM月dd日"));
			}
			// 选择日期
			$('.entry-date').on('click',function () {
				$('.updown-date').toggle();
			});
			$(".updown-date li").on("click",function(){
				var html=$(this).html()
				$("#dates").html(html)
				var selectDateData = {};
				selectDateData.date = $(this).data("temp")
				;
				entryDate = $(this).data("temp");
				selectDateData.s_id = window.location.search.substr(2).split("_id=")[1];
				selectDateData.flag = "1";
				console.log(selectDateData);
				$.ajax({
					type: "post",
					url: nodeurl + "shop/getAppointmentInfoByDate.do",
					data: selectDateData,
					dataType: "json",
					beforeSend: function (XMLHttpRequest) {
						$("#backgroundFefresh").show();
					},
					success: function (data) {
						$("#backgroundFefresh").hide();
						console.log(data);
						if(data.errorcode == "000000"){
							refreshUI(data);
						}else if(data.errorcode == "000023"){
							console.log("查询失败");
						}
					},
					error: function (err) {
						$("#backgroundFefresh").hide();
						alert(err);
					}
				}); 
			});
		}else if(data.errorcode == "000023"){
		}
	},
	error: function (err) {
		$("#backgroundFefresh").hide();
		alert(err);
	}
}); 


// 界面刷新方法
function refreshUI(dataUI) {
	$("#sum").html(dataUI.values.sum + "人");
	$("#sevenbelow").html(dataUI.values["0"] + "人");
	$("#sevenabove").html(dataUI.values["1"] + "人");
	$("#eightabove").html(dataUI.values["2"] + "人");
	$("#femalnum").html(dataUI.values["3"] + "人");

}





// 体重选择
var weightLevel = "0";
$('.weight-select').on('click',function () {
	$('.pulldown-weight').toggle();
})
$('.pulldown-weight li').on('click',function () {
	var html = $(this).html();
	$('#weight').html(html);
	weightLevel = $(this).index();
})

// 登录
$('.enrol img').on('click',function () {
	var weightData = {};
	weightData.s_id = window.location.search.substr(2).split("_id=")[1];
	weightData.date = entryDate;
	weightData.u_id = u_id;
	weightData.level = weightLevel;
	console.log(weightData);
	$.ajax({
		type: "post",
		url: nodeurl + "referee/sighUpForTheGame.do",
		data: weightData,
		dataType: "json",
		beforeSend: function (XMLHttpRequest) {
			$("#backgroundFefresh").show();
		},
		success: function (data) {
			$("#backgroundFefresh").hide();
			console.log(data)
			if(data.errorcode=="000000"){
				tips("请准时到店参加活动");
				$(".entry-mark-tipss").html("您已成功报名参赛活动");

			}else if(data.errorcode=="000034"){
				tips("预约报名失败");  
				$(".entry-mark-tipss").html("不好意思");
			}else if(data.errorcode == "000038"){
				tips("您已报名成功,请不要重复报名");
				$(".entry-mark-tipss").html("");
			}

		},
		error: function (err) {
			$("#backgroundFefresh").hide();
			alert(err);
		}
	});
});


	// 提示信息方法
	function tips(str) {
		$(".entry-mark-tips").html(str);
		$("#entry_mark").show();
		$("#entry_mark").on("click",function () {
			$(this).hide();
		});
	}

}(Zepto,window,document);
$(function () {

});