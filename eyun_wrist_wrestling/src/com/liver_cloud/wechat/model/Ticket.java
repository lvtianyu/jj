package com.liver_cloud.wechat.model;

/**
 * 微信JSSDK的临时票据
 */
public class Ticket {

	/**
	 *  获取到的凭证
	 */
	private String jsapiTicket;

	/**
	 *  凭证到期时刻（单位毫秒）
	 */
	private String expireTime;

	public String getJsapiTicket() {
    return jsapiTicket;
  }

  public void setJsapiTicket(String jsapiTicket) {
    this.jsapiTicket = jsapiTicket;
  }

  public String getExpireTime() {
    return expireTime;
  }

  public void setExpireTime(String expireTime) {
    this.expireTime = expireTime;
  }

  public Ticket() {
	}
}
