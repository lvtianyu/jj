package com.liver_cloud.wechat.core;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.liver_cloud.wechat.common.Consts;
import com.liver_cloud.wechat.model.PayRequestParam;
import com.liver_cloud.wechat.model.PrepayInfo;
import com.liver_cloud.wechat.model.TOrderRecord;
import com.liver_cloud.wechat.model.TWxRecord;
import com.liver_cloud.wechat.util.HttpsUtil;
import com.liver_cloud.wechat.util.IDGenerator;
import com.liver_cloud.wechat.util.PropertyFactory;
import com.liver_cloud.wechat.util.WxUtil;
import com.liver_cloud.wechat.util.XMLUtil;

public class WechatMPPayCore {

	protected static Logger log = Logger.getLogger(WechatMPPayCore.class);

	/**
	 * 调用微信统一下单api，返回预支付信息
	 * 
	 * @return
	 */
	public static PrepayInfo getPrepayInfo(TOrderRecord order) {
		// body 商品描述 货币类型 total_fee 总金额 spbill_create_ip 终端ip
		String notify_url = PropertyFactory.getWxProperty(PropertyFactory.NOTIFYURL);// TODO
		log.info("接收支付通知地址  notify_url " + notify_url);
		String appid = PropertyFactory.getWxProperty(PropertyFactory.MpAppID);
		String mch_id = PropertyFactory.getWxProperty(PropertyFactory.MpMchId);
		String url = Consts.WxPayUnifiedorderUrl;
		String Key = PropertyFactory.getWxProperty(PropertyFactory.MpMchKey);
		// notify_url 通知地址 appid 公众账号id mch_id商户号 从配置文件中读取

		// 2.需要动态生成的数据
		// nonce-str 随机字符串 sign 签名 out_trade_no 订单号 trade_type 交易类型
		String nonce_str = IDGenerator.get32Random() + "";

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", appid);
		parameters.put("mch_id", mch_id);
		if (order.getDeviceInfo() != null) {
			parameters.put("device_info", order.getDeviceInfo());
		}
		parameters.put("nonce_str", nonce_str);
		parameters.put("body", order.getBody());
		if (order.getDetail() != null) {
			parameters.put("detail", order.getDetail());
		}
		if (order.getAttach() != null) {
			parameters.put("attach", order.getAttach());
		}
		parameters.put("out_trade_no", order.getOutTradeNo());
		parameters.put("spbill_create_ip", order.getSpbillCreateIp());
		parameters.put("total_fee", order.getTotalFee());
		parameters.put("trade_type", "JSAPI");
		// 添加附加数据,用户公众平台查账使用
		String time_start = WxUtil.getTimeDependWX() + "";
		parameters.put("time_start", time_start);
		String time_expire = WxUtil.getTimeDelayDependWX() + "";
		parameters.put("time_expire", time_expire);
		parameters.put("notify_url", notify_url);
		parameters.put("openid", order.getOpenid());

		String sign = WxUtil.createSign(Consts.CharsetName, parameters, Key);
		log.info("sign " + sign);
		parameters.put("sign", sign);

		// 3.将数据发送给微信获取和分析包装返回结果之后返回给请求端
		String reqTexts = XMLUtil.getRquestXmlString(parameters);
		log.info("请求地址为: " + url);
		try {
			log.info("request data 请求数据 " + reqTexts);
			// 最终调用统一下单Api的语句
			String resText = HttpsUtil.doPost(url, reqTexts);
			log.info("统一下单Api 响应结果为 : " + resText);
			// 对微信响应结果做解析并返回给AS
			Map<String, String> resultMap = XMLUtil.doXMLParse(resText);
			PrepayInfo prepayInfo = new PrepayInfo();
			prepayInfo.setPrepayInfo(resultMap);
			return prepayInfo;

		} catch (Exception e) {
			log.error("请求支付异常" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 生成公众号支付需要用到的参数，主要是预支付码，通过签名保证安全性
	 * 
	 * @param prepayInfo
	 *            通过调用getPrepayInfo方法获取
	 * @return
	 */
	public static PayRequestParam getPayRequestParam(PrepayInfo prepayInfo) {
		PayRequestParam payRequestParam = new PayRequestParam();
		payRequestParam.setAppId(prepayInfo.getAppId());
		payRequestParam.setTimeStamp(WxUtil.getTimeStamp());
		payRequestParam.setNonceStr(IDGenerator.get32Random() + "");
		payRequestParam.setPackage_("prepay_id=" + prepayInfo.getPrepayId());
		payRequestParam.setSignType("MD5");
		String paySign = WxUtil.createSign(Consts.CharsetName, payRequestParam.convertSortMap(),
				PropertyFactory.getWxProperty(PropertyFactory.MpMchKey));
		;
		payRequestParam.setPaySign(paySign);
		return payRequestParam;
	}

	/**
	 * 获取支付结果 更新本地记录 并通知微信 传入微信支付返回的结果字符串，返回处理微信结果后需要返回给微信的字符串
 	 *	TODO 怎么做并发控制呢
	 * 
	 * @param reqText
	 */
	public synchronized static String dealPayResult(String reqText, WechatPayResultCallback callback) {
		/*
		 * static方法可以直接类名加方法名调用，方法中无法使用this，所以它锁的不是this，而是类的Class对象，所以，static
		 * synchronized方法也相当于全局锁，相当于锁住了代码段。
		 * 支付完成后，微信会把相关支付结果和用户信息发送给商户，商户需要接收处理，并返回应答。
		 * 对后台通知交互时，如果微信收到商户的应答不是成功或超时，微信认为通知失败，微信会通过一定的策略定期重新发起通知，尽可能提高通知的成功率，
		 * 但微信不保证通知最终能成功。 （通知频率为15/15/30/180/1800/1800/1800/1800/3600，单位：秒）
		 * 注意：同样的通知可能会多次发送给商户系统。商户系统必须能够正确处理重复的通知。
		 * 推荐的做法是，当收到通知进行处理时，首先检查对应业务数据的状态，判断该通知是否已经处理过，如果没有处理过再进行处理，如果处理过直接返回结果成功。
		 * 在对业务数据进行状态检查和处理之前，要采用数据锁进行并发控制，以避免函数重入造成的数据混乱。
		 * 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
		 */
		log.info("微信通知支付结果\n" + reqText);
		// 返回给微信的Map
		Map<String, String> map = new HashMap<String, String>();
		// 1.获取支付结果,
		TWxRecord tWxRecord = new TWxRecord();
		tWxRecord.parseXMLText(reqText);
		// TODO 处理return_code失败的情况
		// 对微信返回信息生成签名与微信返回签名比对,验证消息来源
		String geneSign = WxUtil.createSign(Consts.CharsetName, tWxRecord.convertSortMap()
				, PropertyFactory.getWxProperty(PropertyFactory.MpMchKey));
		if(!geneSign.equals(tWxRecord.getSign())){
			// 签名失败
			log.info("geneSign:"+geneSign);
			log.info("wechatSign:"+tWxRecord.getSign());
			throw new IllegalArgumentException();
		}
		// 2. 失败,告知微信已收到消息 ,不更新order表
		if (!tWxRecord.getResultCode().equals("SUCCESS")) {
			// 请求关闭订单 需要:appid mch_id out_trade_no nonce_str sign
			// String url=PropertyFactory.getProperty("WXCLOSEORDER");
			log.info("微信通知支付失败,流程结束 ");
			map.put("return_code", "SUCCESS");
			map.put("return_msg", "OK");
			String rquestXmlString = XMLUtil.getRquestXmlString(map);
			log.info("返回给微信的信息 " + rquestXmlString);
			return rquestXmlString;
		}
		log.info("微信通知支付结果,交易号：" + tWxRecord.getOutTradeNo());
		// 3. 否则,校验当前订单是否已经完成,获取当前订单号的状态
		Boolean orderStatus = callback.getOrderStatus(tWxRecord.getOutTradeNo());
		// 3.1 如果订单已通知过，不必再更新数据库
		if (orderStatus) {
			// 已完成,通知微信不要再通知了
			log.info("订单已经完成,通知微信交易结束,已收到消息");
			map.put("return_code", "SUCCESS");
			map.put("return_msg", "OK");
			String rquestXmlString = XMLUtil.getRquestXmlString(map);
			log.info("返回给微信的信息 " + rquestXmlString);
			return rquestXmlString;
		}
		// 3.2 如果订单没有通知过，更新数据库
		callback.updateOrder(tWxRecord.getOutTradeNo(), tWxRecord);
		// 都没有问题,返回微信已接收支付成功结果
		map.put("return_code", "SUCCESS");
		map.put("return_msg", "OK");
		String rquestXmlString = XMLUtil.getRquestXmlString(map);
		log.info("返回给微信的信息 " + rquestXmlString);
		return rquestXmlString;

	}

}
