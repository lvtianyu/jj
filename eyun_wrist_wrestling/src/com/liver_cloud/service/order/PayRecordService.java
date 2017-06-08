package com.liver_cloud.service.order;

import com.liver_cloud.entity.order.PayRecord;

/**
 * 参与用户支持时需要付款，付款会生成订单
 * @author yajun
 *
 */
public interface PayRecordService {

	/**
	 * 根据前台传递的数据，生成订单，保存到数据库
	 * @param order
	 */
	public void savePayRecord(PayRecord order);
	
	/**
	 * 根据支付结果，保存支付状态，有三种状态 未付款、付款成功、付款失败
	 * @param orderState
	 */
	public void updatePayRecordState(String tradeNo,Integer state);
	
	/**
	 * 根据订单编号，查询订单中微信通知状态
	 * @param tradeId
	 */
	public Boolean getPayRecordWechatStatus(String tradeId);
	
	/**
	 * 根据订单编号，更新订单微信通知状态和支付状态
	 * 场景：接收到微信通知，根据通知状态将支付状态改为支付成功或支付失败，将微信通知状态改为已通知
	 */
	public void receiveWechatNotify(String tradeId,Integer status);

	/**
	 * 查询支付状态
	 * @param gameId
	 * @param userId
	 */
//	public Integer queryGamePayStatus(String gameId, String userId); 
	
	/**
	 * 更新PayRecord
	 * @param payRecord
	 */
	public void updatePayRecordByTradeNo(PayRecord payRecord);
	
	/**
	 * 查询游戏是否已经支付过或者正在支付
	 * @param gameId
	 * @param userId
	 * @return
	 */
	public boolean isPayingOrPayed(String gameId,String userId);
	
	
}
