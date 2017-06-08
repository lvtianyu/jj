package com.liver_cloud.wechat.core;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liver_cloud.wechat.common.Consts;
import com.liver_cloud.wechat.http.HttpRequestor;
import com.liver_cloud.wechat.model.UserInfo;
import com.liver_cloud.wechat.util.JsonUtil;
import com.liver_cloud.wechat.util.LogUtil;
import com.liver_cloud.wechat.util.PropertyFactory;

/**
 * 第一步：用户同意授权，获取code
 * 第二步：通过code换取网页授权access_token
 * 第三步：刷新access_token（如果需要）
 * 第四步：拉取用户信息(需scope为 snsapi_userinfo)
 * @author yajun
 *
 */
public class WechatMPLoginCore {
	
	static Logger Log = Logger.getLogger(WechatMPLoginCore.class);
	
	/**
	 * 获取重定向到微信拼接好参数的url
	 * @param state 重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
	 */
	public static String getRedirectWechatLoginUrl(String state){
        // 重定向到微信接口
        // https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
        String wxurl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=";
        String appId =PropertyFactory.getWxProperty(PropertyFactory.MpAppID);
        // TODO 使用Map改造
        StringBuffer sb = new StringBuffer();
        sb.append(wxurl);
        sb.append(appId);
        sb.append("&redirect_uri=");
        try {
			sb.append(URLEncoder.encode(PropertyFactory.getWxProperty(PropertyFactory.REDIRECT_URI),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        sb.append("&response_type=code&scope=snsapi_userinfo&state=");// 注意此处的scope和PC端不同，PC端是snsapi_login
        sb.append(state);// 后续可用来校验，可不填，把type值也传过去，方便后续判断
        sb.append("#wechat_redirect");
        // 完整url
        String url = sb.toString();
        LogUtil.i("重定向到微信登录页完整url"+url);
        return url;
	}
	
	/**
	 * 微信登录第二步
	 * 获取AccessToken
	 * @param code
	 * @return
		 正确的accessToken返回结果json：
         <pre>
         {
         "access_token":"ACCESS_TOKEN",
         "expires_in":7200,
         "refresh_token":"REFRESH_TOKEN",
         "openid":"OPENID",
         "scope":"SCOPE",
         "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
         }
         </pre>
	 */
	public static String getAccessToken(String code) {
		// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
		String appid=PropertyFactory.getWxProperty(PropertyFactory.MpAppID);
		String secret=PropertyFactory.getWxProperty(PropertyFactory.MpAppSecret);
		StringBuffer sb = new StringBuffer();
		sb.append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
		sb.append(appid);
		sb.append("&secret=");
		sb.append(secret);
		sb.append("&code=");
		sb.append(code);
		sb.append("&grant_type=authorization_code");
		String reqTokenUrl = sb.toString();
		System.out.println("reqTokenUrl: "+reqTokenUrl);
		String result="";
		try {
			result = new HttpRequestor().doGet(reqTokenUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtil.i("accessTokenJson:"+result);
		return result;
	}

	public static UserInfo getUserInfo(HttpServletResponse response, String accessTokenJson) {
		System.out.println("getUserInfo()");
		try {
			JSONObject json = JSON.parseObject(accessTokenJson);
			// 如果出现错误，那么返回首页
			// {"errcode":40030,"errmsg":"invalid refresh_token"}
			int errcode = json.getIntValue("errcode");
			String errmsg = json.getString("errmsg");
			System.out.println("errcode: "+errcode + " errmsg:"+errmsg);
			// 如果没问题，根据token获取用户信息
			if (errcode == 0) {
				// TODO 使用对象完善
				String access_token = json.getString("access_token");
				int expires_in = json.getIntValue("expires_in");
				String refresh_token = json.getString("refresh_token");
				String openid = json.getString("openid");
				String scope = json.getString("scope");
				String unionid = json.getString("unionid");

				StringBuffer sb = new StringBuffer();
				sb.append("https://api.weixin.qq.com/sns/userinfo?access_token=");
				sb.append(access_token);
				sb.append("&openid=");
				sb.append(openid);
				String reqUserJsonUrl = sb.toString();
				Log.info("请求userInfo Url："+reqUserJsonUrl);
				String userJsonStr = new HttpRequestor().doGet(reqUserJsonUrl);
				LogUtil.i("获取到的userInfo:"+userJsonStr);
				return JsonUtil.parseJson(userJsonStr, UserInfo.class);

			}else{
				// TODO 进一步完善
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO 进一步完善
			return null;
		}
	}

}
