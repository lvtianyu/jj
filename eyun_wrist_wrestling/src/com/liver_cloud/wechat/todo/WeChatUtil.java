package com.liver_cloud.wechat.todo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.liver_cloud.wechat.http.MyX509TrustManager;
import com.liver_cloud.wechat.model.AccessToken;
import com.liver_cloud.wechat.model.Ticket;
import com.liver_cloud.wechat.util.PropertyFactory;

/**
 * 微信通用接口工具类
 * 
 * @author caspar.chen
 * @version 1.0
 */
public class WeChatUtil {

	/**
	 * 获取access_token的接口地址（GET） 限200（次/天）
	 */
	private final static String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	private final static String URL_GET_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=";

	private static JSONObject doQueryAccessToken() {
		String appId=PropertyFactory.getWxProperty(PropertyFactory.MpAppID);
		String appSecret=PropertyFactory.getWxProperty(PropertyFactory.MpAppSecret);
		String requestUrl = ACCESS_TOKEN.replace("APPID",appId).replace("APPSECRET",appSecret); 
		JSONObject resultJsonObj = new JSONObject();
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				resultJsonObj.put("access_token", jsonObject.getString("access_token"));
				long now = System.currentTimeMillis();
				long expireTime = now + 7000000;
				// Date d = new Date(now);
				// System.out.println("now: "+String.valueOf(now));
				// System.out.println("now hour: "+d.getHours() + " now min:"+
				// d.getMinutes());
				// Date e = new Date(expireTime);
				// System.out.println("expireTime hour: "+e.getHours() + "
				// expireTime min:"+ e.getMinutes());
				// System.out.println("expireTime:
				// "+String.valueOf(expireTime));
				resultJsonObj.put("expire_time", String.valueOf(expireTime));

			} catch (JSONException e) {
				// 获取token失败
				System.err.println("获取token失败 errcode:" + jsonObject.getIntValue("errcode") + "，errmsg:"
						+ jsonObject.getString("errmsg"));
			}
		}
		return resultJsonObj;
	}

	/**
	 * 获取token值
	 *
	 * @return token
	 */
	public String getToken() {
		AccessToken accessToken = null;

		// 读取本地的缓存文件，如果有，那么读取里面的内容，看看有没有到超时时间，如果超时了，那么重新获取token，更新本地缓存文件
		String tokenFileContent = FileUtil.readFileUTF8(Constants.getTOKEN_FILE_PATH()); // .TOKEN_FILE_PATH
		boolean needReGetAccessToken = false;
		if (null == tokenFileContent || "".equals(tokenFileContent)) {
			needReGetAccessToken = true;
		} else {
			JSONObject j = JSON.parseObject(tokenFileContent);
			long expireTime = j.getLongValue("expire_time");
			// System.out.println("expireTime2: "+ expireTime);
			long now = System.currentTimeMillis();
			// System.out.println("now: "+ now);
			// 时辰已过，需要重新获取token
			if (expireTime < now) {
				needReGetAccessToken = true;
			} else {
				accessToken = new AccessToken();
				accessToken.setToken(j.getString("access_token"));
				accessToken.setExpiresIn(j.getString("expire_time"));
			}
		}

		if (needReGetAccessToken) {
			// 提交请求
			JSONObject res = doQueryAccessToken();
			FileUtil.createIfNoExists(Constants.getFILE_CACHE_FOLDER()); // .FILE_CACHE_FOLDER
			FileUtil.writeContent(res.toString(), Constants.getTOKEN_FILE_PATH(), false); // .TOKEN_FILE_PATH
			accessToken = new AccessToken();
			accessToken.setToken(res.getString("access_token"));
			accessToken.setExpiresIn(res.getString("expire_time"));
		}
		String strAccessToken = "";
		if (null != accessToken) {
			strAccessToken = accessToken.getToken();
		}

		return strAccessToken;
	}

	public String getTicket() {

		// 读取本地的缓存文件，如果有，那么读取里面的内容，看看有没有到超时时间，如果超时了，那么重新获取token，更新本地缓存文件
		String fileContent = FileUtil.readFileUTF8(Constants.getTICKET_FILE_PATH()); // .TICKET_FILE_PATH
		boolean needReGet = false;
		JSONObject j = null;
		if (null == fileContent || "".equals(fileContent)) {
			needReGet = true;
		} else {
			j = JSON.parseObject(fileContent);
			long expireTime = j.getLongValue("expire_time");
			// System.out.println("expireTime2: "+ expireTime);
			long now = System.currentTimeMillis();
			// System.out.println("now: "+ now);
			// 时辰已过，需要重新获取token
			if (expireTime < now) {
				needReGet = true;
			}
		}
		Ticket ticket = new Ticket();
		if (needReGet) {
			// 提交请求
			JSONObject res = doQueryTicket();
			FileUtil.createIfNoExists(Constants.getFILE_CACHE_FOLDER()); // .FILE_CACHE_FOLDER
			FileUtil.writeContent(res.toString(), Constants.getTICKET_FILE_PATH(), false); // .TICKET_FILE_PATH
			ticket.setJsapiTicket(res.getString("jsapi_ticket"));
			ticket.setExpireTime(res.getString("expire_time"));
		} else {
			ticket.setJsapiTicket(j.getString("jsapi_ticket"));
			ticket.setExpireTime(j.getString("expire_time"));
		}
		String strTicket = "";
		if (null != ticket) {
			strTicket = ticket.getJsapiTicket();
		}

		return strTicket;
	}

	private JSONObject doQueryTicket() {
		String accessToken = getToken();
		String requestUrl = URL_GET_TICKET + accessToken;

		JSONObject j = new JSONObject();
		JSONObject jsonObject = httpsRequest(requestUrl, "GET", null);
		// 如果请求成功
		if (null != jsonObject) {
			try {
				j.put("jsapi_ticket", jsonObject.getString("ticket"));
				long now = System.currentTimeMillis();
				long expireTime = now + 7200000;
				// Date d = new Date(now);
				// System.out.println("now: "+String.valueOf(now));
				// System.out.println("now hour: "+d.getHours() + " now min:"+
				// d.getMinutes());
				// Date e = new Date(expireTime);
				// System.out.println("expireTime hour: "+e.getHours() + "
				// expireTime min:"+ e.getMinutes());
				// System.out.println("expireTime:
				// "+String.valueOf(expireTime));
				j.put("expire_time", String.valueOf(expireTime));

			} catch (JSONException e) {
				// 获取token失败
				System.err.println("获取ticket失败 errcode:" + jsonObject.getIntValue("errcode") + "，errmsg:"
						+ jsonObject.getString("errmsg"));
			}
		}

		return j;
	}

	/**
	 * 发起https请求并获取结果
	 *
	 * @param requestUrl
	 *            请求地址
	 * @param requestMethod
	 *            请求方式（GET、POST）
	 * @param outputStr
	 *            提交的数据
	 * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
	 */
	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// 注意编码格式，防止中文乱码
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// 将返回的输入流转换成字符串
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSON.parseObject(buffer.toString());
		} catch (ConnectException ce) {
			System.err.println("server connection timed out.");
		} catch (Exception e) {
			System.err.println("https request error: " + e.getMessage());
		}
		return jsonObject;
	}

	/**
	 * 将微信消息中的CreateTime转换成标准格式的时间（yyyy-MM-dd HH:mm:ss）
	 *
	 * @param createTime
	 *            消息创建时间
	 * @return String
	 */
	public static String formatTime(String createTime) {
		// 将微信传入的CreateTime转换成long类型，再乘以1000
		long msgCreateTime = Long.parseLong(createTime) * 1000L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date(msgCreateTime));
	}

	@SuppressWarnings({ "unchecked", "unchecked" })
	public static void main(String[] args) throws Exception {
		/*
		 * String access_token= WeChat.getAccessToken(); String jsapi_ticket =
		 * WeChat.getJsApiTicket(access_token);
		 */
		// System.out.println("access_token : "+access_token+ " jsapi_ticket: "
		// +jsapi_ticket);
		String jsapi_ticket = "jsapi_ticket";
		String url = "http://cmsplus.com.cn";
		Map<String, String> ret = sign(jsapi_ticket, url);
		for (Map.Entry entry : ret.entrySet()) {
			// System.out.println(entry.getKey() + "======== " +
			// entry.getValue());
		}
		System.out.println("signature:  " + ret.get("signature") + ": timestamp " + ret.get("timestamp"));
		System.out.println(createLinkString(ret));
	};

	// 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）后
	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		return prestr;
	}

	/**
	 * 签名,生成调用JSSDK所需的签名（规则来自微信JS-SDK说明文档附录1）
	 * @param jsapi_ticket
	 * @param url
	 * @return
	 */
	public static Map<String, String> sign(String jsapi_ticket, String url) {
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = create_nonce_str();
		String timestamp = create_timestamp();
		String string1;
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
		// System.out.println(string1);
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(string1.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ret.put("url", url);
		ret.put("jsapi_ticket", jsapi_ticket);
		ret.put("noncestr", nonce_str);
		ret.put("timestamp", timestamp);
		ret.put("signature", signature);
		return ret;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	private static String create_nonce_str() {
		return UUID.randomUUID().toString();
	}

	private static String create_timestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}

}
