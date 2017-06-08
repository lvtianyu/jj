package com.liver_cloud.wechat.core;

import com.liver_cloud.wechat.model.TWxRecord;

public interface WechatPayResultCallback {
	
	/**
	 * 根据orderId，查询对应order是否已接收过微信通知
	 * @param orderId
	 * @return
	 */
	public Boolean getOrderStatus(String orderId);
	
	/**
	 * 更新数据库中对应order的微信通知状态
	 * @param orderId
	 */
	public void updateOrder(String orderId,TWxRecord record);

}
