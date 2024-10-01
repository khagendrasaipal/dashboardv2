package org.saipal.dboard.contorllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.saipal.dboard.admistr.model.Organization;
import org.saipal.fmisutil.service.AutoService;
import org.saipal.fmisutil.util.DbResponse;
import org.saipal.fmisutil.util.HttpRequest;
import org.saipal.fmisutil.util.Messenger;
import org.saipal.fmisutil.util.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ClientCreateTestService extends AutoService{
	@Autowired
	PasswordEncoder pe;
	private String table = "log";
	public String store() {
		
		ClientCreateTest model = new ClientCreateTest();
		model.loadData(document);
		
		String baseUrl = "http://localhost:8011";
		String client_id=model.client_id;
		String client_name=model.client_name;
		String redirect_uri=model.redirect_uri;
		String client_secret=model.client_secret;
		String status=model.status;
		String urlpart="?client_id="+client_id+"&redirect_uri="+redirect_uri+"&client_name="+client_name+"&client_secret="+client_secret+"&status="+status;
		String url = baseUrl + "/add-clients"+urlpart;
		HttpRequest requests = new HttpRequest();
		JSONObject responses = requests.get(url);
		System.out.println(responses);
		return responses.toString();
	}

	public List<Tuple> getOrgs(HttpServletRequest request) {
//		String sql="select * from organization where id="+request("orgid");
		String orgid=request("orgid");
		String sql = "select organization.*,admin_province.namenp as provincename from organization left join admin_local_level_structure on admin_local_level_structure.vcid=organization.adm_id left join admin_province on admin_province.pid=admin_local_level_structure.provinceid   where organization.id="+orgid;
		if(request("orgid").equals("100")) {
			sql = "select organization.*,'बागमती प्रदेश' as provincename from organization left join admin_local_level_structure on admin_local_level_structure.vcid=organization.adm_id left join admin_province on admin_province.pid=admin_local_level_structure.provinceid   where organization.id="+orgid;
		}
//		String sql = "select organization.*,admin_province.namenp as provincename from organization left join admin_local_level_structure on admin_local_level_structure.vcid=organization.adm_id left join admin_province on admin_province.pid=admin_local_level_structure.provinceid   where organization.id="+orgid;
		
		
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
		
	}

	public ResponseEntity<Map<String, Object>> getProgram() {
		String sql = "select id,title from programme where 1=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(1));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("id", t.get("id"));
				mapadmlvl.put("name", t.get("title"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	public ResponseEntity<Map<String, Object>> getDefault() {
		String sql = "select pid,tid,orgid from indicator_config where status=? and orgid=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(1,session("orgid")));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("pid", t.get("pid"));
				mapadmlvl.put("tid", t.get("tid"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().setData(list).success();		}
	}

	public ResponseEntity<Map<String, Object>> getIndicators(String id) {
		String sql = "select uid,data_elements,data_elements_en,data_elements_np from data_elements where pid=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(id));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("id", t.get("uid"));
				mapadmlvl.put("name", t.get("data_elements_en"));
				mapadmlvl.put("namenp", t.get("data_elements_np"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().error();
		}
	}

	public ResponseEntity<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
		String indId=request("iid");
		String fy=request("fy");
//		String ouid="YqQbkwADI71";
		String sql1="select * from organization where id="+session("orgid");
		System.out.println(session("orgid")+" here ");
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
		String ouid=tList1.get(0).get("uid").toString();
		String pe=preparePeriods(fy);
		String pef = pe.replaceFirst(".$","");
//		System.out.println(pef);
		  String link = "/api/analytics.json?dimension=dx:"+indId+"&dimension=pe:"+pef+"&filter=ou:"+ouid;
//		  String link = "/api/29/analytics?dimension=dx:"+indId+",ou:"+ouid+"&filter=pe:"+pe;
//		  System.out.println(getResponse(link));
		  return Messenger.getMessenger().setData(getResponse(link)).success();
//		return null;
	}

	private String preparePeriods(String fys) {
		String pe = "";
		int i;
        int fy=Integer.parseInt(fys) ;
            int nextFy = fy+1;
            for(i=12;i>0;i--){
                if(i>9){
                    pe +=fy+""+i+";";
                }else{
                    if(i<4){
                        pe +=nextFy+"0"+i+";";
                    }else{
                        pe +=fy+"0"+i+";";
                    }
                }
            }
        // }
//        pe = rtrim(pe,";");
        return pe;
	}

	
	private List<String> getResponse(String link) throws Exception {
		String user = "opml.dashboard";
		String pass= "#Mis@2020";
		String server = "http://202.166.205.218/hmis";
		String uri = link;
		String credentials = "Basic "+Base64.getEncoder().encodeToString((user+":"+pass).getBytes());
		String url=server+uri;
		HttpRequest requests = new HttpRequest();
		JSONObject responses = requests.setHeader("Authorization",credentials)
				.setHeader("Accept", "application/json").get(url);
		JSONObject resp = responses.getJSONObject("data");

		JSONArray datas = resp.getJSONArray("rows");
		Map<Integer,String> mdata = new HashMap<>();
//		System.out.println(datas);
		for (int i = 0 ; i < datas.length(); i++) {
			JSONArray obj = datas.getJSONArray(i);
			int month = Integer.parseInt(obj.getString(1).substring(4));
			mdata.put(month, obj.getString(2));
		}
		Map<Integer,String> months = new LinkedHashMap<>();
		months.put(4,"Shrawn");
		months.put(5,"Bhadra");
		months.put(6,"Ashoj");
		months.put(7,"Kartik");
		months.put(8,"Mangsir");
		months.put(9,"Poush");
		months.put(10,"Magh");
		months.put(11,"Falgun");
		months.put(12,"Chaitra");
		months.put(1,"Baishakh");
		months.put(2,"Jestha");
		months.put(3,"Ashar");
		//("4","Shrawn","5","Bhadra","6","Ashoj","7","Kartik","8","Mangsir","9","Poush","10","Magh","11","Falgun","12","Chaitra","1","Baishakh","2","Jestha","3","Ashar");
		List<Integer> mindex = Arrays.asList(4,5,6,7,8,9,10,11,12,1,2,3);
		List<String> fdata = new ArrayList<>();
		List<String> mnths = new ArrayList<>();
		for(int m:mindex) {
			if(mdata.containsKey(m)) {
				fdata.add(mdata.get(m));
			}else {
				fdata.add("0");
			}
			
			mnths.add(months.get(m));
		}
//		System.out.println(fdata);
		return fdata;

	}

	public ResponseEntity<Map<String, Object>> getComposite() {
		String sql = "select weight,value,fy,concat(fy,\\\"/\\\",fy+1) as fys,composite_indicator.name as indicator from composite_indicators_value join composite_indicator on composite_indicator.id=composite_indicators_value.indicator where orgid=? group by indicator,fy order by fy";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(session("orgid")));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("weight", t.get("weight"));
				mapadmlvl.put("value", t.get("value"));
				mapadmlvl.put("fys", t.get("fys"));
				mapadmlvl.put("indicator", t.get("indicator"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().error();
		}
	}

	public List<Tuple> getChartConfig(HttpServletRequest request) {
		String sql = "select indicator,concat(fy,\\\"/\\\",fy+1) as fys,chart_type,time,data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from public_dashboard_setup join data_elements on data_elements.uid=public_dashboard_setup.indicator where orgid="+request("orgid")+" and public_dashboard_setup.status=0 group by indicator ";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	
	
	public List<Tuple> getmageConfig(HttpServletRequest request) {
		String sql = "select title,image,description,time from infographics where status=1 and image NOT LIKE '%.mp4' and  orgid="+request("orgid");
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	public List<Tuple> getcardConfig(HttpServletRequest request) {
		String sql = "select title,content from basic_information where type='Additional Cards' and status=1 and orgid="+request("orgid");
//		System.out.println(sql);
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	public List<Tuple> getBasic(HttpServletRequest request) {
//		System.out.println(request("orgid")+ " hahhaha");
		String sql = "select ward,population,area_sqkm from ward_details where orgid="+request("orgid")+ " order by ward";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		
		return tList;
	}

	public List<Map<String, Object>> getWeatherInfo(HttpServletRequest request) throws JSONException {
		
		String apikey="b3677a60280c0c3589a6bf27bcab3aeb";
		String sql="select value from indicators_value where orgid=? and indicator=?";
		Map<String, Object> lat = db.getSingleResultMap(sql, Arrays.asList(request("orgid"),8));
//		System.out.println(lat);
		String sql1="select value from indicators_value where orgid=? and indicator=?";
		Map<String, Object> longs = db.getSingleResultMap(sql1, Arrays.asList(request("orgid"),9));	
		String lats="0";
		String lon="0";
		if(lat!=null) {
			System.out.println("i am here weather");
			lats=lat.get("value").toString();
		}
		if(longs!=null) {
			System.out.println("i am here weather");
			lon=longs.get("value").toString();
		}
		 
		 
	
		
		String url="https://api.openweathermap.org/data/2.5/weather?lat="+lats+"&lon="+lon+"&appid="+apikey+"&units=metric";
//		String url="https://api.openweathermap.org/data/2.5/weather?lat=27.89272170659029&lon=86.83190351004616&appid="+apikey+"&units=metric";
		HttpRequest requests = new HttpRequest();
		JSONObject responses = requests.setHeader("Accept", "application/json").get(url);
		JSONObject resp = responses.getJSONObject("data");
		String temp=resp.getJSONObject("main").get("temp").toString();
		JSONArray main=resp.getJSONArray("weather");
		JSONObject resps=main.getJSONObject(0);
		String type=resps.get("main").toString();
		String desc=resps.get("description").toString();
		String icon=resps.get("icon").toString();
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> mapadmlvl = new HashMap<>();
		mapadmlvl.put("temp", temp);
		mapadmlvl.put("type", type);
		mapadmlvl.put("desc", desc);
		mapadmlvl.put("icon", icon);
		list.add(mapadmlvl);
		// TODO Auto-generated method stub
		return list;
	}
	
	
	public ResponseEntity<Map<String, Object>> getDashboardCombine() throws Exception {
		String tid=request("tid");
		String pid=request("pid");
		String orgs=request("orgid");
		String orgid="";
		if("0".equals(orgs)) {
			 orgid=auth.getOrgId();
		}else {
			orgid=orgs;
		}
		String sql="";
		if("5".equals(pid)) {
			 sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements where pid=5 and id in(38,42,35,36,37,34,43)";
		}else if("3".equals(pid)) {
			sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements where pid=3";
		}else if("1".equals(pid)) {
			if("0".equals(tid)) {
				sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements where pid=1 and id in(26,27,28)";
			}else {
				sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements where pid=1 and id in(22,23)";
			}
			
		}
			else {
			 sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements where pid=16";
		}
		
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		String sql1="select * from organization where id="+orgid;
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
		String ouid=tList1.get(0).get("uid").toString();
		List<Map<String, Object>> list = new ArrayList<>();
		String fy="2078";
		String fys="2078/79";
//		System.out.println(fys);
		for (Tuple t : tList) {
//			String ouid="YqQbkwADI71";
			String pe=preparePeriods(fy);
			String pef = pe.replaceFirst(".$","");
			 String link = "/api/analytics.json?dimension=dx:"+t.get("indicator").toString()+"&dimension=pe:"+pef+"&filter=ou:"+ouid;
			Map<String, Object> mapadmlvl = new HashMap<>();
			mapadmlvl.put("fys", fys);
			mapadmlvl.put("inameen", t.get("inameen"));
			mapadmlvl.put("inamenp", t.get("inamenp"));
			mapadmlvl.put("data", getResponse(link));
			list.add(mapadmlvl);
		}
		return Messenger.getMessenger().setData(list).success();
	}
	
	public ResponseEntity<Map<String, Object>> getDashboardCombinePublic() throws Exception {
		String tid=request("tid");
		String pid=request("pid");
		String orgs=request("orgid");
		String orgid="";
		if("0".equals(orgs)) {
			 orgid=auth.getOrgId();
		}else {
			orgid=orgs;
		}
		String sql="";
		if("5".equals(pid)) {
			 sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements join indicator_config on indicator_config.pid=data_elements.pid where data_elements.pid=5 and data_elements.id in(38,42,35,36,37,34,43) and indicator_config.orgid="+orgs+" and indicator_config.status=1 ";
		}else if("3".equals(pid)) {
			sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements join indicator_config on indicator_config.pid=data_elements.pid where data_elements.pid=3 and indicator_config.orgid="+orgs+" and indicator_config.status=1";
		}else if("1".equals(pid)) {
			if("0".equals(tid)) {
				sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from   data_elements join indicator_config on indicator_config.pid=data_elements.pid where data_elements.pid=1 and data_elements.id in(26,27,28) and indicator_config.orgid="+orgs+" and indicator_config.status=1";
			}else {
				sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements join indicator_config on indicator_config.pid=data_elements.pid where data_elements.pid=1 and data_elements.id in(22,23) and indicator_config.orgid="+orgs+" and indicator_config.status=1";
			}
			
		}
			else {
			 sql = "select data_elements.uid as indicator, data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from  data_elements where pid=16";
		}
		
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		String sql1="select * from organization where id="+orgid;
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
		String ouid=tList1.get(0).get("uid").toString();
		List<Map<String, Object>> list = new ArrayList<>();
		String fy="2078";
		String fys="2078/79";
		System.out.println(fys);
		for (Tuple t : tList) {
//			String ouid="YqQbkwADI71";
			String pe=preparePeriods(fy);
			String pef = pe.replaceFirst(".$","");
			 String link = "/api/analytics.json?dimension=dx:"+t.get("indicator").toString()+"&dimension=pe:"+pef+"&filter=ou:"+ouid;
			Map<String, Object> mapadmlvl = new HashMap<>();
			mapadmlvl.put("fys", fys);
			mapadmlvl.put("inameen", t.get("inameen"));
			mapadmlvl.put("inamenp", t.get("inamenp"));
			mapadmlvl.put("data", getResponse(link));
			list.add(mapadmlvl);
		}
		return Messenger.getMessenger().setData(list).success();
	}

	
	public ResponseEntity<Map<String, Object>> getDashboard() throws Exception {
		String sql = "select indicator,fy,concat(fy,\\\"/\\\",fy+1) as fys,chart_type,data_elements.data_elements as inameen,data_elements.data_elements_np as inamenp from public_dashboard_setup join data_elements on data_elements.uid=public_dashboard_setup.indicator where orgid="+request("orgid")+" and public_dashboard_setup.status=0 group by indicator";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		String sql1="select * from organization where id="+request("orgid");
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
		String ouid=tList1.get(0).get("uid").toString();
		List<Map<String, Object>> list = new ArrayList<>();
		for (Tuple t : tList) {
//			String ouid="YqQbkwADI71";
			String pe=preparePeriods(t.get("fy").toString());
			String pef = pe.replaceFirst(".$","");
			 String link = "/api/analytics.json?dimension=dx:"+t.get("indicator").toString()+"&dimension=pe:"+pef+"&filter=ou:"+ouid;
			Map<String, Object> mapadmlvl = new HashMap<>();
			mapadmlvl.put("indicator", t.get("indicator"));
			mapadmlvl.put("chart_type", t.get("chart_type"));
			mapadmlvl.put("fys", t.get("fys"));
			mapadmlvl.put("inameen", t.get("inameen"));
			mapadmlvl.put("inamenp", t.get("inamenp"));
			mapadmlvl.put("data", getResponse(link));
			list.add(mapadmlvl);
		}
		return Messenger.getMessenger().setData(list).success();
	}

	public List<Tuple> getIntro(HttpServletRequest request) {
		String sql = "select * from basic_information where status=1 and orgid='"+request("orgid")+"' and type='Introduction' order by created_at desc limit 1";
		//String sql = "select * from indicators_value where orgid="+request("orgid")+" and indicator=10";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	public List<Tuple> getImpInfo(HttpServletRequest request) {
		String sql = "select * from basic_information where status=1 and orgid='"+request("orgid")+"' and type='Important Information about LLG' order by created_at desc limit 1";
		//String sql = "select * from indicators_value where orgid="+request("orgid")+" and indicator=10";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	public List<Tuple> getTips(HttpServletRequest request) {
		String sql = "select * from basic_information where status=1 and orgid='"+request("orgid")+"' and type='Tips (सुझावहरू)' order by created_at desc limit 1";
		//String sql = "select * from indicators_value where orgid="+request("orgid")+" and indicator=10";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	public List<Tuple> getContact(HttpServletRequest request) {
		String sql = "select * from basic_information where status=1 and orgid='"+request("orgid")+"' and type='Contacts' order by created_at desc limit 1";
		//String sql = "select * from indicators_value where orgid="+request("orgid")+" and indicator=11";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	public List<Tuple> getWardContact(HttpServletRequest request) {
		String sql = "select * from basic_information where status=1 and orgid='"+request("orgid")+"' and type='Ward Contacts' order by created_at desc limit 1";
		//String sql = "select * from indicators_value where orgid="+request("orgid")+" and indicator=11";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}
	
	public String getHfCount(HttpServletRequest request) throws JSONException {
		String sql = "select * from organization where id="+request("orgid");
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
//		System.out.println(tList.get(0).get("adm_id"));
		String url="https://nhfr.mohp.gov.np/health-registry/auth-count?roles=palika&rvalues="+tList.get(0).get("adm_id").toString();
		if(request("orgid").equals("100")) {
			url="https://nhfr.mohp.gov.np/health-registry/auth-count?roles=superuser";
		}
		
		HttpRequest requests = new HttpRequest();
		JSONObject responses = requests.setHeader("Accept", "application/json").get(url);
		System.out.println(responses);
		if(responses.toString().startsWith("{\"Ex")) {
			return null;
		}
		if(responses.getInt("status_code")!=200) {
			return null;
		}
//		System.out.println(responses.toString());
//		if(responses.toString().equals("{\"data\":\"<!DOCTYPE HTML PUBLIC \\\"-\\/\\/IETF\\/\\/DTD HTML 2.0\\/\\/EN\\\">\\n<html><head>\\n<title>503 Service Unavailable<\\/title>\\n<\\/head><body>\\n<h1>Service Unavailable<\\/h1>\\n<p>The server is temporarily unable to service your\\nrequest due to maintenance downtime or capacity\\nproblems. Please try again later.<\\/p>\\n<hr>\\n<address>Apache\\/2.4.18 (Ubuntu) Server at nhfr.mohp.gov.np Port 443<\\/address>\\n<\\/body><\\/html>\\n\",\"status_code\":503}")) {
//			return null;
//		}
//		if(responses.toString().equals("{\"Exception thrown (Local) \":\"Connect to https:\\/\\/nhfr.mohp.gov.np:443 [nhfr.mohp.gov.np\\/103.69.125.151] failed: Connection timed out: no further information\"}")) {
//			return null;
//		}
		System.out.println(responses);
		JSONArray resp = responses.getJSONArray("data");
		return resp.get(0).toString();
	}
	
	public JSONArray getWardwisehf(HttpServletRequest request) throws JSONException{
		String sql = "select * from organization where id="+request("orgid");
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		String url="https://nhfr.mohp.gov.np/health-registry/wardwise-count/?roles=palika&rvalues="+tList.get(0).get("adm_id").toString();
		HttpRequest requests = new HttpRequest();
		JSONObject responses = requests.setHeader("Accept", "application/json").get(url);
		System.out.println("new resp: "+responses);
		if(responses.toString().startsWith("{\"Ex")) {
			return null;
		}
		if(responses.getInt("status_code")!=200) {
			return null;
		}
//		if(responses.toString().equals("{\"data\":\"<!DOCTYPE HTML PUBLIC \\\"-\\/\\/IETF\\/\\/DTD HTML 2.0\\/\\/EN\\\">\\n<html><head>\\n<title>503 Service Unavailable<\\/title>\\n<\\/head><body>\\n<h1>Service Unavailable<\\/h1>\\n<p>The server is temporarily unable to service your\\nrequest due to maintenance downtime or capacity\\nproblems. Please try again later.<\\/p>\\n<hr>\\n<address>Apache\\/2.4.18 (Ubuntu) Server at nhfr.mohp.gov.np Port 443<\\/address>\\n<\\/body><\\/html>\\n\",\"status_code\":503}")) {
//			return null;
//		}
//		if(responses.toString().equals("{\"Exception thrown (Local) \":\"Connect to https:\\/\\/nhfr.mohp.gov.np:443 [nhfr.mohp.gov.np\\/103.69.125.151] failed: Connection timed out: no further information\"}")) {
//			return null;
//		}
		System.out.println(responses);
		JSONArray resp = responses.getJSONArray("data");
		System.out.println(resp);
		
		return resp;
	}

	public List<Tuple> getWards(HttpServletRequest request) {
		String sql = "select * from organization where id="+request("orgid");
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		String admid=tList.get(0).get("adm_id")+"";
//		if(request("orgid").equals("100")) {
//			admid="";
//		}
		
		String sql1 = "select * from admin_local_level_structure where vcid="+admid;
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
//		return tList;
		return tList1;
	}
	

	public List<Tuple> getUpdates(HttpServletRequest request) {
		String sql = "select * from basic_information where status=1 and orgid='"+request("orgid")+"' and type='Updates' order by created_at desc";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}

	public String getLat(HttpServletRequest request) {
		String sql="select value from indicators_value where orgid=? and indicator=?";
		Map<String, Object> lat = db.getSingleResultMap(sql, Arrays.asList(request("orgid"),8));
		if(lat!=null) {
			return lat.get("value").toString();
		}
		return "0";
	}

	public String getLong(HttpServletRequest request) {
		String sql1="select value from indicators_value where orgid=? and indicator=?";
		Map<String, Object> longs = db.getSingleResultMap(sql1, Arrays.asList(request("orgid"),9));	
		if(longs!=null) {
			return longs.get("value").toString();
		}
		return "0";
	}

	public List<Tuple> getCensusData(HttpServletRequest request) {
		String sql = "select * from organization where id="+request("orgid");
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		
//		String sql1 = "select population,cast(ceil(population/4.6) as unsigned) as total_household from ll_details where vcid="+tList.get(0).get("adm_id");
		String sql1 = "select population,house_hold as total_household from ll_details where vcid="+tList.get(0).get("adm_id");
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
//		return tList;
		return tList1;
	}

	public ResponseEntity<Map<String, Object>> setSetting() {
		String pid=request("pid");
		String value=request("value");
		String status="0";
		String sql="";
		String td=request("tid");
		DbResponse rowEffect;
		if("false".equals(value)) {
			 status="1";
			 sql = "INSERT INTO indicator_config (pid,tid,orgid,status) VALUES (?,?,?,?)";
				 rowEffect = db.execute(sql,
						Arrays.asList(pid,td,session("orgid"), status));
		}else {
			 status="0";
			 sql = "update indicator_config set status=? where pid=? and tid=? and orgid=?";
				 rowEffect = db.execute(sql,
						Arrays.asList(status,pid,td,session("orgid")));
		}
		 
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	public Map<String,String> mapOrgs = Map.of("7","100532250755925663","8","100732242641300433");

	public String getSutraData() {
//		System.out.println(auth.getOrgId());
		try {
		//broad-sector,sector,sectorial-activity,economic-code
		//broad-sector,sector,sectorial-activity
		String orgid=session("orgid");
		System.out.println("here "+orgid);
		String repType = request("repType");
//		String fOid = mapOrgs.get(orgid);
		String fy = request("fy");
		String key =  "85533B166FE64FEC8EC07CF4091C1EBD";
		String fFy = fy+(Integer.parseInt(fy)+1);
		String sql1 = "select * from organization where id="+orgid;
		List<String> argList1 = new ArrayList<String>();
		List<Tuple> tList1 = db.getResultList(sql1, argList1);
		String fOid=tList1.get(0).get("sutraid").toString();
		//$repType = $request->input("type")?:"sector";
		String url = "https://sutra.fcgo.gov.np/new/api/budget/"+repType+"/index.asp"+"?key="+key+"&fiscalyear="+fFy+"&adminid="+fOid;
//		System.out.println(url);
		
		HttpRequest requests = new HttpRequest();
		JSONObject responses = requests.setHeader("Accept", "application/json").get(url);
		System.out.println(responses.toString());
		JSONArray resp = responses.getJSONArray("data");
			return resp.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getSutraDataPublic() {
		try {
			//broad-sector,sector,sectorial-activity
			String tid=request("pid");
			String orgid=request("orgid");
			String sql="select * from indicator_config where orgid=? and pid=? and status=?";
			List<Tuple> tList = db.getResultList(sql,Arrays.asList(orgid,tid,1));
			if (tList.isEmpty()) {
				return null;
			}
			String repType = request("repType");
//			String fOid = mapOrgs.get(orgid);
			String fy = request("fy");
			String key =  "85533B166FE64FEC8EC07CF4091C1EBD";
			String fFy = fy+(Integer.parseInt(fy)+1);
			String sql1 = "select * from organization where id="+orgid;
			List<String> argList1 = new ArrayList<String>();
			List<Tuple> tList1 = db.getResultList(sql1, argList1);
			String fOid=tList1.get(0).get("sutraid").toString();
			//$repType = $request->input("type")?:"sector";
			String url = "https://sutra.fcgo.gov.np/new/api/budget/"+repType+"/index.asp"+"?key="+key+"&fiscalyear="+fFy+"&adminid="+fOid;
			
			
			HttpRequest requests = new HttpRequest();
			JSONObject responses = requests.setHeader("Accept", "application/json").get(url);
			System.out.println(responses.toString());
			JSONArray resp = responses.getJSONArray("data");
				return resp.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	
	public ResponseEntity<Map<String, Object>> changePasswordSelf(String id){
		String opassword = request("opassword");
		String password = request("password");
		String rpassword = request("rpassword");
		if(opassword.isBlank() || password.isBlank()|| rpassword.isBlank()) {
			return Messenger.getMessenger().setMessage("All Fields are required.").error();
		}
		if(!password.equals(rpassword)) {
			return Messenger.getMessenger().setMessage("Password and Confirm password does not match.").error();
		}
		
		Tuple t = db.getSingleResult("select password from users where id=?",Arrays.asList(id));
		if(t!=null) {
			String encPass = t.get("password")+"";
			if(pe.matches(request("opassword"), encPass)) {
				DbResponse rowEffect;
				String sql = "UPDATE users set password=? where id=?";
				rowEffect = db.execute(sql,Arrays.asList(pe.encode(password),id));
				if (rowEffect.getErrorNumber() == 0) {
					return Messenger.getMessenger().success();
				}
			}else {
				return Messenger.getMessenger().setMessage("Old password does not match.").error();
			}
		}
		return Messenger.getMessenger().setMessage("No such User exists.").error();
	}
	public ResponseEntity<Map<String, Object>> index() {
		String condition = "";
		String td="";
		String dt=request("ldate");
		 td= dt.replaceAll("-","");
		System.out.println(td);
		if(td.equals("")) {
			String tnd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			td= tnd.replaceAll("-","");
		}
		if (!request("searchTerm").isEmpty()) {
			List<String> searchbles = Organization.searchables();
			condition += "and (";
			for (String field : searchbles) {
				condition += field + " LIKE '%" + db.esc(request("searchTerm")) + "%' or ";
			}
			condition = condition.substring(0, condition.length() - 3);
			condition += ")";
		}
		if (!condition.isBlank()) {
			condition = " where 1=1 " + condition;
		}
		String sort = "";
		if(!request("sortKey").isBlank()) {
			if(!request("sortDir").isBlank()) {
				sort = request("sortKey")+" "+request("sortDir");
			}
		}

		Paginator p = new Paginator();
		Map<String, Object> result = p.setPageNo(request("page")).setPerPage(request("perPage"))
				.setOrderBy(sort)
				.select("log.id as lid,action,cast(createdon as char) as createdon,organization.name as orgname,dateint,orgid")
				.sqlBody("from " + table + " join organization on organization.id=log.orgid "+ condition+ " and log.dateint="+td+ " order by createdon desc").paginate();
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return Messenger.getMessenger().error();
		}
	}

	

	public List<Tuple> getvidoes(HttpServletRequest request) {
		String orgid = request("orgid");
		String sql = "select * from infographics where status=1   and image LIKE '%.mp4' and orgid="+orgid+" order by created_at desc";
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sql, argList);
		return tList;
	}

	public List<Tuple> getChartConfigHmis(HttpServletRequest request) {
		String orgid = request("orgid");
		String sql = "select id,name,orders,myear,mindicator,time,charttype,status,cid from dcharts where orgid=? and cid=? and status=1 order by orders";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(orgid,0));
		return admlvl;
	}

	public List<Tuple> getChartConfigSutra(HttpServletRequest request) {
		String orgid = request("orgid");
		String sql = "select id,name,orders,myear,mindicator,time,charttype,status,cid from dcharts where orgid=? and cid=? and status=1 order by orders";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(orgid,1));
		return admlvl;
	}
	

}
