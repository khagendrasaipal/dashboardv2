package org.saipal.dboard.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;

import org.apache.hc.core5.http.HttpResponse;
import org.codehaus.jettison.json.JSONObject;
import org.saipal.dboard.config.ContextHolder;
import org.saipal.fmisutil.util.DB;
import org.saipal.fmisutil.util.HttpRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends SuperService {

	@Autowired
	PasswordEncoder en;

	@Autowired
	DB db;

	// private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> customFormFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> ignorableFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public User auth(String username, String password) {
		Map<String, Object> data = new HashMap<>();
		List<User> users = new ArrayList<>();

		String sql = "select id as uid,email as loginname, password,orgid,usertype from users where email=?";
		Tuple t = db.getSingleResult(sql,Arrays.asList(username));
		if(t!=null) {
			if(en.matches(password, t.get("password") + "")) {
				return new User(t.get("uid") + "", t.get("loginname") + "", t.get("password") + "", t.get("orgid") + "",t.get("usertype") + "");
			}
		}
		return null;
	}
	
	public User auths(String username, String password) {
		Map<String, Object> data = new HashMap<>();
		List<User> users = new ArrayList<>();

		String sql = "select id as uid,email as loginname, password,orgid,usertype from users where email=?";
		Tuple t = db.getSingleResult(sql,Arrays.asList(username));
		if(t!=null) {
//			if(en.matches(password, t.get("password") + "")) {
				return new User(t.get("uid") + "", t.get("loginname") + "", t.get("password") + "", t.get("orgid") + "",t.get("usertype") + "");
//			}
		}
		return null;
	}

	public String getOrgId(String userid, String tenant) {
		String sql = "select orgid from users where id=?";
		Tuple tpl = db.getSingleResult(sql, Arrays.asList(userid));
		if(tpl!=null) {
			return tpl.get("orgid")+"";
		}
		return "";
	}
	public String getString(Object o) {
		return o == null ? "" : o.toString();
	}
	
	public Map<String, String> getUserInfo(String userid) {
		//db.execute("update hr_user_info set ");
		//String query = "select loginname,empid,u.orgid,u.adminid,u.adminlevel,issuperuser,dbo.getfiscalyear(getdate())as fiscalyear, dbo.getfyid('') as fyid,"
		//		+ " dbo.getNepMonth(dbo.getEngdate('')) as nepmonth,(select top 1 orgnamenp from admin_org_str where orgidint=u.orgid ) as orgname,(select orgnamenp from admin_org_str where orgidint=(select top 1 vcid from admin_org_str where orgidint=u.orgid )) as lgname from hr_user_info as u "
		//		+ " inner join hr_user_type as ut on u.usertype=ut.usertypeid "
		//		+ " where uid=" + userid +" and userstatus=1 ";
//		String query = "select loginname,empid,u.orgid,u.adminid,u.adminlevel,issuperuser,'2078' fiscalyear,'18'as fyid,"
//						+ " '12' as nepmonth,'Dummy org' as orgname,'LGDUMMy' as lgname from hr_user_info as u "
//						+ " inner join hr_user_type as ut on u.usertype=ut.usertypeid "
//						+ " where uid=" + userid +" and userstatus=1 ";
		String query = "select email as loginname,u.id as empid,u.orgid as orgid,u.usertype,ot.adm_id as adminid,ot.adm_level as adminlevel,"
				+ " ot.name as orgname from users as u "
				+ " inner join organization as ot on u.orgid=ot.id "
				+ " where u.id=" + userid +" ";
		
		Map<String, String> map = new HashMap<>();
		Tuple record = db.getSingleResult(query);
		//map.put("issuperuser", record.get("issuperuser").toString());
		map.put("loginname", record.get("loginname").toString());
		map.put("empid", record.get("empid").toString());
		map.put("adminlevel", record.get("adminlevel").toString());
		map.put("adminid", record.get("adminid").toString());
		map.put("orgid", record.get("orgid").toString());
		map.put("usertype", record.get("usertype").toString());
//		map.put("darbandiid", record.get("darbandiid").toString());
//		map.put("officeid", record.get("officeid").toString());
		//map.put("fiscalyear", record.get("fiscalyear").toString());
		//map.put("fyid", record.get("fyid").toString());
		//map.put("nepmonth", record.get("nepmonth").toString());
		map.put("orgname", record.get("orgname").toString());
		//map.put("lgname", record.get("lgname").toString());
		map.put("language","Np");
//		System.out.println("upto here login");
		return map;
	}
	
	public Map<String, String> getUserInfoAdmin(String userid) {
		//db.execute("update hr_user_info set ");
		//String query = "select loginname,empid,u.orgid,u.adminid,u.adminlevel,issuperuser,dbo.getfiscalyear(getdate())as fiscalyear, dbo.getfyid('') as fyid,"
		//		+ " dbo.getNepMonth(dbo.getEngdate('')) as nepmonth,(select top 1 orgnamenp from admin_org_str where orgidint=u.orgid ) as orgname,(select orgnamenp from admin_org_str where orgidint=(select top 1 vcid from admin_org_str where orgidint=u.orgid )) as lgname from hr_user_info as u "
		//		+ " inner join hr_user_type as ut on u.usertype=ut.usertypeid "
		//		+ " where uid=" + userid +" and userstatus=1 ";
//		String query = "select loginname,empid,u.orgid,u.adminid,u.adminlevel,issuperuser,'2078' fiscalyear,'18'as fyid,"
//						+ " '12' as nepmonth,'Dummy org' as orgname,'LGDUMMy' as lgname from hr_user_info as u "
//						+ " inner join hr_user_type as ut on u.usertype=ut.usertypeid "
//						+ " where uid=" + userid +" and userstatus=1 ";
		String query = "select username,cast(u.organization as CHAR) as orgid,"
				+ " ot.orgnamenp as orgname from xcs_users as u "
				+ " inner join admin_org_strs as ot on u.organization=ot.orgidint "
				+ " where u.id=" + userid +" and u.status=1 ";
		
		Map<String, String> map = new HashMap<>();
		Tuple record = db.getSingleResult(query);
		//map.put("issuperuser", record.get("issuperuser").toString());
		map.put("loginname", record.get("username").toString());
		map.put("orgid", record.get("orgid").toString());
		map.put("orgname", record.get("orgname").toString());
		map.put("language","Np");
		return map;
	}
	

	public Map<String, Object> findUserByUserId(String userId) {
		String projectId = ContextHolder.getTenant();
		// LOG.info(ContextHolder.getTenant());
		Map<String, Object> data = new HashMap<>();
		String sql = "select uid,loginname,password,userstatus from hr_user_info where uid=? and projectid=? and userstatus=1";
		List<Tuple> tList = db.getResultList(sql, Arrays.asList(userId, projectId));
		if (tList.size() == 0) {
			data.put("message", "User not found for given username");
		} else {
			data.put("user", new User(tList.get(0).get("uid") + "", tList.get(0).get("loginname") + "",
					tList.get(0).get("password") + "",tList.get(0).get("orgid") + "",""));
		}
		return data;

	}

	public User findByUserName(String userName) {
		Object user = findUserByUserId(userName).get("user");
		return user == null ? null : (User) user;
	}

	public Map<String, Object> authAdmin(String username, String password) {
		String projectId = ContextHolder.getTenant();
		Map<String, Object> data = new HashMap<>();
		List<User> users = new ArrayList<>();

		String sql = "select id as uid,username as loginname, password from xcs_users  where username=? and projectid=?";
		List<Tuple> tList = db.getResultList(sql,Arrays.asList(username,projectId));
		tList.forEach((t) -> {
			if (en.matches(password, t.get("password") + "")) {
				users.add(new User(t.get("uid") + "", t.get("loginname") + "", t.get("password") + "",t.get("orgid") + "",""));
			}
		});
		if (users.size() > 1) {
			data.put("message", "More than one account exists for " + username);
		} else if (users.size() == 0) {
			data.put("message", "Invalid Username or password");
		} else {
			User u = users.get(0);
			u.setPassword("");
			data.put("user", users.get(0));
		}
		return data;
	}
	
	public User loginUsingDhis2() {
		try {
			String burl = "http://hmis.gov.np/hmis";
			//String burl = "https://play.dhis2.org/2.38.0";
			String url = burl+"/uaa/oauth/token";
			String username = request("username");
			String password = request("password");
			HttpRequest hreq = new HttpRequest();
			String userPass = "opml.dashboard:#Mis@2020";
			//String userPass = "opml.dashboard:29fb10764-921b-b86f-3ce3-ea46d8e995f";
			hreq.setHeader("Authorization", "Basic "+Base64.getEncoder().encodeToString(userPass.getBytes()));
			hreq.setHeader("Content-Type", "application/x-www-form-urlencoded");
			hreq.setParam("grant_type", "password");
	        hreq.setParam("username", username);
	        hreq.setParam("password", password);
	        JSONObject resp = hreq.post(url);
	        if(resp.getInt("status_code")== 200) {
	        	JSONObject data = resp.getJSONObject("data");
	        	String accessToken = data.getString("access_token");
	        	url = burl+"/api/me?fields=organisationUnits[id]";
	        	HttpRequest hreq1 = new HttpRequest();
	        	JSONObject resp1 = hreq1.setHeader("Authorization","Bearer "+accessToken).get(url);
	        	if(resp1.getInt("status_code")==200) {
	        		JSONObject dt = resp1.getJSONObject("data");
	        		String orgId = dt.getJSONArray("organisationUnits").getJSONObject(0).getString("id");
	        		Tuple t = db.getSingleResult("select * from organization where uid=? limit 1",Arrays.asList(orgId));
	        		if(t != null) {
	        			Tuple u = db.getSingleResult("select * from users where orgid=? limit 1",Arrays.asList(t.get("id")));
	        			if(u != null) {
	        				return new User(u.get("id") + "", u.get("email") + "", u.get("password") + "",u.get("orgid") + "",u.get("usertype") + "");
	        			}
	        		}
	        	}
	        }
		}catch (Exception e) {
			e.getStackTrace();
		}
		return null;
	}

	public int userType(String username) {
		return 1;
	}
}
