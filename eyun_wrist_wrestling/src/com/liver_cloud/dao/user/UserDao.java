package com.liver_cloud.dao.user;

import org.apache.ibatis.annotations.Param;

import com.liver_cloud.entity.user.User;

public interface UserDao {

	/**
	 * 向User表插入一条记录
	 * @param user
	 */
	public void insertUser(User user);
	
	/**
	 * 根据userId更新用户
	 * @param user
	 */
	public void updateUserById(User user);
	
	/**
	 * 根据用户微信openId和用户类型查找用户，如果不存在，返回null
	 * @return
	 */
	public User selectUserByWechatOpenIdAndType(@Param(value="wechatOpenId")String wechatOpenId,@Param(value="userType")Integer userType);
	
	public String getWechatOpenId(Integer userId);
	
	
}
