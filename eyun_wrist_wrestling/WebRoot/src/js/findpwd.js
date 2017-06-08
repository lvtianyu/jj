// 验证密码
var controller = function ($,window,document) {
	var $pwd = $(".pwdfir"),
	$pwdagain = $(".pwdagain")
	$btn = $("#logn-in");
	$btn.on("click",function () {
		if ($pwd.val() === $pwdagain.val()) {
			console.log(window.location.search.substr(2));
			var params = {};
			params.r_phone = window.location.search.substr(2).split("_phone=")[1];
			params.r_password = hex_md5($pwd.val());
			params.reset_password = hex_md5($pwd.val());
			console.log(params);
			if($pwd.val().match(/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/)){
				$.ajax({
					type: "post",
					url: nodeurl + "referee/resetPassword.do",
					data: params,
					dataType: "json",
					beforeSend: function (XMLHttpRequest) {
						$("#backgroundFefresh").show();
					},
					success: function (data) {
						$("#backgroundFefresh").hide();
						console.log(data);
						if(data.errorcode == "000000"){
							tips("您已修改成功");
						}else if(data.errorcode == "000033"){
							tips("修改密码失败");
						}
					},
					error: function (err) {
						$("#backgroundFefresh").hide();
						alert(err);
					}
				}); 

			}else{
				tips("请输入6-20位的数字与字母");			}

			}else{
				tips("两次请输入相同的密码");
			}
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