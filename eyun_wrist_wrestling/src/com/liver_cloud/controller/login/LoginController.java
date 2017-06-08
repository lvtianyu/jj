package com.liver_cloud.controller.login;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.liver_cloud.controller.base.BaseController;
import com.liver_cloud.model.IdentifyingCode;
import com.liver_cloud.model.ServiceResult;
import com.liver_cloud.service.appointment_date.Appointment_DateService;
import com.liver_cloud.service.appointment_info.Appointment_InfoService;
import com.liver_cloud.service.login.LoginService;
import com.liver_cloud.service.referee.RefereeService;
import com.liver_cloud.util.DateUtil;
import com.liver_cloud.util.EnumErrorCode;
import com.liver_cloud.util.PageData;

@Controller
@RequestMapping(value = "/referee")
public class LoginController extends BaseController {

	@Resource(name = "refereeService")
	private RefereeService refereeService;

	@Resource(name = "appointment_DateService")
	private Appointment_DateService appointment_DateService;

	@Resource(name = "appointment_InfoService")
	private Appointment_InfoService appointment_InfoService;

	@Resource(name = "loginService")
	private LoginService loginService;

	/**
	 * 
	 * @Title:
	 * @Description: 裁判由公众号进入裁判登陆页面
	 * @version
	 * @author lifutao
	 * @throws Exception
	 * @date
	 */
	@ResponseBody
	@RequestMapping(value = "/refereeLogin")
	public PageData refereeLogin() throws Exception {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData result = loginService.getLoginResult(pd);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000031.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000031.code);
			return pd;
		}
	}

	/**
	 * 点击下一步时调用的接口
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/resetPasswordCheckPhoneNumber")
	@ResponseBody
	public PageData resetPasswordCheckPhoneNumber() {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			// 校验手机号是否注册
			// 通过手机号查询用户是否注册
			PageData referee = refereeService.selectRefeByMobile(pd);
			if (referee == null) {
				pd.clear();
				pd.put("result", "error");
				pd.put("errormsg", EnumErrorCode.CODE_000025.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000025.code);
				return pd;
			}
			// 验证码校验
			ServiceResult<String> confirmationResult;
			if ((confirmationResult = confirmationPhoneNumber(
					pd.getString("r_phone"), pd.getString("code"))) != null) {
				pd.clear();
				pd.put("result", "error");
				pd.put("errormsg", EnumErrorCode.CODE_000030.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000030.code);
				return pd;
			}
			// 校验完成
			pd.clear();
			pd.put("result", "ok");
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;

		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000032.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000032.code);
			return pd;
		}

	}

	/**
	 * 重置密码接口 TODO
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/resetPassword")
	@ResponseBody
	public PageData resetPassword() {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			// 通过手机号查询用户是否注册
			PageData referee = refereeService.selectRefeByMobile(pd);
			if (referee == null) {
				pd.clear();
				pd.put("result", "error");
				pd.put("errormsg", EnumErrorCode.CODE_000025.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000025.code);
				return pd;
			}
			// 密码重置
			if(pd.get("r_password").toString().equals(pd.get("reset_password").toString())){
				refereeService.updatePassword(pd);
				pd.clear();
				pd.put("result", "ok");
				pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000000.code);
				return pd;
			}else{
				pd.clear();
				pd.put("result", "error");
				pd.put("errormsg", EnumErrorCode.CODE_000036.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000036.code);
				return pd;
			}
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000033.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000033.code);
			return pd;
		}

	}

	/**
	 * 参赛者预约报名接口 TODO
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/sighUpForTheGame")
	@ResponseBody
	public PageData sighUpForTheGame() throws Exception {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			// 将数据存入到date和info表
			PageData result = loginService.insertInfo(pd);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000034.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000034.code);
			return pd;
		}

	}

	private static ServletContext getServletContext() {
		WebApplicationContext webApplicationContext = ContextLoader
				.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext
				.getServletContext();
		return servletContext;
	}

	/**
	 * 使用短信验证码验证手机号 如果返回null说明验证通过，否则为错误结果 
	 *
	 * @param phoneNumber
	 */
	public static ServiceResult<String> confirmationPhoneNumber(
			String phoneNumber, String code) {
		ServiceResult<String> response = new ServiceResult<>();
		// 开始校验手机验证码
		IdentifyingCode ic = (IdentifyingCode) getServletContext()
				.getAttribute(phoneNumber);
		if (ic != null) {
			Date now = new Date();
			int compareMinute = DateUtil.getCompareMinute(now,
					ic.getCreateTime());
			if (compareMinute > -10) {
				// 有效，判断验证码是否正确
				if (ic.getIdentifyingCode().equals(code)) {
					// 有效验证码
					// 从内存中移除
					getServletContext().removeAttribute(phoneNumber);
				} else {
					// 无效验证码
					response.setEnumError(EnumErrorCode.CODE_000030);
					return response;
				}
			} else {
				// 无效
				response.setEnumError(EnumErrorCode.CODE_000030);
				return response;
			}
		} else {
			// 无效
			response.setEnumError(EnumErrorCode.CODE_000030);
			return response;
		}
		return null;

	}

}
