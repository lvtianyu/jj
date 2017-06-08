package com.liver_cloud.wechat.util;


import java.security.MessageDigest;

public class MD5Util {

	// 全局数组
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return hexDigits[iD1] + hexDigits[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte
     * @return
     */
    private static String byteArrayToHexString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * @param origin
     * @return
     */
    public static String getShortMD5(String origin) {
        return getMD5(origin).substring(8, 24);
    }

    /**
     * @param origin
     * @param charSetName
     * @return
     */
    public static String getShortMD5(String origin, String charSetName) {
        return getMD5(origin,charSetName).substring(8, 24);
    }


    /**
     * @param origin
     * @param charSetName
     * @return
     */
    public static String getMD5(String origin, String charSetName) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charSetName == null || "".equals(charSetName)) {
                resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
            }
            else {
                resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes(charSetName)));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultString;
    }

    /**
     * @param origin
     * @return
     */
    public static String getMD5(String origin) {
        return getMD5(origin, "UTF-8");
    }

    public static void main(String[] args) {
        String s1 = "123456";
        String r1 = getMD5(s1);
        String r2 = getMD5(s1,"UTF-8");
        String r3 = getShortMD5(s1);
        String r4 = getShortMD5(s1,"UTF-8");
        System.out.println("s1 = " + s1);
        System.out.println("r1 = " + r1);
        System.out.println("r2 = " + r2);
        System.out.println("r3 = " + r3);
        System.out.println("r4 = " + r4);

//        s1 = 123456
//        r1 = e10adc3949ba59abbe56e057f20f883e
//        r2 = e10adc3949ba59abbe56e057f20f883e
//        r3 = 49ba59abbe56e057
//        r4 = 49ba59abbe56e057


    }

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

}
