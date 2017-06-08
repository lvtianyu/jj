package com.liver_cloud.controller.game;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liver_cloud.controller.base.BaseController;
import com.liver_cloud.service.appointment_date.Appointment_DateService;
import com.liver_cloud.service.game.GameService;
import com.liver_cloud.util.EnumErrorCode;
import com.liver_cloud.util.PageData;

/**
 * 类名称：GameController
 * @author lifutao
 *
 */
@Controller
@RequestMapping(value="/game")
public class GameController extends BaseController {
	
	@Resource(name="gameService")
	private GameService gameService;	
	
	@Resource(name="appointment_DateService")
	private Appointment_DateService appointment_DateService;
	
	/**
	 * 
	 * @Title: 
	 * @Description: 保存商户id，并生成gameId返回给商户（商户扫描二维码调此接口）
	 * @version 
	 * @author lifutao 
	 * @throws Exception 
	 * @date 2016年7月5日下午5:48:00
	 */
	@ResponseBody
	@RequestMapping(value="/generateCompetitionQrCode")
	public PageData createGame() throws Exception{
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			int gameId = gameService.createGameAndReturnGameId(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", gameId);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			pd.clear();
			pd.put("result", "error");
			pd.put("gameId", "");
			pd.put("errormsg", EnumErrorCode.CODE_000010.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000010.code);
			return pd;
		}
		
	}
	/**
	 * 
	 * @Title: 
	 * @Description: 参赛者扫描二维码报名活动
	 * @version 
	 * @author lifutao 
	 * @throws Exception 
	 * @date 2016年7月5日下午5:48:00
	 */
	@ResponseBody
	@RequestMapping(value="/participateInGame")
	public PageData participateGame() {
//		PageData pd = new PageData();
//		try {
//			pd = this.getPageData();
//			PageData  pData = gameService.findGameByGameId(pd);
//			if(pData!=null&&pData.get("g_a")!=null&&pData.get("g_b")!=null){
//				pd.clear();
//				pd.put("result", "ok");
//				pd.put("errormsg", EnumErrorCode.CODE_000012.msg);
//				pd.put("errorcode", EnumErrorCode.CODE_000012.code);
//				return pd;
//			}
//			if(pData!=null&&pData.get("g_a")==null){
//				gameService.participateGameA(pd);
//				pd.clear();
//				pd.put("result", "ok");
//				pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
//				pd.put("errorcode", EnumErrorCode.CODE_000000.code);
//				return pd;
//			}
//			if(pData!=null&&pData.get("g_b")==null){
//				gameService.participateGameB(pd);
//				pd.clear();
//				pd.put("result", "ok");
//				pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
//				pd.put("errorcode", EnumErrorCode.CODE_000000.code);
//				return pd;
//			}
//			return null;
//		} catch (Exception e) {
//			pd.clear();
//			pd.put("result", "error");
//			pd.put("gameId", "");
//			pd.put("errormsg", EnumErrorCode.CODE_000011.msg);
//			pd.put("errorcode", EnumErrorCode.CODE_000011.code);
			return null;
//		}
		
	}
	/**
	 * 
	 * @Title: 
	 * @Description:根据gameId获得双方参赛者的信息
	 * @version 
	 * @author lifutao 
	 * @throws Exception 
	 * @date 2016年7月8日上午10:42:20
	 */
	@ResponseBody
	@RequestMapping(value="/getCompetitorsInfo")
	public PageData getCompetitorsInfo()  {
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData result = gameService.getCompetitorsInfo(pd);
			pd.clear();
			if(result.keySet().size()==3){
				pd.put("result", "ok");
				pd.put("values", result);
				pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000000.code);
				return pd;
			}else{
				pd.put("result", "error");
				pd.put("errormsg", EnumErrorCode.CODE_000014.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000014.code);
				return pd;
			}
			
		} catch (Exception e) {
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000013.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000013.code);
			return pd;
		}
	}
	/**
	 * 
	 * @Title: 
	 * @Description:裁判获取支持各参赛者的人数
	 * @version 
	 * @author lifutao 
	 * @date 2016年7月8日下午3:17:38
	 */
	@ResponseBody
	@RequestMapping(value="/getThumbUpNum")
	public PageData getThumbUpNum() throws Exception{
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData pData = gameService.getThumbUpNum(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", pData);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000015.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000015.code);
			return pd;
		}
	}
	/**
	 * 
	 * @Title: 
	 * @Description:获取点赞人纪录
	 * @version 
	 * @author lifutao 
	 * @date 2016年7月8日下午3:33:15
	 */
	@ResponseBody
	@RequestMapping(value="/getThumbUpInfo")	
	public PageData getThumbUpInfo(){
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData pData = gameService.getThumbUpInfo(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", pData);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000023.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000023.code);
			return pd;
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/getGameInfoBySid")
	/**
	 * 
	 * @Title: 根据商家列表页面的商家ID获取最近正在举行的活动
	 * @Description:
	 * @version 
	 * @author qinhuiwei
	 * @date 2016年7月8日下午4:06:41
	 */
	public PageData getGameInfoBySid() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		try {
			pd = gameService.getGameIdBySid(pd);
			if(null == pd){
				PageData pageData = new PageData();
				pageData.put("result", "error");
				pageData.put("values", "");
				pageData.put("errormsg", EnumErrorCode.CODE_000022.msg);
				pageData.put("errorcode", EnumErrorCode.CODE_000022.code);
				return pageData;
			}
			PageData competitorsInfo = gameService.getCompetitorsInfo(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", competitorsInfo);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000023.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000023.code);
			return pd;
		}
	}
    
    /**
     * 
	 * @Title: eyun_wrist_wrestling
	 * @Description:游戏开始状态
	 * @version 
	 * @author guolele 
	 * @date 2016年7月7日下午6:02:28
	 */
	@RequestMapping(value="/beginTheGame")
	@ResponseBody
	public PageData beginTheGame(){
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			pd.put("g_begin", 1);
			gameService.updateBeginStatu(pd);
			pd.put("a", pd.get("a"));
			pd.put("b", pd.get("b"));
			PageData pDate = gameService.getThumbUpNum(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values",pDate);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000016.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000016.code);
			return pd;
		}
	}
	

	
	/**
	 * 
	 * @Title: 
	 * @Description:点赞者获取店铺历史纪录
	 * @version 
	 * @author lifutao 
	 * @throws Exception 
	 * @date 2016年7月9日下午3:23:53
	 */
	@ResponseBody
	@RequestMapping(value="/getShopRecord")
	public PageData getShopRecord() throws Exception{
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			List<PageData> value = gameService.getShopRecord(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", value);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
			
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000023.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000023.code);
			return pd;
		}
	}


	/**
	 * 
	 * @Title: eyun_wrist_wrestling
	 * @Description: 取消游戏
	 * @version 
	 * @author guolele 
	 * @date 2016年7月8日下午1:43:15
	 */
	@RequestMapping(value="/cancelTheGame")
	@ResponseBody
	public PageData cancelTheGame(){
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			pd.put("g_cancel", 1);
			pd.put("g_finish", 1);
			PageData pD = gameService.cancelTheGame(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", pD);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000017.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000017.code);
			return pd;
		}
	}
	/**
     * 
	 * @Title: eyun_wrist_wrestling
	 * @Description: 裁判决定参赛者胜负
	 * @version 
	 * @author guolele 
	 * @throws Exception 
	 * @date 2016年7月8日下午1:45:28
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/decideTheWinner")
	@ResponseBody
	public PageData decideTheWinner() throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData fbs = gameService.findBeginStatu(pd);
		if (1 == (int)(fbs.get("g_begin"))) {
			try {
				pd.put("g_finish", 1);
				gameService.updateTheWinner(pd);
				pd.clear();
				pd.put("result", "ok");
				pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000000.code);
				return pd;
			}catch (Exception e) {
				pd.clear();
				pd.put("result", "error");
				pd.put("values", "");
				pd.put("errormsg", EnumErrorCode.CODE_000018.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000018.code);
				return pd;
			}
		}else {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000024.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000024.code);
			return pd;
		}
	}
	/**
	 * 
	 * @Title: 
	 * @Description:获取点赞人金额
	 * @version 
	 * @author lifutao 
	 * @date 2016年7月14日下午2:19:10
	 */
	@ResponseBody
	@RequestMapping(value="getThumbUpSum")
	public PageData getThumbUpSum(){
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData values  = gameService.getThumbUpSum(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", values);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000023.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000023.code);
			return pd;
		}	
	}
	
	
	/**
	 * 
	 * @Title: 
	 * @Description:裁判添加举办活动日期
	 * @version 
	 * @author lifutao 
	 * @throws Exception 
	 * @date 
	 */
	@ResponseBody
	@RequestMapping(value="addAppointmentDate")
	public PageData addAppointmentDate() throws Exception{
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			//向date表插入信息
			if(pd.getString("s_id").isEmpty()||pd.getString("date").isEmpty()){
				pd.clear();
				pd.put("result", "ok");
				pd.put("errormsg", EnumErrorCode.CODE_000037.msg);
				pd.put("errorcode", EnumErrorCode.CODE_000037.code);
				return pd;	
			}
			appointment_DateService.batchInsertDate(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
	} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000035.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000035.code);
			return pd;
		}
		
	}
	
			
}
