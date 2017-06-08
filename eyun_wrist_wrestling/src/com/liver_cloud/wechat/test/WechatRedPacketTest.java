package com.liver_cloud.wechat.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.SortedMap;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.liver_cloud.wechat.core.WechatRedPacketCore;
import com.liver_cloud.wechat.model.RedPacketResult;

public class WechatRedPacketTest {
	
	static Logger Log = Logger.getLogger(WechatRedPacketCore.class);

	@Test
	public void testSendRedPacket(){
        //微信发送现金红包
        String billNo = WechatRedPacketCore.createBillNo("haha");
        SortedMap<String, String> map = WechatRedPacketCore.createSendRedPacketParam(billNo, "ohS1ov0qwYjkemlkk8cDWrJ-uW80", 100,"192.168.0.1");
        WechatRedPacketCore.sign(map);
        String requestXml = WechatRedPacketCore.getRequestXml(map);

        String keystoreFile = "/usr/local/wechatKeystore/apiclient_cert.p12";
        FileInputStream instream=null;
		try {
			instream = new FileInputStream(new File(keystoreFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

        try {
			RedPacketResult redPacketResult = WechatRedPacketCore.post(requestXml, instream);
			Log.info(redPacketResult.toString());
			
		} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException
				| KeyStoreException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
