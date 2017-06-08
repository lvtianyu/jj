package com.liver_cloud.service.user;

import com.liver_cloud.entity.user.User;
import com.liver_cloud.wechat.model.UserInfo;

public interface UserService2 {

	public User saveOrUpdateUser(UserInfo userInfo,Integer userType);

	public String getWechatOpenId(Integer userId);
	
}
