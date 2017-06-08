package com.liver_cloud.entity.order;

/**
 * 支付记录
 * 1. 参与游戏时，支持某个参赛者时支付一定金额时产生的支付记录
 * 2. 用户充值产生的支付记录
 * @author yajun
 * 注意，修改表字段，记得修改数据库表
 *CREATE  TABLE `eyun_wrist_wrestling`.`pay_record` (
  `pr_id` INT NOT NULL AUTO_INCREMENT COMMENT '自增的订单id，不是订单号，订单号是trade_no' ,
  `pr_user_id` INT NULL COMMENT '支付用户' ,
  `pr_order_id` INT NULL COMMENT '在game中，支持某个参赛者时，orderId的值为gameId' ,
  `pr_pay_type` INT NULL COMMENT '支付类型，1：支持某个参赛者、2：充值' ,
  `pr_device_info` VARCHAR(45) NULL COMMENT '支付用户设备号' ,
  `pr_body` VARCHAR(45) NULL COMMENT '支付内容' ,
  `pr_detail` VARCHAR(255) NULL COMMENT '支付详情' ,
  `pr_trade_no` VARCHAR(45) NULL COMMENT '交易号，与微信支付对接用的就是这个' ,
  `pr_fee_type` VARCHAR(45) NULL COMMENT '货币类型' ,
  `pr_total_fee` INT NULL COMMENT '总金额' ,
  `pr_spbill_create_ip` VARCHAR(45) NULL COMMENT '支付设备ip' ,
  `pr_time_start` BIGINT(15) NULL COMMENT '交易开始时间' ,
  `pr_time_expire` BIGINT(15) NULL COMMENT '交易结束时间' ,
  `pr_status` INT NULL COMMENT '支付状态' ,
  `pr_wechat_status` INT NULL COMMENT '是否收到微信支付通知' ,
  `pr_competitor_id` int(11) DEFAULT NULL COMMENT '被赞者ID',
  `pr_trade_type` VARCHAR(45) NULL COMMENT '微信支付类型JSAPI、APP、Native' ,
  `pr_bank_type` VARCHAR(45) NULL COMMENT '付款银行' ,
  `pr_transaction_id` VARCHAR(45) NULL COMMENT '微信支付交易号' ,
  `pr_err_code` VARCHAR(45) NULL COMMENT '支付错误code' ,
  `pr_err_code_des` VARCHAR(45) NULL COMMENT '支付错误描述' ,
  `pr_redPacket_status` TINYINT(2) NULL COMMENT '' ,
  PRIMARY KEY (`pr_id`) )
COMMENT = '支付记录，比如支持某个参赛者时支付一定金额';

 */
public class PayRecord {
	
	/**
	 * 支付类型，给参赛者点赞
	 * 后期优化
	 */
	public final static Integer PayTypeSupportGame=1;
	/**
	 * 支付类型，充值
	 */
	public final static Integer PayTypeCharge=2;
	/**
	 * 支付状态未支付
	 */
	public final static Integer StatusUnPay=1;
	/**
	 * 支付状态支付成功
	 */
	public final static Integer StatusPaySuccess=2;
	/**
	 * 支付状态支付失败
	 */
	public final static Integer StatusPayFail=3;
	/**
	 * 支付状态支付取消
	 */
	public final static Integer StatusPayCancel=4;
	
	/**
	 * 数据库表主键，自增
	 */
	private Integer payRecordId;
	
	private Integer userId;//用户id
	
	/**
	 * 为哪一个订单支付，如果为参与游戏，那么orderId就是gameId
	 */
	private Integer orderId;
	
	/**
	 * 后期优化
	 * 商品类型，类型1：给支持的人点赞
	 */
	private Integer payType;

    private String deviceInfo;//设备号
    
    /**
     * JSAPI、NATIVE、APP
     */
    private String tradeType;

    /**
     * 付款银行
     */
    private String bankType;
    
    private String body;//商品描述

    private String detail;//商品详情

    private String tradeNo;//交易号，支付记录的唯一标识

    private String feeType;//货币类型

    private Integer totalFee;//总金额

    private String spbillCreateIp;//终端IP

    private Long timeStart;//交易起始时间

    private Long timeExpire;//交易结束时间
    
    /**
     * 微信支付订单号
     */
    private String transactionId;

    private Integer status;//支付状态
    
    private Boolean wechatStatus;//微信通知状态，是否接受到微信支付通知
    
    /**
     * 支付失败时错误代码
     */
    private String errCode;
    
    /**
     * 支付失败时错误代码描述
     */
    private String errCodeDes;
    
    private Integer competitorId;

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
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
		this.spbillCreateIp = spbillCreateIp;
	}

	public Long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}

	public Long getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(Long timeExpire) {
		this.timeExpire = timeExpire;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Boolean getWechatStatus() {
		return wechatStatus;
	}

	public void setWechatStatus(Boolean wechatStatus) {
		this.wechatStatus = wechatStatus;
	}

	public Integer getPayRecordId() {
		return payRecordId;
	}

	public void setPayRecordId(Integer payRecordId) {
		this.payRecordId = payRecordId;
	}

	public Integer getCompetitorId() {
		return competitorId;
	}

	public void setCompetitorId(Integer competitorId) {
		this.competitorId = competitorId;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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

	
}
