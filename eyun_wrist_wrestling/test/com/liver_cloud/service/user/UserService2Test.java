package com.liver_cloud.service.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liver_cloud.entity.user.User;
import com.liver_cloud.wechat.common.EnumErrorCode;
import com.liver_cloud.wechat.http.ResponseUtils;
import com.liver_cloud.wechat.model.PayRequestParam;
import com.liver_cloud.wechat.model.UserInfo;
import com.liver_cloud.wechat.util.JsonUtil;
import com.liver_cloud.wechat.util.PropertyFactory;

public class UserService2Test {
	
	protected static Logger Log = Logger.getLogger(UserService2Test.class);

	private static UserService2 userService;

	@BeforeClass
	public static void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/ApplicationContext.xml");
		userService = (UserService2) context.getBean("userService2");
	}

	@Test
	public void testSaveOrUpdateUser() {
		UserInfo userInfo = new UserInfo("openid", "nickname", 1, "language", "city", "province", "country",
				"headimgurl", null, "unionid");
		userService.saveOrUpdateUser(userInfo, User.UserTypeSport);
	}
	
	@Test
	public void testGetWechatOpenIdByUserId(){
		String wechatOpenId = userService.getWechatOpenId(1);
		Log.info("wechatOpenId："+wechatOpenId);
	}
	
	@Test
	public void testAppendUrl(){
		StringBuffer redirectUrlSb = new StringBuffer();
        redirectUrlSb.append(PropertyFactory.getWxProperty(PropertyFactory.REDIRECT_FOREGROUND));
        redirectUrlSb.append("?openId=");
        redirectUrlSb.append("1234");
        redirectUrlSb.append("&nickName=");
        redirectUrlSb.append("zhangsan");
        redirectUrlSb.append("&headImageUrl=");
        redirectUrlSb.append("http://a.hiphotos.baidu.com/image/h%3D200/sign=d20242020e24ab18ff16e63705fae69a/267f9e2f070828389f547b30bf99a9014c08f1bd.jpg");
        redirectUrlSb.append("&userId=");
        redirectUrlSb.append("abcd123");
        
        Log.info(redirectUrlSb.toString());
        
        /*
         	@ResponseBody
	@RequestMapping(value="/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response){
		String openId = request.getParameter("openId");
		String nickName = request.getParameter("nickName");
		String headImageUrl = request.getParameter("headImageUrl");
		String userId = request.getParameter("userId");
		Log.info(openId);
		Log.info(nickName);
		Log.info(headImageUrl);
		Log.info(userId);
	}
         */
	}
	
	@Test
	public void testJson(){
		PayRequestParam payRequestParam =new PayRequestParam();
		payRequestParam.setAppId("appId");
		payRequestParam.setPaySign("paySign");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "ok");
		map.put("values", payRequestParam);
		map.put("errormsg", EnumErrorCode.CODE_000000.msg);
		map.put("errorcode", EnumErrorCode.CODE_000000.code);
		String jsonString = JsonUtil.toJSONString(map);
		Log.info(jsonString);
	}
	
	
}
