package com.liver_cloud.service.appointment_info;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liver_cloud.dao.DaoSupport;
import com.liver_cloud.util.PageData;

@Service
public class Appointment_InfoService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public List<PageData> selectByAdId(PageData app_date) throws Exception {
		// TODO Auto-generated method stub
		return (List<PageData>) dao.findForList("Appointment_InfoMapper.selectByAdId", app_date);
	}

	public void insertInfo(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.save("Appointment_InfoMapper.insertInfo", pd);
		
	}

	public PageData selectByReferee(PageData param) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("Appointment_InfoMapper.selectByReferee", param);

	}

	public PageData selectInfoByDateAndSid(PageData param) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("Appointment_InfoMapper.selectByDateAndSid", param);
	}

	public PageData selectByUidAndAdId(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("Appointment_InfoMapper.selectByUidAndAdId", pd);
	}

	

}
