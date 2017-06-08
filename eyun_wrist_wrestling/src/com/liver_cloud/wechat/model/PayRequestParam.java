package com.liver_cloud.wechat.model;

import java.util.SortedMap;
import java.util.TreeMap;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 调起微信支付用到的参数，由服务器返回给客户端
 * @author yajun
 *
 */
public class PayRequestParam {

	private String appId;
	private String timeStamp;
	private String nonceStr;
	@JSONField(name="package")
	private String package_;
	private String signType;
	private String paySign;
	private String tradeNo;//交易号
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getPackage_() {
		return package_;
	}
	public void setPackage_(String package_) {
		this.package_ = package_;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	
	
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public SortedMap<Object, Object> convertSortMap(){
		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appId", getAppId());
		parameters.put("timeStamp", getTimeStamp());
		parameters.put("nonceStr", getNonceStr());
		parameters.put("package", getPackage_());
		parameters.put("signType", getSignType());
		
		return parameters;
	}
	@Override
	public String toString() {
		return "PayRequestParam [appId=" + appId + ", timeStamp=" + timeStamp + ", nonceStr=" + nonceStr + ", package_="
				+ package_ + ", signType=" + signType + ", paySign=" + paySign + "]";
	}
	
	
	
	
}
