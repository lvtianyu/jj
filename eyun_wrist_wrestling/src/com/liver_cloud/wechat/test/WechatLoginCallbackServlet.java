package com.liver_cloud.wechat.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liver_cloud.wechat.common.Consts;
import com.liver_cloud.wechat.core.WechatMPLoginCore;
import com.liver_cloud.wechat.http.ResponseUtils;
import com.liver_cloud.wechat.model.UserInfo;
import com.liver_cloud.wechat.util.JsonUtil;
import com.liver_cloud.wechat.util.LogUtil;

/**
 * Servlet implementation class WeixinLoginCallback
 */
@WebServlet("/WeixinLoginCallback")
public class WechatLoginCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WechatLoginCallbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		wxloginCallback(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
    /**
     * 微信登录第一步中用到的回调接口，在这个回调接口中执行第二步和第四步
     * 微信授权登录回调接口
     * 在用户获取登录的url后，前端进行跳转，用户会看到一个绿色的是否允许公众号获取用户信息的界面，
     * 用户点击“同意”后，微信回调的url，如果传递来有code参数，那么说明用户允许登录，如果没有则不允许。
     * 拿到code后，用它去获取accessToken，然后再用token去获取用户的信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void wxloginCallback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	LogUtil.i("wxloginCallback接口被调用");
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String sessionId = request.getSession().getId();
        System.out.println("code:"+code + " state:"+state + " sessionId:" +sessionId);
        boolean isAllowed = false;
        if (null != code && !"".equals(code)) {
            isAllowed = true;
        } else {
            isAllowed = false;
        }

        if (isAllowed) {
            String accessTokenJson = WechatMPLoginCore.getAccessToken(code);
           
            UserInfo userInfo = WechatMPLoginCore.getUserInfo(response, accessTokenJson);
            ResponseUtils.renderJsJson(response, JsonUtil.toJSONString(userInfo)); 
        } else {
        	// TODO
//            String url = Consts.PROTOCOL + Consts.HOST_SERVER +Consts.PORT;
//            response.sendRedirect(url);
            ResponseUtils.renderJsJson(response, "用户拒绝登录"); 
        }
    }

}
