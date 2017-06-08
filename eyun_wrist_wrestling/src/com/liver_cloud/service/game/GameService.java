package com.liver_cloud.service.game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.liver_cloud.dao.DaoSupport;
import com.liver_cloud.util.PageData;
import com.liver_cloud.wechat.core.WechatRedPacketCore;
import com.liver_cloud.wechat.model.RedPacketResult;

@Service
public class GameService {
	protected static Logger Log = Logger.getLogger(GameService.class);
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/**
	 * 
	 * @Title:
	 * @Description:根据商户id创建game，并返回gameid
	 * @version
	 * @author lifutao
	 * @date 2016年7月8日上午10:22:50
	 */
	public void insertGame(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		pd.put("begin", 0);
		pd.put("cancel", 0);
		pd.put("finish", 0);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = format.format(date);
		pd.put("create_time", dateTime);
		dao.save("GameMapper.createGame", pd);

	}

	/**
	 * 
	 * @Title:
	 * @Description: 通过gameid找到game
	 * @version
	 * @author lifutao
	 * @date 2016年7月8日上午10:23:34
	 */
	public PageData findGameByGameId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("GameMapper.findGameByGameId", pd);
	}

	/**
	 * 
	 * @Title:
	 * @Description:更新game表中a参赛者
	 * @version
	 * @author lifutao
	 * @date 2016年7月7日下午2:03:17
	 */
	public void participateGameA(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("GameMapper.participateGameA", pd);
	}

	/**
	 * 
	 * @Title:
	 * @Description:更新game表中b参赛者
	 * @version
	 * @author lifutao
	 * @date 2016年7月7日下午2:03:47
	 */
	public void participateGameB(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("GameMapper.participateGameB", pd);
	}

	/**
	 * 
	 * @Title:
	 * @Description: 返回新创建的game的id
	 * @version
	 * @author lifutao
	 * @date 2016年7月8日上午10:24:13
	 */
	public int findInsertGameId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		List<PageData> list = (List) dao.findForList("GameMapper.findGameId", pd);
		return (int) list.get(0).get("g_id");
	}

	/**
	 * 
	 * @Title:
	 * @Description:通过gameid查找参赛者信息
	 * @version
	 * @author lifutao
	 * @date 2016年7月8日上午10:32:06
	 */
	public PageData getCompetitorsInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData pData = findGameByGameId(pd);
		PageData result = new PageData();
		if (pData == null) {
			result.put("g_id", pd.get("g_id"));
			return result;
		}
		if (pData.get("g_a") != null) {
			PageData pd1 = (PageData) dao.findForObject("GameMapper.getCompetitorsInfoA", pData);
			result.put("a", pd1);
		}
		if (pData.get("g_b") != null) {
			PageData pd2 = (PageData) dao.findForObject("GameMapper.getCompetitorsInfoB", pData);
			result.put("b", pd2);
		}
		result.put("g_id", pd.get("g_id"));
		return result;
	}

	/**
	 * 
	 * @Title:
	 * @Description:裁判获取双方支持人数
	 * @version
	 * @author lifutao
	 * @date 2016年7月8日下午3:41:42
	 */
	@SuppressWarnings("unchecked")
	public PageData getThumbUpNum(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData pData = new PageData();
		List<PageData> thumbUpA = (List<PageData>) dao.findForList("GameMapper.getThumbUpNumA", pd);
		pData.put("a", thumbUpA.size());
		List<PageData> thumbUpB = (List<PageData>) dao.findForList("GameMapper.getThumbUpNumB", pd);
		pData.put("b", thumbUpB.size());
		return pData;
	}

	/**
	 * 
	 * @Title:
	 * @Description:获取比赛者信息及各自点赞者人数
	 * @version
	 * @author lifutao
	 * @throws Exception
	 * @date 2016年7月8日下午3:45:12
	 */
	public PageData getThumbUpInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData result = new PageData();
		pd.put("a", pd.get("g_a"));
		pd.put("b", pd.get("g_b"));
		// 查询pay_record表，得到点赞人集合
		List<PageData> thumbUpA = (List<PageData>) dao.findForList("GameMapper.getThumbUpNumA", pd);
		if (thumbUpA != null) {
			result.put("a_num", thumbUpA.size());
		} else {
			result.put("a_num", 0);
		}
		List<PageData> thumbUpUserList = new ArrayList<>();
		// 遍历点赞人集合，得到点赞人id，去user表查询点赞人详情
		for (int i = 0; i < thumbUpA.size(); i++) {
			PageData p = thumbUpA.get(i);
			PageData pData = (PageData) dao.findForObject("GameMapper.getThumbUpUserInfo", p);
			pData.put("fee", p.get("pr_total_fee"));
			thumbUpUserList.add(pData);
		}
		result.put("list", thumbUpUserList);
		List<PageData> thumbUpB = (List<PageData>) dao.findForList("GameMapper.getThumbUpNumB", pd);
		if (thumbUpB != null) {
			result.put("b_num", thumbUpB.size());
		} else {
			result.put("b_num", 0);
		}
		return result;
	}

	public PageData getGameIdBySid(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("Y-MM-dd HH:mm:ss");
		pd.put("beginTime", sdf.format(new Date(date.getTime() - 30 * 60 * 1000)));
		List<PageData> list = (List<PageData>) dao.findForList("GameMapper.getGameIdBySid", pd);
		if (list.size() == 0) {
			return null;
		}
		if(list.get(0).get("g_a")!=null&list.get(0).get("g_b")!=null&(int)list.get(0).get("g_finish")!=1){
			return list.get(0);
		}
		
		return null;
	}

	/**
	 * 
	 * @Title: eyun_wrist_wrestling
	 * @Description:
	 * @version
	 * @author guolele
	 * @date 2016年7月8日上午11:25:45
	 */
	public void updateBeginStatu(PageData pd) throws Exception {

		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = format.format(date);
		pd.put("create_time", dateTime);
		dao.update("GameMapper.updateGameBeginStatu", pd);

	}

	/**
	 * 
	 * @Title: eyun_wrist_wrestling
	 * @Description:
	 * @version
	 * @author guolele
	 * @date 2016年7月8日上午11:25:40
	 */
	public PageData cancelTheGame(PageData pd) throws Exception {
		dao.update("GameMapper.cancelGameStatu", pd);
		int createGameAndReturnGameId = this.createGameAndReturnGameId(pd);
		PageData pD = new PageData();
		pD.put("g_id", createGameAndReturnGameId);
		return pD;
	}

	/**
	 * 
	 * @Title: eyun_wrist_wrestling
	 * @Description:
	 * @version
	 * @author guolele
	 * @date 2016年7月8日下午5:08:33
	 */
	public void updateTheWinner(PageData pd) throws Exception {
		dao.save("GameMapper.saveGameWinnerInfo", pd);

		//微信为点赞获胜者发放红包
		PageData redPacketCount = (PageData)dao.findForObject("com.liver_cloud.dao.order.PayRecordDao.redPacketCount", pd);
		if(Integer.parseInt(redPacketCount.get("a")+"")!=0){
			double sum = (Integer.parseInt(redPacketCount.get("a")+"")*100+Integer.parseInt(redPacketCount.get("b")+"")*100)/Integer.parseInt(redPacketCount.get("a")+"");
			//Log.info("decideTheWinner11111111111111111111111----sum"+sum+"redPacketCount-a"+redPacketCount.get("a")+"------b"+redPacketCount.get("a"));
			List<PageData> winnerList = (List<PageData>)dao.findForList("com.liver_cloud.dao.order.PayRecordDao.getWinnerList", pd);
			for(int i = 0;i<winnerList.size();i++){

				if(Integer.parseInt(winnerList.get(i).get("pr_redPacket_status")+"")==0){
					Log.info("用户"+winnerList.get(i).get("u_id")+"---开始发送钱包！！！！");
					String billNo = WechatRedPacketCore.createBillNo(pd.get("g_id")+"");
					SortedMap<String, String> map = WechatRedPacketCore.createSendRedPacketParam(billNo, winnerList.get(i).get("openid")+"", (int)Math.floor(sum),"123.56.159.169");
					WechatRedPacketCore.sign(map);
					String requestXml = WechatRedPacketCore.getRequestXml(map);
					String keystoreFile = "/usr/local/wechatKeystore/apiclient_cert.p12";
//				String keystoreFile = "d:/apiclient_cert.p12";
					FileInputStream instream=null;
					try {
						instream = new FileInputStream(new File(keystoreFile));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					pd.put("pr_trade_no", winnerList.get(i).get("pr_trade_no"));
					try {
						RedPacketResult post = WechatRedPacketCore.post(requestXml, instream);
						if(post.getReturnCode().equals("SUCCESS")&post.getResultCode().equals("SUCCESS")){
							dao.update("com.liver_cloud.dao.order.PayRecordDao.updateRedPacketStatus", pd);
						}
						Log.info("用户"+winnerList.get(i).get("u_id")+"---钱包发送成功！！！！");

					} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException
							| KeyStoreException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * 
	 * @Title: eyun_wrist_wrestling
	 * @Description:
	 * @version
	 * @author guolele
	 * @throws Exception
	 * @date 2016年7月8日下午4:53:00
	 */
	public PageData findBeginStatu(PageData pd) throws Exception {
		return (PageData) dao.findForObject("GameMapper.getGameBeginInfo2", pd);

	}

	public List<PageData> getShopRecord(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		List<PageData> result = new ArrayList<>();
		// 获得s_id对应的game信息 PageData pData :list
		List<PageData> list = (List<PageData>) dao.findForList("GameMapper.getGameBySid", pd);
		for (int i = 0; i < list.size(); i++) {
			// 通过game信息获得比赛者信息
			PageData pDataTemp = new PageData();
			PageData pData = list.get(i);
			pData.put("g_a", pData.get("a"));
			pData.put("g_b", pData.get("b"));
			PageData pd_uerA = (PageData) dao.findForObject("GameMapper.getCompetitorsInfoA", pData);
			PageData pd_uerB = (PageData) dao.findForObject("GameMapper.getCompetitorsInfoB", pData);
			// 获取参赛者各自的对应支持人数
			PageData thumbUpNum = getThumbUpNum(pData);
			if (thumbUpNum != null) {
				pd_uerA.put("num", thumbUpNum.get("a"));
				pd_uerB.put("num", thumbUpNum.get("b"));
			} else {
				pd_uerA.put("num", 0);
				pd_uerB.put("num", 0);
			}
			if ((int) pData.get("winner") == (int) pd_uerA.get("u_id")) {
				pDataTemp.put("a", pd_uerA);
				pDataTemp.put("b", pd_uerB);
			} else {
				pDataTemp.put("a", pd_uerB);
				pDataTemp.put("b", pd_uerA);
			}
			pDataTemp.put("level", pData.get("level"));
			pDataTemp.put("g_id", pData.get("g_id"));
			result.add(pDataTemp);
		}
		return result;
	}

	/**
	 * 保存参赛者id
	 * 
	 * @param gameId
	 * @param userType
	 * @throws Exception
	 */
	public void saveCompetitorId(Integer userId, String gameId, String userType) {
		PageData pd = new PageData();
		pd.put("g_id", gameId);
		pd.put("u_id", userId);
		Log.info("进入方法saveCompetitorId,userId:gameId:userType" + userId + ":" + gameId + ":" + userType);

		try {
			PageData gameInfo = findGameByGameId(pd);
			if ("2".equals(userType)) {
				if (gameInfo != null && gameInfo.get("g_a") == null) {
					participateGameA(pd);
					Log.info("进入方法saveCompetitorId,userId:gameId:userType" + userId + ":" + gameId + ":" + userType);

				} else if (gameInfo != null && gameInfo.get("g_b") == null && gameInfo.get("g_a") != null&&(int)gameInfo.get("g_a")!=userId) {
					Log.info("进入方法saveCompetitorId,userId:gameId:userType" + userId + ":" + gameId + ":" + userType);

					participateGameB(pd);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 
	 * @Title:
	 * @Description:检查游戏状态是否开始。true为开始，false为未开始
	 * @version
	 * @author lifutao
	 * @throws Exception
	 * @date 2016年7月13日上午10:39:21
	 */
	public boolean checkGameBeginStatus(String gameId) {
		try {
			PageData pd = new PageData();
			pd.put("g_id", gameId);
			pd = findBeginStatu(pd);
			if (pd != null && 1 == (int) pd.get("g_begin")) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public int createGameAndReturnGameId(PageData pd) throws Exception {
		// 先查询后插入
		// 根据s_id按时间排序查询创建的game，判断finish字段是否为1
		List<PageData> list = (List) dao.findForList("GameMapper.findGameId", pd);
		// 商户第一次创建游戏
		if (list.size()==0) {
			insertGame(pd);
			return findInsertGameId(pd);
		}
		Object finish = list.get(0).get("g_finish");
		if ((int) finish == 1) {
			insertGame(pd);
			return findInsertGameId(pd);
		} else {
			return findInsertGameId(pd);
		}
	}

	public PageData getThumbUpSum(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData result = new PageData();
		pd.put("a", pd.get("g_a"));
		pd.put("b", pd.get("g_b"));
		pd.put("winner", pd.get("g_a"));
		// 查询pay_record表，得到点赞人集合
		List<PageData> thumbUpA = (List<PageData>) dao.findForList("GameMapper.getThumbUpNumA", pd);
		List<PageData> thumbUpUserList = new ArrayList<>();
		PageData redPacketCount = (PageData) dao.findForObject("com.liver_cloud.dao.order.PayRecordDao.redPacketCount",
				pd);
		Double sum = (Double.parseDouble(redPacketCount.get("a")+"")+Double.parseDouble(redPacketCount.get("b")+""))/Integer.parseInt(redPacketCount.get("a")+"");
		//如果胜利 者没有支持者，返回空的结果
		if (thumbUpA.size() == 0) {
			result.put("list", thumbUpUserList);
			return result;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  
		String format = df.format(sum);
		//user表查询点赞者详情
		for (int i = 0; i < thumbUpA.size(); i++) {
			PageData p = thumbUpA.get(i);
			PageData pData = (PageData) dao.findForObject("GameMapper.getThumbUpUserInfo", p);
			pData.put("fee", format);		
			thumbUpUserList.add(pData);
		}
		result.put("list", thumbUpUserList);		
		return result;
	}

	
}

