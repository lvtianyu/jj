package com.liver_cloud.wechat.model;

import java.util.Map;

public class PrepayInfo {

	private String returnCode;
	private String returnMsg;
	private String appId;
	private String mchId;
	private String nonceStr;
	private String sign;
	private String resultCode;
	private String prepayId;
	private String tradeType;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public void setPrepayInfo(Map<String,String> resultMap){
		//遍历Map
		for(Map.Entry<String, String> entry:resultMap.entrySet()){
			if("return_code".equals(entry.getKey())){
				setReturnCode(entry.getValue());
			}else if("return_msg".equals(entry.getKey())){
				setReturnMsg(entry.getValue());
			}else if("appid".equals(entry.getKey())){
				setAppId(entry.getValue());
			}else if("mch_id".equals(entry.getKey())){
				setMchId(entry.getValue());
			}else if("nonce_str".equals(entry.getKey())){
				setNonceStr(entry.getValue());
			}else if("sign".equals(entry.getKey())){
				setSign(entry.getValue());
			}else if("result_code".equals(entry.getKey())){
				setResultCode(entry.getValue());
			}else if("prepay_id".equals(entry.getKey())){
				setPrepayId(entry.getValue());
			}else if("trade_type".equals(entry.getKey())){
				setTradeType(entry.getValue());
			}
			
		}
	}
	@Override
	public String toString() {
		return "PrepayInfo [returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", appId=" + appId + ", mchId="
				+ mchId + ", nonceStr=" + nonceStr + ", sign=" + sign + ", resultCode=" + resultCode + ", prepayId="
				+ prepayId + ", tradeType=" + tradeType + "]";
	}
	
	
	
	
}
