package com.liver_cloud.wechat.test;

import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import com.liver_cloud.wechat.common.Consts;
import com.liver_cloud.wechat.core.WechatMPPayCore;
import com.liver_cloud.wechat.model.PayRequestParam;
import com.liver_cloud.wechat.model.PrepayInfo;
import com.liver_cloud.wechat.model.TOrderRecord;
import com.liver_cloud.wechat.model.TWxRecord;
import com.liver_cloud.wechat.util.IDGenerator;
import com.liver_cloud.wechat.util.PropertyFactory;
import com.liver_cloud.wechat.util.StringUtil;
import com.liver_cloud.wechat.util.WxUtil;
import com.liver_cloud.wechat.util.XMLUtil;

public class WechatMPPayCoreTest {

	@Test
	public void testGetPrepayInfo() {
		String out_trade_no = IDGenerator.get32Random() + "";
		TOrderRecord order = new TOrderRecord(out_trade_no, "支付测试", 1, "ohS1ov0qwYjkemlkk8cDWrJ-uW80", "192.168.0.1",
				null, null, null);

		PrepayInfo prepayInfo = WechatMPPayCore.getPrepayInfo(order);

		System.out.println("prepayInfo：" + prepayInfo);

	}

	@Test
	public void testGetPayRequestParam() {

		PrepayInfo prepayInfo = new PrepayInfo();
		prepayInfo.setAppId("wxe9fe4bbf81854ad0");
		prepayInfo.setPrepayId("wx20160706162904163a10b9fb0812395041");

		PayRequestParam payRequestParam = WechatMPPayCore.getPayRequestParam(prepayInfo);
		SortedMap<Object, Object> sortMap = payRequestParam.convertSortMap();
		sortMap.put("sign", payRequestParam.getPaySign());
		String paramXmlString = XMLUtil.getRquestXmlString(sortMap);

		System.out.println(paramXmlString);
		System.out.println("payRequestParam" + payRequestParam);

	}

	@Test
	public void testRecordConvert() {
		TWxRecord tWxRecord = new TWxRecord();
		tWxRecord.setAppid("appidvalue");
		tWxRecord.setAttach("attachvalue");
		tWxRecord.setErrCode("errCodeValue");
		tWxRecord.setCouponFee_$n("1");
		SortedMap<Object, Object> sortMap = tWxRecord.convertSortMap();
		for (Map.Entry<Object, Object> entry : sortMap.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

	@Test
	public void testNotifySign() {
		String reqText = StringUtil
				.toConvertString(WechatMPPayCoreTest.class.getClassLoader().getResourceAsStream("notify.xml"));
		// 1.获取支付结果,
		TWxRecord tWxRecord = new TWxRecord();
		tWxRecord.parseXMLText(reqText);
		String geneSign = WxUtil.createSign(Consts.CharsetName, tWxRecord.convertSortMap(),
				PropertyFactory.getWxProperty(PropertyFactory.MpMchKey));
		if (!geneSign.equals(tWxRecord.getSign())) {
			System.out.println("签名不同");
		} else {
			System.out.println("签名相同");
		}
	}
}
