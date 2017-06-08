package com.liver_cloud.service.user.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.liver_cloud.dao.user.UserDao;
import com.liver_cloud.entity.user.User;
import com.liver_cloud.service.user.UserService2;
import com.liver_cloud.wechat.model.UserInfo;

@Service(value="userService2")
public class UserService2Impl implements UserService2{
	
	protected static Logger Log = Logger.getLogger(UserService2Impl.class);
	
	@Resource
	private UserDao userDao;

	@Override
	public User saveOrUpdateUser(UserInfo userInfo,Integer userType) {
		User user = new User();
		user.setHeadImgUrl(userInfo.getHeadimgurl());
		user.setNickName(userInfo.getNickname());
		user.setSex(userInfo.getSex());
		user.setWechatOpenId(userInfo.getOpenid());
		user.setWechatUnionId(userInfo.getUnionid());
		user.setUserType(userType);
		
		User userByWechatOpenIdAndType = userDao.selectUserByWechatOpenIdAndType(user.getWechatOpenId(),user.getUserType());
		if (userByWechatOpenIdAndType==null) {
			Log.info("saveOrUpdateUser：用户不存在，插入数据库");
			userDao.insertUser(user);
			User newUser = userDao.selectUserByWechatOpenIdAndType(user.getWechatOpenId(),user.getUserType());
			user.setUserId(newUser.getUserId());
		}else{
			Log.info("saveOrUpdateUser：用户存在，更新数据库");
			user.setUserId(userByWechatOpenIdAndType.getUserId());
			userDao.updateUserById(user);
		}
		return user;
	}

	@Override
	public String getWechatOpenId(Integer userId) {
		String wechatOpenId = userDao.getWechatOpenId(userId);
		return wechatOpenId;
	}
}
