package com.liver_cloud.dao.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.liver_cloud.entity.order.PayRecord;

public interface PayRecordDao {

	public void insertPayRecord(PayRecord order);

	public void updatePayRecordByTradeNo(PayRecord payRecord);

	public PayRecord selectPayRecordByTradeNo(String tradeNo);

	public List<PayRecord> selectPayStatusByOrderIdAndUserId(@Param(value="orderId")String orderId, @Param(value="userId")String userId);
	
}
