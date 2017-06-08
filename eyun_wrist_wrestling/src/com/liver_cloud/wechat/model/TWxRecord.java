package com.liver_cloud.wechat.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.persistence.PersistenceProperty;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.liver_cloud.wechat.core.WechatMPPayCore;
import com.liver_cloud.wechat.util.CamelUnderlineUtil;
import com.liver_cloud.wechat.util.PropertyUtil;
import com.liver_cloud.wechat.util.XMLUtil;

/**
 * 微信支付成功后返回对象
 * 参考微信商户平台支付结果通知Api
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 * @author yajun
 *
 */
public class TWxRecord {
	
	protected static Logger log = Logger.getLogger(WechatMPPayCore.class);
	
	/**
	 * 返回状态码，为通信标识，非业务标识
	 */
	private String returnCode;
	/**
	 * 返回状态值，为通信标识，非业务标识
	 */
	private String returnMsg;
	private String appid;
	
	private String mchId;
	
//    private String id;

	private String deviceInfo;
	private String nonceStr;



    private String sign;
    /**
     * 业务结果
     * SUCCESS/FAIL
     */
    private String resultCode;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    private String openid;

    /**
     * 是否关注公众账号
     */
    private String isSubscribe;

    /**
     * 交易类型 JSAPI、NATIVE、APP
     */
    private String tradeType;

    private String bankType;

    private String totalFee;
    
    /**
     * 应结订单金额
     */
    private String settlementTotalFee;

    private String feeType;
    
    /**
     * 现金支付金额
     */
    private String cashFee;

    /**
     * 现金支付货币类型
     * 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY
     */
    private String cashFeeType;

    /**
     * 代金券金额
     */
    private String couponFee;

    private String couponCount;

    private String couponId_$n;

    private String couponFee_$n;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    private String attach;

    /**
     * 支付完成时间
     */
    private String timeEnd;
    
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

	public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo == null ? null : deviceInfo.trim();
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr == null ? null : nonceStr.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode == null ? null : resultCode.trim();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(String isSubscribe) {
        this.isSubscribe = isSubscribe == null ? null : isSubscribe.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType == null ? null : cashFeeType.trim();
    }

    public String getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(String couponFee) {
        this.couponFee = couponFee;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd == null ? null : timeEnd.trim();
    }

    public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(String settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}

	public String getCouponId_$n() {
		return couponId_$n;
	}

	public void setCouponId_$n(String couponId_$n) {
		this.couponId_$n = couponId_$n;
	}

	public String getCouponFee_$n() {
		return couponFee_$n;
	}

	public void setCouponFee_$n(String couponFee_$n) {
		this.couponFee_$n = couponFee_$n;
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
				if("coupon_id_$n".equals(key)){
					setCouponId_$n(value);
				}else if("coupon_fee_$n".equals(key)){
					setCouponFee_$n(value);
				}else{
					PropertyUtil.setProperty(this, CamelUnderlineUtil.underlineToCamel(key), value);
				}
				
			}
		} catch (Exception e) {
			log.error("转换微信通知结果时异常"+e.getMessage(),e);
		}
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