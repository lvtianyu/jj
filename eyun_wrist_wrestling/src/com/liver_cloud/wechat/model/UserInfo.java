package com.liver_cloud.wechat.model;

import java.util.Arrays;

public class UserInfo {
	String openid;
	String nickname;
	int sex;
	String language;
	String city;
	String province;
	String country;
	String headimgurl;
	String [] privilege;
	String unionid;
	@Override
	public String toString() {
		return "UserInfo [openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", language=" + language
				+ ", city=" + city + ", province=" + province + ", country=" + country + ", headimgurl=" + headimgurl
				+ ", privilege=" + Arrays.toString(privilege) + ", unionid=" + unionid + "]";
	}
	public UserInfo() {
		super();
	}
	public UserInfo(String openid, String nickname, int sex, String language, String city, String province,
			String country, String headimgurl, String[] privilege, String unionid) {
		super();
		this.openid = openid;
		this.nickname = nickname;
		this.sex = sex;
		this.language = language;
		this.city = city;
		this.province = province;
		this.country = country;
		this.headimgurl = headimgurl;
		this.privilege = privilege;
		this.unionid = unionid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String[] getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	
}
