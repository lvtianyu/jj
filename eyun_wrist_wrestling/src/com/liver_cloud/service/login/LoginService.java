package com.liver_cloud.service.login;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.liver_cloud.dao.DaoSupport;
import com.liver_cloud.service.appointment_date.Appointment_DateService;
import com.liver_cloud.service.appointment_info.Appointment_InfoService;
import com.liver_cloud.service.referee.RefereeService;
import com.liver_cloud.util.EnumErrorCode;
import com.liver_cloud.util.PageData;

@Service
public class LoginService {
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	@Resource(name="refereeService")
	private RefereeService refereeService;
	
	@Resource(name="appointment_DateService")
	private Appointment_DateService  appointment_DateService; 
	
	@Resource(name="appointment_InfoService")
	private Appointment_InfoService  appointment_InfoService;
	
	
	

	public PageData getLoginResult(PageData pd) throws Exception {
		// TODO Auto-generated method stub	
		//通过手机号查询用户是否注册	
		PageData referee = refereeService.selectRefeByMobile(pd);
		if(referee==null){
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000025.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000025.code);
			return pd;
		}
		//校验密码参数是否为空
		if(pd.getString("r_password").isEmpty()){
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000026.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000026.code);
			return pd;
		}
		//校验输入密码是否正确
		if(!referee.getString("r_password").equals(pd.getString("r_password"))){
			pd.clear();
			pd.put("result", "error");
			pd.put("errormsg", EnumErrorCode.CODE_000027.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000027.code);
			return pd;
		}
		//根据referee返回的s_id，查询date表中离登陆日期最近的信息
		referee.put("date", new Date());
		PageData app_date = appointment_DateService.selectLatestBySid(referee);
		if(app_date==null){
			pd.clear();
			PageData temp = new PageData();
			temp.put("s_id", referee.get("r_s_id"));
			temp.put("s_name", referee.get("r_s_name"));
			pd.put("values", temp);
			pd.put("result", "ok");
			pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
			pd.put("errorcode", EnumErrorCode.CODE_000000.code);
			return pd;
		}
		//根据date表的ad_id查询info表
		 List<PageData> app_info = appointment_InfoService.selectByAdId(app_date);
		 int sum=0;
		 int leve0 =0;
		 int leve1 =0;
		 int leve2 =0;
		 int leve3 =0;
		 for(int i=0;i<app_info.size();i++){
			 if((int)(app_info.get(i).get("ai_g_level"))==0){
				 leve0++;
			 }else if((int)(app_info.get(i).get("ai_g_level"))==1){
				 leve1++;
			 }else if((int)(app_info.get(i).get("ai_g_level"))==2){
				 leve2++;
			 }else if((int)(app_info.get(i).get("ai_g_level"))==3){
				 leve3++;
			 }
		 }
		 sum=app_info.size();
		PageData values = new PageData();
		//Date temp = (Date) app_date.get("ad_date");
		//values.put("date", app_date.get("ad_date"));
		values.put("s_id", app_date.get("ad_s_id"));
		/*values.put("sum", sum);
		values.put("0", leve0);
		values.put("1", leve1);
		values.put("2", leve2);
		values.put("3", leve3);*/
		values.put("s_name", referee.getString("r_s_name"));
		pd.clear();
		pd.put("result", "ok");
		pd.put("values", values);
		pd.put("errormsg", EnumErrorCode.CODE_000000.msg);
		pd.put("errorcode", EnumErrorCode.CODE_000000.code);
		return pd;				
	}




	public PageData insertInfo(PageData pd) throws Exception {
		//根据date和s_id，查询ad_id
		PageData result = new PageData();
		SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formt.parse(pd.getString("date"));
		pd.put("date", date);
		PageData app_date = appointment_DateService.selectBySidAndDate(pd);
		//结合date表中的ad_id，向info表添加信息
		pd.put("ai_ad_id", app_date.get("ad_id"));
		//根据ad_id和u_id查看info表是否今天信息已经存在
		PageData appInfo = appointment_InfoService.selectByUidAndAdId(pd);
		if(appInfo==null){
			appointment_InfoService.insertInfo(pd);	
			result.put("result", "ok");
			result.put("errormsg", EnumErrorCode.CODE_000000.msg);
			result.put("errorcode", EnumErrorCode.CODE_000000.code);
		}else{
			result.put("result", "error");
			result.put("errormsg", EnumErrorCode.CODE_000038.msg);
			result.put("errorcode", EnumErrorCode.CODE_000038.code);
		}
			return result;		
	}		
}
