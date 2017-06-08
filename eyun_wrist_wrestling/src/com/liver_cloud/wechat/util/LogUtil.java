package com.liver_cloud.wechat.util;

import org.apache.log4j.Logger;

public class LogUtil {

	static Logger logger = Logger.getLogger(LogUtil.class);
	
	public static void i(String log){
		logger.info(log);
	}
}
