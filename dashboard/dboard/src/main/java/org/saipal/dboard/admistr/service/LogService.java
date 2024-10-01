package org.saipal.dboard.admistr.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.saipal.dboard.service.AutoService;
import org.springframework.stereotype.Component;

@Component
public class LogService extends AutoService {
	
	public void logLogins(String userId,String orgId) {
		db.execute("insert into log(action,userid,orgid,dateint) values (?,?,?,?)",Arrays.asList("login",userId,orgId,currentDateToInt()));
	}
	
	public void logDashboardScreenShow(HttpServletRequest request) {
		String orgId = request("orgid");
		String ss=request("ss");
		String info=request("infos");
		System.out.println("screen size: "+ss);
		String ip1=request.getHeader("X-Forwarded-For");
		String ip = request.getRemoteAddr();
		if(ip1==null) {
			ip1=ip;
		}
		if(ip1.isBlank()) {
			ip1=ip;
		}
		db.execute("insert into log(action,orgid,dateint,ip,other_details,info) values (?,?,?,?,?,?)",Arrays.asList("screen",orgId,currentDateToInt(),ip1.split(":")[0].trim(),ss,info));
	}
	
	public void logPortalView(HttpServletRequest request) {
		String orgId = request("orgid");
		String ip1=request.getHeader("X-Forwarded-For");
		String ip = request.getRemoteAddr();
		if(ip1==null) {
			ip1=ip;
		}
		if(ip1.isBlank()) {
			ip1=ip;
		}
		db.execute("insert into log(action,orgid,dateint,ip) values (?,?,?,?)",Arrays.asList("portal",orgId,currentDateToInt(),ip1.split(":")[0].trim()));
	}
	
	public String currentDateToInt() {
		String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return dt.replaceAll("-","");
	}
}
