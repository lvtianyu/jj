package com.liver_cloud.wechat.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.liver_cloud.wechat.common.EnumErrorCode;

/**
 * HttpServletResponse帮助类
 */
public final class ResponseUtils {
	public static final Logger logger = LoggerFactory
			.getLogger(ResponseUtils.class);

	/**
	 * 发送文本。使用UTF-8编码。
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	/**
	 * 发送json。使用UTF-8编码。
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderJsJson(HttpServletResponse response, String text,
			String callback) {
		if (callback == null || "".equals(callback)) {
			render(response, "application/json;charset=UTF-8", text);
		} else {
			render(response, "application/json;charset=UTF-8", callback + "("
					+ text + ")");
		}
	}

	public static void renderJsJson(HttpServletResponse response, String text) {
		renderJsJson(response, text, null);
	}

	/**
	 * 发送xml。使用UTF-8编码。
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param text
	 *            发送的字符串
	 */
	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	/**
	 * 发送内容。使用UTF-8编码。
	 *
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void render(HttpServletResponse response, String contentType,
			String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @param response
	 */
	public static void respSuccess(HttpServletResponse response){
		resp(response,"ok","", EnumErrorCode.CODE_000000.msg,EnumErrorCode.CODE_000000.code);
	}
	/**
	 * @param response
	 * @param callback
	 */
	public static void respSuccess(HttpServletResponse response, String callback){
		resp(response,"ok","",EnumErrorCode.CODE_000000.msg,EnumErrorCode.CODE_000000.code,callback);
	}

	/**
	 * @param response
	 * @param values
	 */
	public static void respSuccess(HttpServletResponse response,Object values){
		resp(response,"ok",values,"","",null);
	}
	/**
	 * @param response
	 * @param values
	 * @param callback
	 */
	public static void respSuccess(HttpServletResponse response,Object values, String callback){
		resp(response,"ok",values,"","",callback);
	}

	/**
	 * @param response
	 * @param values
	 * @param errormsg
	 * @param errorcode
	 */
	public static void respSuccess(HttpServletResponse response,Object values, String errormsg, String errorcode){
		resp(response,"ok",values,errormsg,errorcode,null);
	}
	/**
	 * @param response
	 * @param values
	 * @param errormsg
	 * @param errorcode
	 * @param callback
	 */
	public static void respSuccess(HttpServletResponse response,Object values, String errormsg, String errorcode, String callback){
		resp(response,"ok",values,errormsg,errorcode,callback);
	}

	/**
	 * @param response
	 * @param enumError
	 */
	public static void respError(HttpServletResponse response,EnumErrorCode enumError){
		resp(response,"error","",enumError.msg,enumError.code,null);
	}
	/**
	 * @param response
	 * @param enumError
	 * @param callback
	 */
	public static void respError(HttpServletResponse response,EnumErrorCode enumError, String callback){
		resp(response,"error","",enumError.msg,enumError.code,callback);
	}

	/**
	 * @param response
	 * @param errormsg
	 * @param errorcode
	 */
	public static void respError(HttpServletResponse response,String errormsg, String errorcode){
		resp(response,"error","",errormsg,errorcode);
	}
	/**
	 * @param response
	 * @param errormsg
	 * @param errorcode
	 * @param callback
	 */
	public static void respError(HttpServletResponse response,String errormsg, String errorcode, String callback){
		resp(response,"error","",errormsg,errorcode,callback);
	}

    /**
     * @param response
     * @param errormsg
     * @param enumError
     * @param callback
     */
    public static void respError(HttpServletResponse response,String errormsg, EnumErrorCode enumError, String callback){
		resp(response,"error","",errormsg,enumError.code,callback);
	}
	/**
	 * @param response
	 * @param values
	 * @param errormsg
	 * @param errorcode
	 */
	public static void respError(HttpServletResponse response,Object values, String errormsg, String errorcode){
		resp(response,"error",values,errormsg,errorcode,null);
	}
	/**
	 * @param response
	 * @param values
	 * @param errormsg
	 * @param errorcode
	 * @param callback
	 */
	public static void respError(HttpServletResponse response,Object values, String errormsg, String errorcode, String callback){
		resp(response,"error",values,errormsg,errorcode,callback);
	}

	/**
	 * @param response
	 * @param result
	 * @param values
	 * @param errormsg
	 * @param errorcode
	 */
	public static void resp(HttpServletResponse response,String result, Object values, String errormsg, String errorcode){
		resp(response, result, values, errormsg, errorcode, null);
	}
	/**
	 * @param response
	 * @param result
	 * @param values
	 * @param errormsg
	 * @param errorcode
	 * @param callback
	 */
	public static void resp(HttpServletResponse response,String result, Object values, String errormsg, String errorcode, String callback){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		map.put("values", values);
		map.put("errormsg", errormsg);
		map.put("errorcode", errorcode);
		renderJsJson(response, JSON.toJSONString(map),callback);
	}


    /**
     * 把request.getParameterMap()的Map<String, String[]>转变为一般的Map<String, String>
     * @param map
     * @return
     */
    public static Map<String, String> reformat(Map<String, String[]> map) {
        Map<String, String> r = new HashMap<String, String>();
        String[] vs = null;
        for (String k : map.keySet()) {
            r.put(k,getParameter(k,map));
        }

        return r;
    }

    public static String getParameter(String name, Map<String, String[]> params) {
        String result = "";

        Object v = params.get(name);
        if (v == null) {
            result = null;
        } else if (v instanceof String[]) {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                result = strArr[0];
            } else {
                result = null;
            }
        } else if (v instanceof String) {
            result = (String) v;
        } else {
            result = v.toString();
        }

        return result;
    }
}
