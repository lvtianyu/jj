package com.liver_cloud.service.order.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liver_cloud.dao.order.PayRecordDao;
import com.liver_cloud.entity.order.PayRecord;
import com.liver_cloud.service.order.PayRecordService;


@Service(value="payRecordService")
public class PayRecordServiceImpl implements PayRecordService {
	
	@Resource
	private PayRecordDao payRecordDao;

	@Override
	public void savePayRecord(PayRecord payRecord) {
		payRecordDao.insertPayRecord(payRecord);
	}

	@Override
	public void updatePayRecordState(String tradeNo,Integer status) {
		PayRecord payRecord = new PayRecord();
		payRecord.setTradeNo(tradeNo);
		payRecord.setStatus(status);
		payRecordDao.updatePayRecordByTradeNo(payRecord);
	}

	@Override
	public Boolean getPayRecordWechatStatus(String tradeNo) {
		PayRecord payRecord=payRecordDao.selectPayRecordByTradeNo(tradeNo);
		Boolean wechatStatus = payRecord.getWechatStatus();
		return wechatStatus;
	}

	@Override
	public void receiveWechatNotify(String tradeNo,Integer status) {
		PayRecord payRecord = new PayRecord();
		payRecord.setTradeNo(tradeNo);
		payRecord.setWechatStatus(true);
		payRecord.setStatus(status);
		payRecordDao.updatePayRecordByTradeNo(payRecord);
	}

	/*@Override
	public Integer queryGamePayStatus(String gameId, String userId) {
		List<PayRecord> payRecordList=payRecordDao.selectPayStatusByOrderIdAndUserId(gameId,userId);
		for(PayRecord payRecord:payRecordList){
			
		}
		return payStatus;
	}*/
	
	public boolean isPayingOrPayed(String gameId,String userId){
		List<PayRecord> payRecordList=payRecordDao.selectPayStatusByOrderIdAndUserId(gameId,userId);
		for(PayRecord payRecord:payRecordList){
			if(payRecord.getStatus()==PayRecord.StatusUnPay||payRecord.getStatus()==PayRecord.StatusPaySuccess){
				return true;
			}
		}
		return false;
	}

	@Override
	public void updatePayRecordByTradeNo(PayRecord payRecord) {
		payRecordDao.updatePayRecordByTradeNo(payRecord); 
	}


}
