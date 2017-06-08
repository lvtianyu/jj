package com.liver_cloud.controller.wechat;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liver_cloud.entity.order.PayRecord;
import com.liver_cloud.entity.user.User;
import com.liver_cloud.service.game.GameService;
import com.liver_cloud.service.order.PayRecordService;
import com.liver_cloud.service.user.UserService2;
import com.liver_cloud.wechat.common.EnumErrorCode;
import com.liver_cloud.wechat.core.WechatMPLoginCore;
import com.liver_cloud.wechat.core.WechatMPPayCore;
import com.liver_cloud.wechat.core.WechatPayResultCallback;
import com.liver_cloud.wechat.http.ResponseUtils;
import com.liver_cloud.wechat.model.PayRequestParam;
import com.liver_cloud.wechat.model.PrepayInfo;
import com.liver_cloud.wechat.model.TOrderRecord;
import com.liver_cloud.wechat.model.TWxRecord;
import com.liver_cloud.wechat.model.UserInfo;
import com.liver_cloud.wechat.todo.WeChatUtil;
import com.liver_cloud.wechat.util.HttpsUtil;
import com.liver_cloud.wechat.util.IDGenerator;
import com.liver_cloud.wechat.util.IpUtil;
import com.liver_cloud.wechat.util.JsonUtil;
import com.liver_cloud.wechat.util.PropertyFactory;

/**
 * 注意： 修改回调相关url时，应同时修改wechat.properties配置文件
 * 
 * @author yajun
 *
 */
@Controller
@RequestMapping(value = "/wechat")
public class WechatController {

	protected static Logger Log = Logger.getLogger(WechatController.class);

	@Resource
	private UserService2 userService2;

	@Resource
	private PayRecordService orderService;

	@Resource
	private GameService gameService;
	
