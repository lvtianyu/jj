package com.liver_cloud.controller.shop;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liver_cloud.controller.base.BaseController;
import com.liver_cloud.service.game.GameService;
import com.liver_cloud.service.shop.ShopService;
import com.liver_cloud.util.EnumErrorCode;
import com.liver_cloud.util.PageData;
@Controller
@RequestMapping(value="/shop")
public class ShopController extends BaseController{
	
	@Resource(name="shopService")
	private ShopService shopService;
	@Resource(name="gameService")
	private GameService gameService;
	/**
	 * 
	 * @Title:查询附近商家列表 
	 * @Description:
	 * @version 
	 * @author qinhuiwei
	 * @throws Exception 
	 * @date 2016年7月7日下午1:51:29
	 */
	@ResponseBody
	@RequestMapping(value="/getShopList")
	public PageData getShopList() throws Exception{
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			List<PageData> shopList =  shopService.getShopList(pd);
			for(int i = 0;i<shopList.size();i++){
				PageData pageData = new PageData();
				pageData.put("s_id", shopList.get(i).get("s_id"));
				PageData gameIdBySid = gameService.getGameIdBySid(pageData);
				if(null == gameIdBySid){
					shopList.get(i).put("activity", 0);
				}else{
					shopList.get(i).put("activity", 1);
				}
			}
			
			pd.remove("longitude");
			pd.remove("latitude");
			pd.put("result", "ok");
			pd.put("values", shopList);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("values", "");
			pd.put("errormsg", EnumErrorCode.CODE_000003.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000003.code);
			return pd;
		}
	}
	
	/**
	 * 
	 * @Title:查询商家详细信息
	 * @Description:
	 * @version 
	 * @author qinhuiwei
	 * @throws Exception 
	 * @date 2016年7月7日下午1:51:29
	 */
	@ResponseBody
	@RequestMapping(value="/getShopInfo")
	public PageData getShopInfo(){
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData pageData = shopService.getShopInfo(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", pageData);
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
	 * @Title:根据前台传来的年月日查询报名详细信息
	 * @Description:
	 * @version 
	 * @author lifutao
	 * @throws Exception 
	 * @date 
	 */
	@ResponseBody
	@RequestMapping(value="/getAppointmentInfoByDate")
	public PageData getAppointmentInfoByDate() throws Exception{
		PageData pd = new PageData();
		try {
			pd = this.getPageData();
			PageData result = shopService.getAppointmentInfoByDate(pd);
			pd.clear();
			pd.put("result", "ok");
			pd.put("values", result);
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;						
		} catch (Exception e) {
			// TODO: handle exception
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000023.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000023.code);
			return pd;
		}
		
		
		
	}
	
	
}
