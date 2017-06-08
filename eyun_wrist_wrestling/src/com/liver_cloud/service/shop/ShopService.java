package com.liver_cloud.service.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liver_cloud.dao.DaoSupport;
import com.liver_cloud.service.appointment_date.Appointment_DateService;
import com.liver_cloud.service.appointment_info.Appointment_InfoService;
import com.liver_cloud.util.PageData;

@Service
public class ShopService {
	
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="appointment_DateService")
	private Appointment_DateService  appointment_DateService; 
	
	@Resource(name="appointment_InfoService")
	private Appointment_InfoService  appointment_InfoService;
	
	public List<PageData> getShopList(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>)dao.findForList("ShopMapper.getShopList",pd);
	}
	public PageData getShopInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		PageData pageData = (PageData)dao.findForObject("ShopMapper.getShopInfo", pd);
		String urls = pageData.get("pictures").toString();
		String[] split = urls.split(",");
		List<String> pictures = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			pictures.add(split[i]);
		}
		pageData.put("pictures", pictures);
		return pageData;
	}
	
	
	public PageData getAppointmentInfoByDate(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		//获取date对应的ad_id，根据ad_id获取info信息
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		PageData result = new PageData();
		result.put("sum", 0);
		result.put("0", 0);
		result.put("1", 0);
		result.put("2", 0);
		result.put("3", 0);	
		if(pd.get("date").toString().equals(" ")){
			//根据s_id去date表查询查询该商家离今天最近的ad_date,并将此值赋值给date参数
			Date temp = new Date();
			pd.put("date", dateFormat.format(temp));
			PageData appDate = appointment_DateService.selectAPPInfoLatestBySid(pd);
			if(appDate==null){
				result.put("date", "");
				return result;
			}
			date = (Date) appDate.get("ad_date");
			pd.put("date", date.toString());
		}else{
			date = dateFormat.parse(pd.getString("date"));			
		}		
		PageData param = new PageData();
		param.put("date", date);
		param.put("s_id", pd.getString("s_id"));
		PageData info = appointment_InfoService.selectInfoByDateAndSid(param);
		if(info!=null&&(long)info.get("sum")!=0){
			result.put("sum", info.get("sum"));
			result.put("0", info.get("level0"));
			result.put("1", info.get("level1"));
			result.put("2", info.get("level2"));
			result.put("3", info.get("level3"));	
		}
		String flag = pd.getString("flag");
		//裁判显示当月信息
		List<PageData> appDate ;
		List<String> timeList = new ArrayList<String>();
		result.put("date", timeList);
		if("0".equals(flag)){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
			String dateTemp = format.format(date);
			param.put("date", dateTemp);
			appDate = appointment_DateService.selectByReferee(param);
			//遍历appDate,查询具体的时间
			for(int i=0;i<appDate.size();i++){
				timeList.add(appDate.get(i).get("ad_date").toString());
			}
			return result;
		}else{
			//显示从当日后所有信息
			appDate = appointment_DateService.selectByApplicant(pd);
			for(int i=0;i<appDate.size();i++){
				timeList.add(appDate.get(i).get("ad_date").toString());
			}
			return result;		
		}	
	}
}

