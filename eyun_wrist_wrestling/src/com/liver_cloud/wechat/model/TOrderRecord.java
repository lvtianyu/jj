package com.liver_cloud.wechat.model;
/**
 * 统一下单请求参数用到
 * @author yajun
 *
 */
public class TOrderRecord {
	
    private String oid;

    private String deviceInfo;//设备号

    private String nonceStr;//随机字符串

    private String sign;//签名

    private String body;//商品描述

    private String detail;//商品详情

    private String attach;//附加数据

    private String outTradeNo;//商户订单号

    private String feeType;//货币类型

    private Integer totalFee;//总金额

    private String spbillCreateIp;//终端IP

    private String timeStart;//交易起始时间

    private String timeExpire;//交易结束时间

    private String goodsTag;//商品标记

    private String tradeType;//交易类型

    private String productId;//商品ID

    private String limitPay;//指定支付方式

    private String openid;//用户标识

    private Integer status;

    private String acceptContent;
    
    public TOrderRecord(){}
    
    /**
     * 调用统一下单Api需要传入的TOrderRecord
     * 如果参数为空，要传null，不能传空字符串
     * @param deviceInfo 可为空
     * @param body
     * @param detail 可为空
     * 商品详细列表，使用Json格式，传输签名前请务必使用CDATA标签将JSON文本串保护起来。
		goods_detail []：
		└ goods_id String 必填 32 商品的编号
		└ wxpay_goods_id String 可选 32 微信支付定义的统一商品编号
		└ goods_name String 必填 256 商品名称
		└ goods_num Int 必填 商品数量
		└ price Int 必填 商品单价，单位为分
		└ goods_category String 可选 32 商品类目ID
		└ body String 可选 1000 商品描述信息
     * @param attach 可为空
     * @param totalFee 
     */
    public TOrderRecord(String outTradeNo,String body,Integer totalFee,String openid,String spbillCreateIp,String deviceInfo,  String detail, String attach) {
		super();
		this.outTradeNo=outTradeNo;
		this.deviceInfo = deviceInfo;
		this.body = body;
		this.detail = detail;
		this.attach = attach;
		this.totalFee = totalFee;
		this.openid=openid;
		this.spbillCreateIp=spbillCreateIp;
	}

	public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? null : oid.trim();
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach == null ? null : attach.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp == null ? null : spbillCreateIp.trim();
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart == null ? null : timeStart.trim();
    }

    public String getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(String timeExpire) {
        this.timeExpire = timeExpire == null ? null : timeExpire.trim();
    }

    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag == null ? null : goodsTag.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId == null ? null : productId.trim();
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay == null ? null : limitPay.trim();
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAcceptContent() {
        return acceptContent;
    }

    public void setAcceptContent(String acceptContent) {
        this.acceptContent = acceptContent == null ? null : acceptContent.trim();
    }
}