package com.liver_cloud.wechat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

	  /**
	   * 将一个InputStream流转换成字符串
	   * 
	   * @param is
	   * @return
	   */
	  public static String toConvertString(InputStream is) {
	    StringBuffer res = new StringBuffer();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader read = new BufferedReader(isr);
	    try {
	      String line;
	      line = read.readLine();
	      while (line != null) {
	        res.append(line);
	        line = read.readLine();
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      try {
	        if (null != isr) {
	          isr.close();
	          isr.close();
	        }
	        if (null != read) {
	          read.close();
	          read = null;
	        }
	        if (null != is) {
	          is.close();
	          is = null;
	        }
	      } catch (IOException e) {
	      }
	    }
	    return res.toString();
	  }
}
