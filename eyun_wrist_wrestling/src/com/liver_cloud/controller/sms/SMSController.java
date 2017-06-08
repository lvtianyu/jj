package com.liver_cloud.controller.sms;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.liver_cloud.common.SMSUtil;
import com.liver_cloud.controller.base.BaseController;
import com.liver_cloud.model.EnumSMSTem;
import com.liver_cloud.model.IdentifyingCode;
import com.liver_cloud.util.EnumErrorCode;
import com.liver_cloud.util.PageData;

@Controller
public class SMSController extends BaseController{
	
	
	
	
	/**
	 * @param parameter
	 * @param appCode
	 * @param appKey
	 * @param accessToken
	 * @param type
	 * @param phoneNumber
	 * @param templateID
	 *            0：表示开发测试 1：表示生产环境使用
	 * @param sendType
	 *            0 手机号注册 1 手机号找回密码 2 余额支付使用绑定手机号验证码 3 设置支付密码 4 绑定银行卡
	 * @return
	 */
	/**
	 * 发送手机短信验证码
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sendSMSCaptchaCode")
	@ResponseBody
	public PageData sendSMSIdentifyingCode(HttpServletRequest request,
			HttpServletResponse response) {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			String phoneNumber = pd.getString("phoneNumber");
			String templateID = pd.getString("templateID");
			String time = "10";
			IdentifyingCode ic = null;
			ic = SMSUtil.sendSMSVerificationCode(phoneNumber, time,
					EnumSMSTem.IDENTIFYINGCODE.value.toString(), templateID);
			WebApplicationContext webApplicationContext = ContextLoader
					.getCurrentWebApplicationContext();
			ServletContext servletContext = webApplicationContext
					.getServletContext();
			if (ic != null) {
				if (ic.getStateCode().equals("000000")) {
					// 发送成功
					// 将数据保存到内存中
					servletContext.setAttribute(phoneNumber, ic);
					pd.clear();
					pd.put("result", "ok");
					pd.put("values", "");
					pd.put("errormsg", EnumErrorCode.CODE_000028.msg);
					pd.put("errorcode", EnumErrorCode.CODE_000028.code);
					return pd;
				}else {
					pd.clear();
					pd.put("result", "error");
					pd.put("values", "");
					pd.put("errormsg", EnumErrorCode.CODE_000029.msg);
					pd.put("errorcode", EnumErrorCode.CODE_000029.code);
					return pd;
				}							
		} else {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000029.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000029.code);
			return pd;
		}
		}catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000029.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000029.code);
			return pd;
		}					
	}
}


