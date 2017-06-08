package com.liver_cloud.wechat.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.liver_cloud.wechat.common.EnumErrorCode;
import com.liver_cloud.wechat.core.WechatMPLoginCore;
import com.liver_cloud.wechat.core.WechatMPPayCore;
import com.liver_cloud.wechat.http.ResponseUtils;
import com.liver_cloud.wechat.model.PayRequestParam;
import com.liver_cloud.wechat.model.PrepayInfo;
import com.liver_cloud.wechat.model.TOrderRecord;
import com.liver_cloud.wechat.util.IDGenerator;
import com.liver_cloud.wechat.util.JsonUtil;

/**
 * Servlet implementation class WeChatServlet
 */
@WebServlet("/WeChatServlet")
public class WeChatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WeChatServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String _method = request.getParameter("action");
		if (null != _method && !"".equals(_method.trim())) {
			if ("redirectWechatLogin".equalsIgnoreCase(_method.trim())) {
				redirectWechatLogin(request, response);
			} else if ("prepay".equalsIgnoreCase(_method.trim())) {
				prepay(request,response);
			} else if ("doSignJs".equalsIgnoreCase(_method.trim())) {
			} else if ("loginFromMobile".equalsIgnoreCase(_method.trim())) {
			} else if ("loginFromDesktop".equalsIgnoreCase(_method.trim())) {
			} else if ("initJSSDK".equalsIgnoreCase(_method.trim())) {
			} else if ("requestWxPay".equalsIgnoreCase(_method.trim())) {
			} else if ("game/saveImageToDisk.do".equalsIgnoreCase(_method.trim())) {
			} else if ("test".equalsIgnoreCase(_method.trim())) {
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * 微信登录第一步：用户同意授权，获取code
	 * 
	 * @param request
	 * @param response
	 *            如果用户同意授权，页面将跳转至
	 *            redirect_uri/?code=CODE&state=STATE。若用户禁止授权，则重定向后不会带上code参数，
	 *            仅会带上state参数redirect_uri?state=STATE
	 *            code说明：code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，
	 *            5分钟未被使用自动过期。
	 * @return
	 */
	public String redirectWechatLogin(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String redirectWechatLogin = WechatMPLoginCore.getRedirectWechatLoginUrl("state");
			response.sendRedirect(redirectWechatLogin);
			return null;
		} catch (Exception e) {
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", EnumErrorCode.CODE_000011.msg);
			map.put("errorcode", EnumErrorCode.CODE_000011.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
			return null;
		}

	}
	
	/**
	 * 预支付
	 * 在服务器端生成订单
	 * 调用微信统一下单Api
	 * 返回微信支付需要的参数
	 * @param request
	 * @param response
	 * @return
	 */
	public void prepay(HttpServletRequest request, HttpServletResponse response){
		String out_trade_no = IDGenerator.get32Random() + "";
		TOrderRecord order=new TOrderRecord(out_trade_no,"支付测试", 1, "ohS1ov0qwYjkemlkk8cDWrJ-uW80","192.168.0.1", null,null,null);
		PrepayInfo prepayInfo = WechatMPPayCore.getPrepayInfo(order);
		PayRequestParam payRequestParam = WechatMPPayCore.getPayRequestParam(prepayInfo);
		ResponseUtils.renderJson(response, JsonUtil.toJSONString(payRequestParam));
	}

}
