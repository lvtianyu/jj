package com.liver_cloud.wechat.core;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.liver_cloud.wechat.common.Consts;
import com.liver_cloud.wechat.model.RedPacketResult;
import com.liver_cloud.wechat.util.MD5Util;
import com.liver_cloud.wechat.util.PropertyFactory;
import com.liver_cloud.wechat.util.WxUtil;


/**
 * 微信企业红包相关
 */
public class WechatRedPacketCore {
	
	static Logger Log = Logger.getLogger(WechatRedPacketCore.class);
	
    public static final String MCH_ID = PropertyFactory.getWxProperty(PropertyFactory.MpMchId);               //商户号
    public static final String MCH_KEY = PropertyFactory.getWxProperty(PropertyFactory.MpMchKey); //微信商户平台的秘钥(key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置)
    public static final String WXAPPID = PropertyFactory.getWxProperty(PropertyFactory.MpAppID); //WeChatUtil.APPID;        //公众账号appid
    public static final String NICK_NAME = "福瑞医云";            //提供方名称
    public static final String SEND_NAME = "福瑞医云";            //商户名称
    public static final int TOTAL_NUM = 1;                      //红包发放人数
    public static final String WISHING = "谢谢支持";            //红包祝福语
    public static final String REMARK = "liver-cloud";                  //备注
    public static final String charset = "UTF-8";

    public static final int FAIL = 0;    //领取失败
    public static final int SUCCESS = 1; //领取成功
    public static final int LOCK = 2;    //已在余额表中锁定该用户的余额,防止领取的红包金额大于预算

    /**
     * 对请求参数名ASCII码从小到大排序后签名
     *
     * @param params
     */
    public static void sign(SortedMap<String, String> params) {
        Set<Entry<String, String>> entrys = params.entrySet();
        Iterator<Entry<String, String>> it = entrys.iterator();
        String result = "";
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            result += entry.getKey() + "=" + entry.getValue() + "&";
        }
        result += "key=" + MCH_KEY;
        String sign = null;
        try {
            sign = MD5Util.MD5Encode(result,charset).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("sign", sign);
    }

    public static String getRequestXml(SortedMap<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = params.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 创建发送红包接口请求参数
     * @param billNo 
     * @param openid 向哪个微信用户发红包
     * @param amount 单位为分
     * @param clientIp 调用该接口的客户端ip
     * @return
     */
    public static SortedMap<String, String> createSendRedPacketParam(String billNo, String openid, int amount,String clientIp) {
    	
        SortedMap<String, String> params = createSendRedPacketParam(billNo, openid, amount, clientIp, "掰手腕竞猜");
        return params;
    }
    /**
     * 创建发送红包接口请求参数
     * @param billNo 
     * @param openid 向哪个微信用户发红包
     * @param amount 单位为分
     * @param clientIp 调用该接口的客户端ip
     * @param actName 活动名称，因参与哪种活动而发的红包
     * @return
     */
    public static SortedMap<String, String> createSendRedPacketParam(String billNo, String openid, int amount,String clientIp,String actName) {
    	
    	SortedMap<String, String> params = new TreeMap<String, String>();
    	params.put("nonce_str", createNonceStr());
    	params.put("mch_billno", billNo);
    	params.put("mch_id", MCH_ID);
    	params.put("wxappid", WXAPPID);
    	params.put("nick_name", NICK_NAME);
    	params.put("send_name", SEND_NAME);
    	params.put("re_openid", openid);
    	params.put("total_amount", amount + "");
    	params.put("total_num", TOTAL_NUM + "");
    	params.put("wishing", WISHING);
    	params.put("client_ip", clientIp);
    	params.put("act_name", actName);
    	params.put("remark", REMARK);
    	return params;
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String createNonceStr() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "");
    }

    /**
     * 生成商户订单号
     *
     * @param userId 该用户的userID
     * @return
     */
    public static String createBillNo(String userId) {
        //组成： mch_id+yyyymmdd+10位一天内不能重复的数字
        //10位一天内不能重复的数字实现方法如下:
        //因为每个用户绑定了userId,他们的userId不同,加上随机生成的(10-length(userId))可保证这10位数字不一样
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        String nowTime = df.format(dt);
        int length = 10 - userId.length();
        return MCH_ID + nowTime + userId + getRandomNum(length);
    }

//    public static void main(String[] args) {
//        String billNo = createBillNo("R5746857");
//        System.out.println("billNo = " + billNo);
//    }

    /**
     * 生成特定位数的随机数字
     *
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    public static RedPacketResult post(String requestXML, InputStream instream) throws NoSuchAlgorithmException, CertificateException, IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException {
        Log.info("调用微信红包，请求参数："+requestXML);
    	KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(instream, MCH_ID.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCH_ID.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
            sslcontext,
            new String[]{"TLSv1"},
            null,
            SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

        String result = "";
        try {

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");
            StringEntity reqEntity = new StringEntity(requestXML, "UTF-8");
            // 设置类型
            reqEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        result += text;
                    }
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        Log.info("调用微信发红包，返回结果："+result);
        RedPacketResult redPacketResult = new RedPacketResult();
        redPacketResult.parseXMLText(result);
        //微信文档上有sign返回值，实际测试却没有，所以注释掉验证签名这一段
        /*SortedMap<Object,Object> sortMap = redPacketResult.convertSortMap();
        String geneSign = WxUtil.createSign(Consts.CharsetName, sortMap
				, PropertyFactory.getWxProperty(PropertyFactory.MpMchKey));
		if(!geneSign.equals(redPacketResult.getSign())){
			// 签名失败
			Log.info("发送红包返回结果，验证签名失败");
			Log.info("geneSign:"+geneSign);
			Log.info("wechatSign:"+redPacketResult.getSign());
			throw new IllegalArgumentException();
		}*/
        
        return redPacketResult;
    }
}
