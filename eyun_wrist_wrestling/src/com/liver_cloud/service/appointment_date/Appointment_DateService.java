package com.liver_cloud.service.appointment_date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liver_cloud.dao.DaoSupport;
import com.liver_cloud.util.PageData;


@Service
public class Appointment_DateService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public PageData selectLatestBySid(PageData referee) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("Appointment_DateMapper.selectLatestBySid", referee);
	}

	public PageData insertDate(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.save("Appointment_DateMapper.insertAppDate", pd);
		PageData appDate = (PageData) dao.findForObject("Appointment_DateMapper.selectBySid", pd);
		return appDate;
	}

	public List<PageData> selectByReferee(PageData param) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("Appointment_DateMapper.selectByReferee", param);
	}

	public PageData selectBySidAndDate(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("Appointment_DateMapper.selectByDateAndSid", pd);
	}

	public List<PageData> selectByApplicant(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("Appointment_DateMapper.selectByApplicant", pd);
	}

	public PageData selectAPPInfoLatestBySid(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("Appointment_DateMapper.selectAPPInfoLatestBySid", pd);
	}

	public void batchInsertDate(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		String [] temp = pd.getString("date").split(",");
		PageData param = new PageData();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<temp.length;i++){
			param.clear();
			param.put("s_id", pd.get("s_id"));
			Date date = format.parse(temp[i]);
			param.put("date", date);
			//查询param下是否有数据
			PageData appDate = (PageData) dao.findForObject("Appointment_DateMapper.selectByDateAndSid", param);
			if(appDate==null){
				dao.save("Appointment_DateMapper.batchInsertDate", param);				
			}
		}
	
	}



}
