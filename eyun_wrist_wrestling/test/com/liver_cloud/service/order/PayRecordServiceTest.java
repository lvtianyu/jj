package com.liver_cloud.service.order;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liver_cloud.entity.order.PayRecord;

public class PayRecordServiceTest {

	protected static Logger Log = Logger.getLogger(PayRecordServiceTest.class);

	private static PayRecordService payRecordService;

	@BeforeClass
	public static void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/ApplicationContext.xml");
		payRecordService = (PayRecordService) context.getBean("payRecordService");
	}

	@Test
	public void testSavePayRecord() {
		PayRecord payRecord=new PayRecord();
		payRecord.setBody("黄金赞");
		payRecord.setOrderId(1);
		payRecord.setPayType(1);
		payRecord.setWechatStatus(false);
		payRecordService.savePayRecord(payRecord);
	}
	
	@Test
	public void testReceiveWechatNotify(){
		
		payRecordService.receiveWechatNotify("1", 1);
	}
	
	@Test
	public void testGetPayRecordWechatStatus(){
		Boolean payRecordWechatStatus = payRecordService.getPayRecordWechatStatus("1");
		Log.info("payRecordWechatStatus："+payRecordWechatStatus);
	}
	
	@Test
	public void testQueryGamePayStatus(){
		String gameId="1";
		String userId="4";
//		Integer payStatus = payRecordService.queryGamePayStatus(gameId, userId);
//		Log.info("payStatus："+payStatus);
	}
	@Test
	public void testQueryGamePayStatus2(){
//		Integer gamePayStatus = payRecordService.queryGamePayStatus("1", "4");
//		//如果是支付成功或者正在支付，不能继续支付，只有支付失败或者未支付时才可以进行支付
//		if(gamePayStatus==PayRecord.StatusPaySuccess||gamePayStatus==PayRecord.StatusUnPay){
//			Log.info("不能重复投注一场比赛");
//		}
	}

}
