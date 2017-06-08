package com.liver_cloud.common;




import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.liver_cloud.model.IdentifyingCode;

public class SMSUtil {
	/**
	 * 发送方法(验证码) phoneNum 接收方电话号码 identifyingCode 验证码 time 有效时间 （默认为10分钟）
	 * SMSTemplateNum 短信模板编号（在云通讯的平台上添加短信模板编号） type 0：表示开发测试 1：表示生产环境使用 返回值
	 * 000000 代表发送成功。
	 */
	public static IdentifyingCode sendSMSVerificationCode(String phoneNum,
			String time, String SMSTemplateNum, String type) {
		String identifyingCode = null;
		identifyingCode = returnIdentifyingCode();
		HashMap<String, Object> result = null;

		// 初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

		// ******************************注释*********************************************
		// *初始化服务器地址和端口 *
		// *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		// *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
		// *******************************************************************************
		if (type.endsWith("0")) {
			restAPI.init("sandboxapp.cloopen.com", "8883");
		} else {
			restAPI.init("app.cloopen.com", "8883");
		}

		// ******************************注释*********************************************
		// *初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN *
		// *ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		// *参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。 *
		// *******************************************************************************
		String ACOUNT_SID = null;
		String AUTH_TOKEN = null;
		ACOUNT_SID = "aaf98f894dd24f24014dd2633aa10034";
		AUTH_TOKEN = "d79532034eaa43e2b36785cb8b361c8e";
		restAPI.setAccount(ACOUNT_SID, AUTH_TOKEN);

		// ******************************注释*********************************************
		// *初始化应用ID *
		// *测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID *
		// *应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		// *******************************************************************************
		String APP_ID = "8a48b5514dd25566014dd267b33d0025";
		restAPI.setAppId(APP_ID);

		// ******************************注释****************************************************************
		// *调用发送模板短信的接口发送短信 *
		// *参数顺序说明： *
		// *第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号 *
		// *第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。 *
		// *系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		// *第三个参数是要替换的内容数组。 *
		// **************************************************************************************************

		// **************************************举例说明***********************************************************************
		// *假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
		// *
		// *result = restAPI.sendTemplateSMS("13800000000","1" ,new
		// String[]{"6532","5"}); *
		// *则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入 *
		// *********************************************************************************************************************
		result = restAPI.sendTemplateSMS(phoneNum, SMSTemplateNum,
				new String[] { identifyingCode, time });
		IdentifyingCode ic = new IdentifyingCode();
		ic.setPhoneNumber(phoneNum);
		ic.setStateCode(result.get("statusCode").toString());
		if (result.get("statusMsg") != null) {
			ic.setErrorContext(result.get("statusMsg").toString());
		}
		ic.setIdentifyingCode(identifyingCode);
		Date now = new Date();
		ic.setCreateTime(now);
		ic.setTime(time);
		ic.setSmsType(SMSTemplateNum);
		ic.setType(0);
		return ic;
	}

	/**
	 * 发送方法（邀请码） phoneNum 接收方电话号码 identifyingCode 验证码 time 有效时间 （默认为10分钟）
	 * SMSTemplateNum 短信模板编号（在云通讯的平台上添加短信模板编号） type 0：表示开发测试 1：表示生产环境使用 返回值
	 * 000000 代表发送成功。
	 */
	public static IdentifyingCode sendSMSInviteCode(String phoneNum,
			String time, String inviteCode, String applyIntroduction,
			String SMSTemplateNum, String type) {

		HashMap<String, Object> result = null;

		// 初始化SDK
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();

		// ******************************注释*********************************************
		// *初始化服务器地址和端口 *
		// *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		// *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
		// *******************************************************************************
		if (type.endsWith("0")) {
			restAPI.init("sandboxapp.cloopen.com", "8883");
		} else {
			restAPI.init("app.cloopen.com", "8883");
		}

		// ******************************注释*********************************************
		// *初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN *
		// *ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
		// *参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。 *
		// *******************************************************************************
		String ACOUNT_SID = null;
		String AUTH_TOKEN = null;
		ACOUNT_SID = "aaf98f894dd24f24014dd2633aa10034";
		AUTH_TOKEN = "d79532034eaa43e2b36785cb8b361c8e";
		restAPI.setAccount(ACOUNT_SID, AUTH_TOKEN);

		// ******************************注释*********************************************
		// *初始化应用ID *
		// *测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID *
		// *应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
		// *******************************************************************************
		String APP_ID = "8a48b5514dd25566014dd267b33d0025";
		restAPI.setAppId(APP_ID);

		// ******************************注释****************************************************************
		// *调用发送模板短信的接口发送短信 *
		// *参数顺序说明： *
		// *第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号 *
		// *第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。 *
		// *系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
		// *第三个参数是要替换的内容数组。 *
		// **************************************************************************************************

		// **************************************举例说明***********************************************************************
		// *假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为
		// *
		// *result = restAPI.sendTemplateSMS("13800000000","1" ,new
		// String[]{"6532","5"}); *
		// *则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入 *
		// *********************************************************************************************************************
		result = restAPI.sendTemplateSMS(phoneNum, SMSTemplateNum,
				new String[] { inviteCode, time + "分钟", applyIntroduction });
		IdentifyingCode ic = new IdentifyingCode();
		ic.setPhoneNumber(phoneNum);
		ic.setStateCode(result.get("statusCode").toString());
		if (result.get("statusMsg") != null) {
			ic.setErrorContext(result.get("statusMsg").toString());
		}
		ic.setIdentifyingCode(inviteCode);
		Date now = new Date();
		ic.setCreateTime(now);
		ic.setTime(time + "分钟");
		ic.setSmsType(SMSTemplateNum);
		ic.setType(1);
		return ic;
	}

	/**
	 * 随机生成6位数字验证码
	 */
	public static String returnIdentifyingCode() {
		StringBuffer sb = new StringBuffer();
		// 生成6位数字验证码 每位 0-9
		Random random = new Random();
		int count = 0;
		while (true) {
			if (count == 6) {
				break;
			}
			int s = random.nextInt(9) % (9 - 0 + 1) + 0;
			sb.append(s);
			count++;
		}
		return sb.toString();
	}
}
