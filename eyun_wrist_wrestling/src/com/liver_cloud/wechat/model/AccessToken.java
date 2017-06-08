package com.liver_cloud.wechat.model;

/**
 * 微信通用接口凭证
 */
public class AccessToken {

	/**
	 *  获取到的凭证
	 */
	private String token;

	/**
	 *  凭证到期时刻（单位毫秒）
	 */
	private String expiresIn;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public AccessToken(String token, String expiresIn) {
		this.token = token;
		this.expiresIn = expiresIn;
	}

	public AccessToken() {
	}
}
