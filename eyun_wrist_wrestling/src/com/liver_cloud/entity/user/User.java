package com.liver_cloud.entity.user;

/**
 * 
* 类名称：User.java
* 类描述： 
* @author Yajun
* @version 1.0
* 修改字段记得修改sql
* CREATE  TABLE `eyun_wrist_wrestling`.`user` (
  `u_id` INT NOT NULL COMMENT '自增的用户id' ,
  `u_password` VARCHAR(45) NULL COMMENT '用户密码' ,
  `u_nick_name` VARCHAR(45) NULL COMMENT '用户昵称' ,
  `u_head_img_url` VARCHAR(255) NULL COMMENT '用户头像url' ,
  `u_sex` INT NULL COMMENT '性别，1男、2女' ,
  `u_user_type` INT NULL COMMENT '用户类型，点赞者1、比赛者2' ,
  `u_wechat_open_id` VARCHAR(45) NULL COMMENT '微信登录openId' ,
  `u_wechat_union_id` VARCHAR(45) NULL COMMENT '微信登录UnionId' ,
  PRIMARY KEY (`u_id`) );
 */
public class User {
	
	/**
	 * 点赞者
	 */
	public final static int UserTypeSupport=1;
	
	/**
	 * 比赛者
	 */
	public final static int UserTypeSport=2;
	
	/**
	 * 裁判
	 */
	public final static int UserTypeReferee=3;
	
	private Integer userId;		//用户id
	private String password;	//登录密码
	private String wechatOpenId; 	//微信登录标识，用户的微信openid
	private String wechatUnionId;	//微信登录UnionId
	private String nickName;		//姓名
	private String headImgUrl; //用户头像url
	private int sex;//用户性别
	private int userType;//用户类型
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getWechatOpenId() {
		return wechatOpenId;
	}
	public void setWechatOpenId(String wechatOpenId) {
		this.wechatOpenId = wechatOpenId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headPortraitUrl) {
		this.headImgUrl = headPortraitUrl;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getWechatUnionId() {
		return wechatUnionId;
	}
	public void setWechatUnionId(String wechatUnionId) {
		this.wechatUnionId = wechatUnionId;
	}
}
