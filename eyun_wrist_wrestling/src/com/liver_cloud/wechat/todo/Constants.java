package com.liver_cloud.wechat.todo;

import com.liver_cloud.wechat.util.PropertyFactory;

public class Constants {

	// 微信相关的token等需要保存下来的信息
	private final static String TOKEN_FILE_NAME = "/access_token.json";
	private final static String TICKET_FILE_NAME = "/jsapi_ticket.json";
	
	public static String getFILE_CACHE_FOLDER() {
		return PropertyFactory.getWxProperty(PropertyFactory.TOKEN_CACHE_FOLDER);
	}

	public static String getTOKEN_FILE_PATH() {
		return Constants.getFILE_CACHE_FOLDER() + TOKEN_FILE_NAME;
	}

	public static String getTICKET_FILE_PATH() {
		return Constants.getFILE_CACHE_FOLDER() + TICKET_FILE_NAME;
	}

}
