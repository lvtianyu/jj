package com.liver_cloud.wechat.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import com.liver_cloud.wechat.common.Consts;

/**
 * 
 * 项目名称：PayServer 类名称：WxUtil 创建人：huli 创建时间：2015-12-27 下午8:43:51
 * 
 */
public class WxUtil {
	/**
	 * 按照微信签名算法对请求参数进行签名
	 * @param characterEncoding
	 * @param parameters
	 * @param Key
	 * @return
	 */
	public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters,String Key) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + Key);
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
	public static String getNonceStr() {
		Random random = new Random();
		return MD5Util.MD5Encode(String.valueOf(random.nextInt(10000)), Consts.CharsetName);
	}

	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	public static String getTimeDependWX(){
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
		Date date=new Date();
		String format = sf.format(date);
		return format;
	}
	public static String getTimeDelayDependWX(){
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMddHHmmss");
		Date date=new Date();
		date.setMinutes(date.getMinutes()+10);
		String format = sf.format(date);
		return format;
	}
}
