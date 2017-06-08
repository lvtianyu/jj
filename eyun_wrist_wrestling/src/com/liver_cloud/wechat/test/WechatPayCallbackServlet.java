package com.liver_cloud.wechat.test;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.liver_cloud.wechat.core.WechatMPPayCore;
import com.liver_cloud.wechat.core.WechatPayResultCallback;
import com.liver_cloud.wechat.model.TWxRecord;
import com.liver_cloud.wechat.util.HttpsUtil;
import com.liver_cloud.wechat.util.StringUtil;

/**
 * 微信支付结果回调函数
 */
@WebServlet("/WechatPayCallbackServlet")
public class WechatPayCallbackServlet extends HttpServlet {
	
	protected static Logger Log = Logger.getLogger(WechatMPPayCore.class);
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WechatPayCallbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream payresultStream = WechatPayCallbackServlet.class.getClassLoader().getResourceAsStream("payresult.xml");
		String reqText = StringUtil.toConvertString(payresultStream);
		WechatPayResultCallback callback=new WechatPayResultCallback() {
			
			@Override
			public void updateOrder(String orderId, TWxRecord record) {
				Log.info("updateOrderStatus");
			}
			
			@Override
			public Boolean getOrderStatus(String orderId) {
				Log.info("getOrderStatus");
				return false;
			}
		};
		String payResult = WechatMPPayCore.dealPayResult(reqText, callback);
		HttpsUtil.sendAppMessage(payResult, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