	@ResponseBody
	@RequestMapping(value = "/initJSSDK")
	public void initJSSDK(HttpServletRequest request, HttpServletResponse response) {
		Log.info("initJSSDK请求参数：" + request.getQueryString());
		JSONObject result = null;
		try {
//			jsapi_ticket是公众号用于调用微信JS接口的临时票据。正常情况下，jsapi_ticket的有效期为7200秒，通过access_token来获取
			WeChatUtil wx = new WeChatUtil();
			String ticket = wx.getTicket();

			String pageUrlParam = request.getParameter("pageUrl");
			String pageUrl = "";
			if (-1 != pageUrlParam.lastIndexOf("#")) {
				pageUrl = pageUrlParam.substring(0, pageUrlParam.lastIndexOf("#"));
			} else {
				pageUrl = pageUrlParam;
			}
			Map<String, String> map = WeChatUtil.sign(ticket, pageUrl);
			JSONObject valueJson = new JSONObject();
			valueJson.put("appid", PropertyFactory.getWxProperty(PropertyFactory.MpAppID));
			valueJson.put("signature", map.get("signature"));
			valueJson.put("noncestr", map.get("noncestr"));
			valueJson.put("timestamp", map.get("timestamp"));
			valueJson.put("url", map.get("url"));

			result = new JSONObject();
			result.put("result", "ok");
			result.put("values", valueJson);
			result.put("errormsg", "");
			result.put("errorcode", "000000");

/*			String callback = request.getParameter("callback");
			if (null != callback && !"".equals(callback)) {
				response.getWriter().append(callback + "(" + result.toJSONString() + ")");
			} else {
				response.getWriter().append(result.toJSONString());
			}*/
			ResponseUtils.renderJsJson(response, JsonUtil.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> map = new HashMap<String, Object>();
			Log.error(e.getMessage());
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", e.getMessage());
			map.put("errorcode", EnumErrorCode.CODE_000001.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		}

	}

	/**
	 * 微信登录第一步：重定向到微信登录页
	 * 
	 * @param request
	 * @param response
	 *            如果用户同意授权，页面将跳转至
	 *            redirect_uri/?code=CODE&state=STATE。若用户禁止授权，则重定向后不会带上code参数，
	 *            仅会带上state参数redirect_uri?state=STATE
	 *            code说明：code作为换取access_token的票据，每次用户授权带上的code将不一样，code只能使用一次，
	 *            5分钟未被使用自动过期。
	 * 
	 * @return 所需传递的参数 userType 用户类型 点赞者1、参赛者2、裁判3 （参见User类的常量）
	 */
	@ResponseBody
	@RequestMapping(value = "/redirectWechatLogin")
	private String redirectWechatLogin(HttpServletRequest request, HttpServletResponse response) {
		Log.info("wechatLoginCallback请求参数：" + request.getQueryString());
		try {
			String userType = request.getParameter("userType");
			String gameId = request.getParameter("g_id");
			HashMap<String, String> stateMap = new HashMap<>();
			stateMap.put("userType", userType);
			if(User.UserTypeSport==Integer.parseInt(userType)){
				stateMap.put("g_id", gameId);
			}
			String stateJson = JsonUtil.toJSONString(stateMap);
			String redirectWechatLogin = WechatMPLoginCore.getRedirectWechatLoginUrl(stateJson);
			response.sendRedirect(redirectWechatLogin);
			return null;
		} catch (Exception e) {
			Map<String, Object> map = new HashMap<String, Object>();
			Log.error(e.getMessage());
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", e.getMessage());
			map.put("errorcode", EnumErrorCode.CODE_000001.code);
			ResponseUtils.renderJsJson(response, JsonUtil.toJSONString(map));
			return null;
		}

	}

	/**
	 * 用户点击微信登录后会调这个接口 微信授权登录回调接口
	 * 在用户获取登录的url后，前端进行跳转，用户会看到一个绿色的是否允许公众号获取用户信息的界面，
	 * 用户点击“同意”后，微信回调的url，如果传递来有code参数，那么说明用户允许登录，如果没有则不允许。
	 * 拿到code后，用它去获取accessToken，然后再用token去获取用户的信息
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/wechatLoginCallback")
	public void wxloginCallback(HttpServletRequest request, HttpServletResponse response) {
		try {
			String code = request.getParameter("code");
			String state = request.getParameter("state");
			Log.info("wechatLoginCallback请求参数:" + request.getQueryString());
			boolean isAllowed = false;
			if (null != code && !"".equals(code)) {
				isAllowed = true;
			} else {
				isAllowed = false;
			}
			Log.info("wechatLoginCallback，Wechat isAllowed：" + isAllowed);
			// 判断是否登录成功
			StringBuffer redirectUrlSb = new StringBuffer();
			redirectUrlSb.append(PropertyFactory.getWxProperty(PropertyFactory.REDIRECT_FOREGROUND));
			if (isAllowed) {
				// 根据code获取accessToken
				String accessTokenJson = WechatMPLoginCore.getAccessToken(code);
				// 根据accessToken获取微信用户信息
				UserInfo userInfo = WechatMPLoginCore.getUserInfo(response, accessTokenJson);
				Log.info("wechatLoginCallback，Wechat userinfo：" + userInfo);
				// 将微信用户信息保存到数据库
				JSONObject stateObject = JSON.parseObject(state);
				String userType = stateObject.getString("userType");
				User user = userService2.saveOrUpdateUser(userInfo, Integer.parseInt(userType));
				redirectUrlSb.append("?openId=");
				redirectUrlSb.append(userInfo.getOpenid());
				redirectUrlSb.append("&nickName=");
				String nickNameEncode = URLEncoder.encode(userInfo.getNickname(), "utf-8");
				redirectUrlSb.append(nickNameEncode);
				redirectUrlSb.append("&headImageUrl=");
				redirectUrlSb.append(userInfo.getHeadimgurl());
				redirectUrlSb.append("&userId=");
				redirectUrlSb.append(user.getUserId());
				redirectUrlSb.append("&userType=");
				redirectUrlSb.append(userType);
				if(User.UserTypeSport==Integer.parseInt(userType)){
					String gameId = stateObject.getString("g_id");
					gameService.saveCompetitorId(user.getUserId(), gameId, userType);
					redirectUrlSb.append("&g_id=");
					redirectUrlSb.append(gameId);
				}
			} else {
				redirectUrlSb.append("?error=");
				redirectUrlSb.append("error");
			}
			Log.info("wechatLoginCallback，重定向到这个页面：" + redirectUrlSb.toString());
			response.sendRedirect(redirectUrlSb.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Map<String, Object> map = new HashMap<String, Object>();
			Log.error(e.getMessage());
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", e.getMessage());
			map.put("errorcode", EnumErrorCode.CODE_000001.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		}
	}
	/**
	 * 用户取消支付
	 * 
	 * @param request
	 * 参数 tradeNo 交易号
	 * @param response
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/payCancel")
	public void payCancel(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String queryString = request.getQueryString();
			Log.info("payCancel请求参数：" + queryString);
			String tradeNo=request.getParameter("tradeNo");
			orderService.updatePayRecordState(tradeNo, PayRecord.StatusPayCancel); 
			map.put("result", "ok");
			map.put("values", "取消成功");
			map.put("errormsg", "ok");
			map.put("errorcode", EnumErrorCode.CODE_000000.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", e.getMessage());
			map.put("errorcode", EnumErrorCode.CODE_000001.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		}
	}

	/**
	 * 扳手腕投注一元接口，调起微信统一下单API，获取预支付码接口
	 * @param request
	 * @param response
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "/payForGame")
	public void payForGame(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String queryString = request.getQueryString();
			Log.info("payForGame请求参数：" + queryString);
			// TODO 校验是否是int类型
			String orderId = request.getParameter("gameId");
			//如果游戏已开始，不能进行支付
			boolean gameBegin = gameService.checkGameBeginStatus(orderId);
			if(gameBegin){
				map.put("result", "error");
				map.put("values", EnumErrorCode.CODE_000146.value);
				map.put("errormsg", EnumErrorCode.CODE_000146.msg);
				map.put("errorcode", EnumErrorCode.CODE_000146.code);
				ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
				return;
			}
			// TODO 校验是否是int类型
			String paraUserId = request.getParameter("userId");
			// TODO 优先使用前台传递的ip，还是优先使用后台传递的ip
			String spbillCreateIp = request.getParameter("spbillCreateIp");
			String paraSportUserId = request.getParameter("sportUserId");
//			Integer gamePayStatus = orderService.queryGamePayStatus(orderId, paraUserId);
			//如果是支付成功或者正在支付，不能继续支付，只有支付失败或者未支付时才可以进行支付
			if(orderService.isPayingOrPayed(orderId, paraUserId)){
				map.put("result", "error");
				map.put("values", EnumErrorCode.CODE_000147.value);
				map.put("errormsg", EnumErrorCode.CODE_000147.msg);
				map.put("errorcode", EnumErrorCode.CODE_000147.code);
				ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
				return;
			}
			String out_trade_no = IDGenerator.get32Random() + "";
			//
			PayRecord order = new PayRecord();
			order.setBody("掰手腕竞猜");
			order.setTotalFee(Integer.parseInt(PropertyFactory.getWxProperty(PropertyFactory.PAY_FOR_GAME_MONEY)));// 单位为分
			order.setUserId(Integer.parseInt(paraUserId));
			String remoteIpAddr = IpUtil.getRemoteIpAddr(request);
			Log.info("payForGame，支付客户端ip地址：" + remoteIpAddr);
			order.setDeviceInfo("WEB");
			order.setSpbillCreateIp(remoteIpAddr);
			order.setOrderId(Integer.parseInt(orderId));
			order.setPayType(PayRecord.PayTypeSupportGame);
			order.setStatus(PayRecord.StatusUnPay);
			order.setWechatStatus(false);
			order.setCompetitorId(Integer.parseInt(paraSportUserId));
			order.setTradeNo(out_trade_no);
			order.setTimeStart(new Date().getTime());
			// **核心语句**
			PayRequestParam payRequestParam = prepay(order, null);
			map.put("result", "ok");
			map.put("values", payRequestParam);
			map.put("errormsg", EnumErrorCode.CODE_000000.msg);
			map.put("errorcode", EnumErrorCode.CODE_000000.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> map = new HashMap<String, Object>();
			Log.error(e.getMessage());
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", e.getMessage());
			map.put("errorcode", EnumErrorCode.CODE_000001.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		}
	}

	/**
	 * 调起微信统一下单Api PayRecord需要设置的属性 类型 名称 是否必填 描述 String body, 是 商品名称 Integer
	 * totalFee, 是 总金额 String userId, 是 用户标识 String sportUserId, 是 支持哪个参赛者
	 * String orderId 是 如果为参与某个游戏，那么orderId就是gameId String payType 是
	 * 如果为支持某个游戏，传1 String spbillCreateIp, 是 客户端ip String deviceInfo, 否 设备号
	 * String detail, 否 商品详情 String attach 否 附加字段
	 * 
	 * @param payRecord
	 * @param attach
	 * @return
	 */
	private PayRequestParam prepay(PayRecord payRecord, String attach) {
		orderService.savePayRecord(payRecord);
		// ---------------调起微信服务器，获取支付参数，返回给前台
		// 测试语句
		// TOrderRecord order=new TOrderRecord(out_trade_no,"支付测试", 1,
		// "ohS1ov0qwYjkemlkk8cDWrJ-uW80","192.168.0.1", null,null,null);
		// 保存PayRecord到数据库
		String openId = userService2.getWechatOpenId(payRecord.getUserId());
		TOrderRecord tOrder = new TOrderRecord(payRecord.getTradeNo(), payRecord.getBody(), payRecord.getTotalFee(),
				openId, payRecord.getSpbillCreateIp(), payRecord.getDeviceInfo(), payRecord.getDetail(), attach);
		PrepayInfo prepayInfo = WechatMPPayCore.getPrepayInfo(tOrder);
		PayRequestParam payRequestParam = WechatMPPayCore.getPayRequestParam(prepayInfo);
		payRequestParam.setTradeNo(payRecord.getTradeNo());
		return payRequestParam;
	}

	/**
	 * 微信服务器确认支付成功后调起这个接口 这个接口和登录回调还不一样
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/wechatPayCallback")
	public void wechatPayCallback(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 获取请求数据
			String reqText = HttpsUtil.getJsonFromRequest(request);

			// 测试代码
			/*
			 * InputStream payresultStream =
			 * WechatPayCallbackServlet.class.getClassLoader().
			 * getResourceAsStream("payresult.xml"); String reqText =
			 * StringUtil.toConvertString(payresultStream);
			 */
			WechatPayResultCallback callback = new WechatPayResultCallback() {

				@Override
				public void updateOrder(String tradeId, TWxRecord record) {
					Log.info("updateOrder");
					// TODO 是否应该校验金额呢？
					PayRecord payRecord = new PayRecord();
					//tradeType bankType transactionId errCode errCodeDes TODO 编写Dao层
					payRecord.setTradeNo(tradeId);
					payRecord.setTradeType(record.getTradeType());
					payRecord.setBankType(record.getBankType());
					payRecord.setTransactionId(record.getTransactionId());
					payRecord.setWechatStatus(true);
					payRecord.setTotalFee(Integer.parseInt(record.getTotalFee()));
					if("SUCCESS".equals(record.getResultCode())){
						payRecord.setStatus(PayRecord.StatusPaySuccess);
					}else if("FAIL".equals(record.getResultCode())){
						payRecord.setStatus(PayRecord.StatusPayFail);
						payRecord.setErrCode(record.getErrCode());
						payRecord.setErrCodeDes(record.getErrCodeDes());
					}
					orderService.updatePayRecordByTradeNo(payRecord);
				}

				@Override
				public Boolean getOrderStatus(String tradeId) {
					Log.info("getOrderStatus");
					Boolean wechatStatus = orderService.getPayRecordWechatStatus(tradeId);
					return wechatStatus;
				}
			};
			String payResult = WechatMPPayCore.dealPayResult(reqText, callback);
			// 处理完成，同步返回给微信
			HttpsUtil.sendAppMessage(payResult, response);
		}catch(IllegalArgumentException e){
			//收到签名错误的微信支付通知
			Log.warn("收到签名错误的微信支付通知");
		}catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 由于规则改变，该接口已废弃
	 * 前端查询游戏支付是否真正成功 payStatus 1 未支付，2 支付成功，3 支付失败
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping(value = "/queryGamePayStatus")
	public void queryGamePayStatus(HttpServletRequest request, HttpServletResponse response) {
		try {
			String gameId = request.getParameter("gameId");
			String userId = request.getParameter("userId");
			Integer payStatus = 0;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("result", "ok");
			map.put("values", payStatus);
			map.put("errormsg", EnumErrorCode.CODE_000000.msg);
			map.put("errorcode", EnumErrorCode.CODE_000000.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> map = new HashMap<String, Object>();
			Log.error(e.getMessage());
			map.put("result", "error");
			map.put("values", "");
			map.put("errormsg", e.getMessage());
			map.put("errorcode", EnumErrorCode.CODE_000001.code);
			ResponseUtils.renderJson(response, JsonUtil.toJSONString(map));
		}

	}

}