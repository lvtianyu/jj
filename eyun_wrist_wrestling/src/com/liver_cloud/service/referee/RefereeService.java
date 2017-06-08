package com.liver_cloud.service.referee;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liver_cloud.dao.DaoSupport;
import com.liver_cloud.util.PageData;


@Service
public class RefereeService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public PageData selectRefeByMobile(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		return (PageData) dao.findForObject("RefereeMapper.selectRefeByMobile", pd);
	}

	public void updatePassword(PageData pd) throws Exception {
		// TODO Auto-generated method stub
		dao.update("RefereeMapper.updatePassword", pd);		
	}

}
