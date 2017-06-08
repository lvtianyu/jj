package com.liver_cloud.wechat.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.liver_cloud.wechat.core.WechatMPPayCore;
import com.liver_cloud.wechat.util.CamelUnderlineUtil;
import com.liver_cloud.wechat.util.PropertyUtil;
import com.liver_cloud.wechat.util.XMLUtil;

/**
 * 发送红包返回值
 * TODO 待完善
 * https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_5
 * @author yajun
 *
 */
public class RedPacketResult {
	
	protected static Logger log = Logger.getLogger(WechatMPPayCore.class);

	private String returnCode;
	private String returnMsg;
	private String sign;
	private String resultCode;
	private String errCode;
	private String errCodeDes;
	private String mchBillno;
	private String mchId;
	private String wxappid;
	private String reOpenid;
	private String totalAmount;
	private String sendTime;//发送成功时间
	private String sendListid;//微信单号
	
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

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}
	
	public String getMchBillno() {
		return mchBillno;
	}

	public void setMchBillno(String mchBillno) {
		this.mchBillno = mchBillno;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getWxappid() {
		return wxappid;
	}

	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}

	public String getReOpenid() {
		return reOpenid;
	}

	public void setReOpenid(String reOpenid) {
		this.reOpenid = reOpenid;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendListid() {
		return sendListid;
	}

	public void setSendListid(String sendListid) {
		this.sendListid = sendListid;
	}

	/**
     * 解析xml格式的支付结果对象为java格式的支付结果对象
     * 根据属性名，设置属性值
     */
    public void parseXMLText(String wxPayResultXMLText){
		try {
			Map<String,String> resultMap = XMLUtil.doXMLParse(wxPayResultXMLText);
			for(Map.Entry<String,String> entrySet:resultMap.entrySet()){
				String value = entrySet.getValue();
				String key = entrySet.getKey();
				
				PropertyUtil.setProperty(this, CamelUnderlineUtil.underlineToCamel(key), value);
				
			}
		} catch (Exception e) {
			log.error("转换微信通知结果时异常"+e.getMessage(),e);
		}
    }
    
    @Override
	public String toString() {
		return "RedPacketResult [returnCode=" + returnCode + ", returnMsg=" + returnMsg + ", sign=" + sign
				+ ", resultCode=" + resultCode + ", errCode=" + errCode + ", errCodeDes=" + errCodeDes + ", mchBillno="
				+ mchBillno + ", mchId=" + mchId + ", wxappid=" + wxappid + ", reOpenid=" + reOpenid + ", totalAmount="
				+ totalAmount + ", sendTime=" + sendTime + ", sendListid=" + sendListid + "]";
	}

	/**
     * 签名时用到
     * 1.遍历所有get方法，判断返回值是否为空
     * 2.如果返回值不为空，以属性名为key，属性值为value，put到SortedMap中
     * @return
     */
	public SortedMap<Object, Object> convertSortMap(){
		SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : pds) {
				Object value = pd.getReadMethod().invoke(this);
				String name=pd.getName();
				if (value != null&&!name.equals("class")&&!name.equals("sign")) {
					sortedMap.put(CamelUnderlineUtil.camelToUnderline(name), value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sortedMap;
	}
	
	

}
