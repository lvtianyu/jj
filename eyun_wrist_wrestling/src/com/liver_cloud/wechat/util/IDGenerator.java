package com.liver_cloud.wechat.util;

import java.math.BigInteger;
import java.util.Random;

public class IDGenerator{
	public static long longFlag = 0;

	public static String getId() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 1; i < 32; i++)
			buffer.append("0");
		String strTime = "" + System.currentTimeMillis();// +longFlag+(int)(Math.random()*1000+1000);
		if (longFlag < 10) {
			strTime = strTime + "000" + longFlag;
		} else if (longFlag < 100) {
			strTime = strTime + "00" + longFlag;
		} else if (longFlag < 1000) {
			strTime = strTime + "0" + longFlag;
		} else if (longFlag < 10000) {
			strTime = strTime + longFlag;
		}
		strTime = strTime + (int) (Math.random() * 1000 + 1000);
		if (strTime.length() > 31) {
			strTime = strTime.substring(strTime.length() - 31);
		}
		buffer.replace(buffer.length() - strTime.length(), buffer.length(),
				strTime);
		longFlag++;
		if (longFlag > 9999) {
			longFlag = 0;
		}
		return buffer.toString();
//		return strTime.substring(7,strTime.length());
	}
	
	public static String getSchoolId() {		
		return getId().substring(20, IDGenerator.getId().length());
	}
	
	public static void main(String[] args) {
//		BigInteger b = new BigInteger("bcb15f821479b4d5772bd0ca866c00ad5f926e3580720659cc80d39c9d09802a",16);
//		System.out.println(b);
//		
//		BigInteger a = get64Random();
//		
//		System.out.println(a);
//		
//		BigInteger c = b.subtract(a);
//		
//		System.out.println(c);
		
		
		String str_userpwd = getId();
		System.out.println(str_userpwd);
	}
	
	public static BigInteger get64Random(){
		Random ran = new Random();
		StringBuffer strb = new StringBuffer();
		int num1 = 0;
		for (int i = 0; i < 64 / 8; i++) {// 这里是产生9位的64/8=8次，
			while (true) {
				num1 = ran.nextInt(99999999);
				if (num1 > 10000000) {
					strb.append(num1);
					break;
				}
			}
		}	
		return new BigInteger(strb.toString());
	}
	
	public static BigInteger get32Random(){
		Random ran = new Random();
		StringBuffer strb = new StringBuffer();
		int num1 = 0;
		for (int i = 0; i < 32 / 8; i++) {// 这里是产生9位的64/8=8次，
			while (true) {
				num1 = ran.nextInt(99999999);
				if (num1 > 10000000) {
					strb.append(num1);
					break;
				}
			}
		}	
		return new BigInteger(strb.toString());
	}
	
	public static String toHexString(String str) { 
        String result= " "; 
        for (int i=0;i <str.length();i ++) { 
              int ch = (int)str.charAt(i); 
              String s = Integer.toHexString(ch); 
              result = str+ s; 
        } 
        return result; 
}
}
