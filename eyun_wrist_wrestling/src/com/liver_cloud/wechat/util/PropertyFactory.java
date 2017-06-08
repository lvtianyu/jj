package com.liver_cloud.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * 暂时写死，封装成jar包时需要改进
 * @author yajun
 */
public class PropertyFactory {
	
	public final static String MpAppID="MpAppID";//公众平台AppId
	public final static String MpAppSecret="MpAppSecret";//公众平台AppSecret
	public final static String MpMchId="MpMchId";//公众平台支付用到的商户Id
	public final static String MpMchKey="MpMchKey";//公众平台支付用到的商户Id
	public final static String NOTIFYURL="NOTIFYURL";//公众平台支付结果回调url
	public final static String REDIRECT_URI="REDIRECT_URI";//公众平台授权登录用到的回调url
	public final static String REDIRECT_FOREGROUND="REDIRECT_FOREGROUND";//后台收到微信回调后重定向到前台页面的URL
	public final static String PAY_FOR_GAME_MONEY="PAY_FOR_GAME_MONEY";
	public final static String TOKEN_CACHE_FOLDER="TOKEN_CACHE_FOLDER";
	
	static Properties wechatProps = new Properties();

	static {
		try {
			wechatProps.load(PropertyFactory.class.getClassLoader().getResourceAsStream("wechat.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private PropertyFactory() {
		
	}; 
	
	/**
	 * pops.load(PropertyFactory.class.getClassLoader().getResourceAsStream("system.properties"));
	 * @param wechatProperty
	 */
	public static void loadWxProperty(InputStream wechatProperty){
		try {
//			pops.load(wechatProperty);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getWxProperty(String key) {
		return wechatProps.getProperty(key);
	}


}