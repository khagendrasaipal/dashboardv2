package org.saipal.dboard.contorllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.saipal.dboard.services.SvgMapService;
import org.saipal.dboard.service.DashboardService;
import org.saipal.fmisutil.auth.Authenticated;
import org.saipal.fmisutil.service.AutoService;
import org.saipal.fmisutil.util.DB;
import org.saipal.fmisutil.util.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/")
public class MainController extends AutoService {
	
	@Autowired
	Environment env;
	
	@Autowired
	ClientCreateTestService createClient;
	
	@Autowired
	DashboardService ds;
	
	@Autowired
	DB db;
	
	@Autowired
	Authenticated auth;
	
	@Autowired
	SvgMapService sms;
	
	
//	@ResponseBody
//	@GetMapping("/test/token")
//	public String index(HttpServletRequest request) {
//		String baseUrl = "http://localhost:8011/token";
//		try {
//			HttpRequest hreq = new HttpRequest();
//			hreq.setParam("client_id", env.getProperty("oascentral.client_id"));
//			hreq.setParam("client_secret", env.getProperty("oascentral.client_secret"));
//			hreq.setParam("code", env.getProperty("oascentral.code"));
//			hreq.setParam("grant_type", "authorization_code");
//			JSONObject response = hreq.post(baseUrl);
//			if (response.getInt("status_code") == 200) {
//				JSONObject resp = response.getJSONObject("data");
//				System.out.println(resp.toString());
//				//return resp.toString();
//			}
//			System.out.println(response);
//		} catch (JSONException e) {
//			e.printStackTrace();
//			System.out.println("bye");
//			return "errr";
//		}
//		return "I'm working fine";
//	}
	
	// hell
	
	@ResponseBody
	@GetMapping("/test/create")
	public String create(HttpServletRequest request) {
		String accessToken="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkZXZpbnN0YW5jZSIsImV4cCI6MTY0NDkwMTgyMiwiaWF0IjoxNjQ0ODE1NDIyLCJqdGkiOiI5OTY0NDgzMjQ2ODY5NzExNiJ9.OvpFhclYRbH5ZKbXKJI6f3Thf4xFiRUEAQqvihm4zMM";
		String baseUrl = "http://localhost:8011/sync/table";
		String sql = "select * from admin_org_strs where orgidint=?";
		List<Tuple> orgs = db.getResultList(sql, Arrays.asList("99550740053229891"));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!orgs.isEmpty()) {
			for (Tuple t : orgs) {
				Map<String, Object> mapOrg = new HashMap<>();
				mapOrg.put("orgidint", t.get("orgidint"));
				mapOrg.put("parentorgid", t.get("parentorgid"));
				mapOrg.put("code", t.get("code"));
				mapOrg.put("orgnamenp", t.get("orgnamenp"));
				mapOrg.put("orgnameen", t.get("orgnameen"));
				mapOrg.put("orgnamelc", t.get("orgnamelc"));
				mapOrg.put("adminlevel", t.get("adminlevel"));
				mapOrg.put("adminid", t.get("adminid"));
				mapOrg.put("orglevelid", t.get("orglevelid"));
				mapOrg.put("provinceid", t.get("provinceid"));
				mapOrg.put("districtid", t.get("districtid"));
				mapOrg.put("vcid", t.get("vcid"));
				mapOrg.put("wardno", t.get("wardno"));
				mapOrg.put("approved", t.get("approved"));
				mapOrg.put("disabled", t.get("disabled"));
				mapOrg.put("enterby", t.get("enterby"));
				list.add(mapOrg);
			}
		}
		
		try {
			JSONObject jo = new JSONObject();
			jo.put("table", "admin_org_strs");
			jo.put("action", "create");
			jo.put("rows", list);
			JSONArray ja = new JSONArray();
			ja.put(jo);

			HttpRequest hreq = new HttpRequest();
			hreq.setParam("data",ja.toString());
			
			JSONObject response = hreq.setHeader("Authorization", "Bearer " + accessToken).post(baseUrl);
			
			if (response.getInt("status_code") == 200) {
				JSONObject resp = response.getJSONObject("data");
				System.out.println(resp.toString());
				return resp.toString();
			}
			System.out.println(response);
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("bye");
			return "errr";
		}
		return "I'm working fine";
	}
	
	
	
	@GetMapping("/dashboard1111")
	public String dash333(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
		String admin=request("admin");
//		System.out.println(admin);
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		String hfcount=createClient.getWardwisehf(request).toString();
		ObjectMapper mapper = new ObjectMapper();
		Object map = mapper.readValue(hfcount, Object.class);
		hf = hf.replace("[","");
		hf = hf.replace("]","");
		hf=hf.replace("\"", "");
		String wards="0";
		String update="<p> स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ । </p>";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
		}
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("name").toString();
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",weather.get(0).get("temp"));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",hf);
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("census",census.get(0));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		if(admin.equals("1")) {
			return "test/dashboardadmin";
		}else {
			return "test/dashboard";
		}
		
	}
	

	
	
	
	@GetMapping("/dboardnew")
	public String dboard(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		String hfcount=createClient.getWardwisehf(request).toString();
		ObjectMapper mapper = new ObjectMapper();
		Object map = mapper.readValue(hfcount, Object.class);
		hf = hf.replace("[","");
		hf = hf.replace("]","");
		hf=hf.replace("\"", "");
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
		}
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("name").toString();
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",weather.get(0).get("temp"));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",hf);
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("census",census.get(0));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		
		return "test/dboard";
//		if(admin.equals("1")) {
//			return "test/dashboardadmin";
//		}else {
//			return "test/dashboard";
//		}
		
	}
	
	
	
	@GetMapping("/dashboard2")
	public String dash1(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
//		System.out.println(request("orgid"));
		//List<Tuple> video = createClient.getvidoes(request);
		//String videos=video.get(0).get("image").toString();
		//System.out.println(videos);
		List<Tuple> video = createClient.getvidoes(request);
//		String videos=video.get(0).get("image").toString();
		//System.out.println(videos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   String dates=ds.getNepaliDate();
//		   System.out.println(dtf.format(now)); 
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		List<Tuple> acard=createClient.getcardConfig(request);
		List<Tuple> cont=createClient.getContact(request);
		List<Tuple> wc = createClient.getWardContact(request);

		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		Object result = createClient.getWardwisehf(request);
		String hfcount="";
		if (result != null) {
		    hfcount = result.toString();
		}
//		String hfcount=createClient.getWardwisehf(request).toString();
		
		Object map = null;
		if(hfcount.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			 map = null;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			 map = mapper.readValue(hfcount, Object.class);
		}
		if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		String contact="Contact Information";
		String wcn="";
		if (!cont.isEmpty()) {
			contact=cont.get(0).get("content").toString();
		}
		if (!wc.isEmpty()) {
			wcn=wc.get(0).get("content").toString();
		}

		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		String v="1";
		if (!video.isEmpty()) {
			v=video.get(0).get("image").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("namenp").toString();
		String pname=org.get(0).get("provincename").toString();
		model.addAttribute("video", v);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",convertToNepali(weather.get(0).get("temp")+""));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",convertToNepali(hf));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("population",convertToNepali(census.get(0).get("population")+""));
		model.addAttribute("household",convertToNepali(census.get(0).get("total_household")+""));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		model.addAttribute("pname",pname);
		model.addAttribute("acard",acard);
		model.addAttribute("contact",contact);
		model.addAttribute("wc",wcn);
		model.addAttribute("dates",convertToNepali(dates));
//		model.addAttribute("time",java.time.LocalDate.now());
		
		return "test/dashnew";
	}
	
	@GetMapping("/dashboardold")
	public String dash2(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
//		System.out.println(request("orgid"));
		List<Tuple> video = createClient.getvidoes(request);
//		String videos=video.get(0).get("image").toString();
		//System.out.println(videos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   String dates=ds.getNepaliDate();
//		   System.out.println(dtf.format(now)); 
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		
		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		Object result = createClient.getWardwisehf(request);
		String hfcount="";
		if (result != null) {
		    hfcount = result.toString();
		}
//		String hfcount=createClient.getWardwisehf(request).toString();
		
		Object map = null;
		if(hfcount.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			 map = null;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			 map = mapper.readValue(hfcount, Object.class);
		}
		if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		String v="1";
		if (!video.isEmpty()) {
			v=video.get(0).get("image").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("namenp").toString();
		String pname=org.get(0).get("provincename").toString();
		model.addAttribute("video", v);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",convertToNepali(weather.get(0).get("temp")+""));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",convertToNepali(hf));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("population",convertToNepali(census.get(0).get("population")+""));
		model.addAttribute("household",convertToNepali(census.get(0).get("total_household")+""));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		model.addAttribute("pname",pname);
		model.addAttribute("dates",convertToNepali(dates));
//		model.addAttribute("time",java.time.LocalDate.now());
		if(request("orgid").equals("100")) {
			return "test/hmisdashboard";
		}else {
			return "test/dashboardsuman1";
		}
		
	}
	
	@GetMapping("/dashboardupdate")
	public String dashboardtest(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
//		System.out.println(request("orgid"));
		List<Tuple> video = createClient.getvidoes(request);
//		String videos=video.get(0).get("image").toString();
		//System.out.println(videos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   String dates=ds.getNepaliDate();
//		   System.out.println(dtf.format(now)); 
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		
		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		Object result = createClient.getWardwisehf(request);
		String hfcount="";
		if (result != null) {
		    hfcount = result.toString();
		}
//		String hfcount=createClient.getWardwisehf(request).toString();
		
		Object map = null;
		if(hfcount.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			 map = null;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			 map = mapper.readValue(hfcount, Object.class);
		}
		if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		String v="1";
		if (!video.isEmpty()) {
			v=video.get(0).get("image").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("namenp").toString();
		String pname=org.get(0).get("provincename").toString();
		model.addAttribute("video", v);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",convertToNepali(weather.get(0).get("temp")+""));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",convertToNepali(hf));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("population",convertToNepali(census.get(0).get("population")+""));
		model.addAttribute("household",convertToNepali(census.get(0).get("total_household")+""));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		model.addAttribute("pname",pname);
		model.addAttribute("dates",convertToNepali(dates));
//		model.addAttribute("time",java.time.LocalDate.now());
		if(request("orgid").equals("100")) {
			return "test/hmisdashboard";
		}else {
			return "test/dashboardtest";
		}
		
	}
	
	
	@GetMapping("/dashboard")
	public String dashboardtest1(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
//		System.out.println(request("orgid"));
		List<Tuple> video = createClient.getvidoes(request);
//		String videos=video.get(0).get("image").toString();
		//System.out.println(videos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   String dates=ds.getNepaliDate();
//		   System.out.println(dtf.format(now)); 
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		List<Tuple> acard=createClient.getcardConfig(request);
		List<Tuple> cont=createClient.getContact(request);
		List<Tuple> wc = createClient.getWardContact(request);

		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		Object result = createClient.getWardwisehf(request);
		String hfcount="";
		if (result != null) {
		    hfcount = result.toString();
		}
//		String hfcount=createClient.getWardwisehf(request).toString();
		
		Object map = null;
		if(hfcount.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			 map = null;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			 map = mapper.readValue(hfcount, Object.class);
		}
		if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		String contact="Contact Information";
		String wcn="";
		if (!cont.isEmpty()) {
			contact=cont.get(0).get("content").toString();
		}
		if (!wc.isEmpty()) {
			wcn=wc.get(0).get("content").toString();
		}

		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		String v="1";
		if (!video.isEmpty()) {
			v=video.get(0).get("image").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("namenp").toString();
		String pname=org.get(0).get("provincename").toString();
		model.addAttribute("video", v);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",convertToNepali(weather.get(0).get("temp")+""));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",convertToNepali(hf));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("population",convertToNepali(census.get(0).get("population")+""));
		model.addAttribute("household",convertToNepali(census.get(0).get("total_household")+""));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		model.addAttribute("pname",pname);
		model.addAttribute("acard",acard);
		model.addAttribute("contact",contact);
		model.addAttribute("wc",wcn);
		model.addAttribute("dates",convertToNepali(dates));
//		model.addAttribute("time",java.time.LocalDate.now());
		if(request("orgid").equals("100")) {
			return "test/hmisdashboard";
		}else {
			return "test/dashboardtest1";
		}
		
	}
	
	
	
	
	@GetMapping("/dashboard1")
	public String dash22(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
////		System.out.println(request("orgid"));
//		List<Tuple> video = createClient.getvidoes(request);
////		String videos=video.get(0).get("image").toString();
//		//System.out.println(videos);
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
//		   LocalDateTime now = LocalDateTime.now();  
////		   System.out.println(dtf.format(now)); 
//		List<Tuple> org = createClient.getOrgs(request);
//		List<Tuple> ward = createClient.getWards(request);
//		List<Tuple> chart=createClient.getChartConfig(request);
//		List<Tuple> dchart=createClient.getChartConfigHmis(request);
//		List<Tuple> fchart=createClient.getChartConfigSutra(request);
//		List<Tuple> image=createClient.getmageConfig(request);
//		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
//		List<Tuple> updates = createClient.getUpdates(request);
//		
//		String hf=createClient.getHfCount(request);
//		List<Tuple> census=createClient.getCensusData(request);
//		String hfcount=createClient.getWardwisehf(request).toString();
//		String dates=ds.getNepaliDate();
//		
//		ObjectMapper mapper = new ObjectMapper();
//		Object map = mapper.readValue(hfcount, Object.class);
//		hf = hf.replace("[","");
//		hf = hf.replace("]","");
//		hf=hf.replace("\"", "");
//		String wards="0";
//		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
//		if (!ward.isEmpty()) {
//			wards=ward.get(0).get("numberofward").toString();
//		}
//		String v="1";
//		if (!video.isEmpty()) {
//			v=video.get(0).get("image").toString();
//		}
//		if (!updates.isEmpty()) {
//			update=updates.get(0).get("content").toString();
//			update=update.replace("<p>","");
//			update=update.replace("</p>","");
//		}
//		
////		System.out.println(weather.get(0).get("temp"));
//		String orgname=org.get(0).get("namenp").toString();
//		String pname=org.get(0).get("provincename").toString();
//		model.addAttribute("video", v);
//		model.addAttribute("orgname", orgname);
//		model.addAttribute("chart",chart);
//		model.addAttribute("dchart",dchart);
//		model.addAttribute("fchart",fchart);
//		model.addAttribute("ward",wards);
//		model.addAttribute("temp",convertToNepali(weather.get(0).get("temp")+""));
//		model.addAttribute("type",weather.get(0).get("type"));
//		model.addAttribute("desc",weather.get(0).get("desc"));
//		model.addAttribute("icon",weather.get(0).get("icon"));
//		model.addAttribute("hf",convertToNepali(hf));
//		model.addAttribute("orgid",org.get(0).get("id").toString());
//		model.addAttribute("update",update);
//		model.addAttribute("population",convertToNepali(census.get(0).get("population")+""));
//		model.addAttribute("household",convertToNepali(census.get(0).get("total_household")+""));
//		model.addAttribute("hfcount",map);
//		model.addAttribute("image",image);
//		model.addAttribute("pname",pname);
//		model.addAttribute("dates",convertToNepali(dates));
////		model.addAttribute("time",java.time.LocalDate.now());
//		
//		return "test/dashboardsuman1";
		
		
		
		List<Tuple> video = createClient.getvidoes(request);
//		String videos=video.get(0).get("image").toString();
		//System.out.println(videos);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   String dates=ds.getNepaliDate();
//		   System.out.println(dtf.format(now)); 
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		List<Tuple> updates = createClient.getUpdates(request);
		List<Tuple> acard=createClient.getcardConfig(request);
		List<Tuple> cont=createClient.getContact(request);
		List<Tuple> wc = createClient.getWardContact(request);

		String hf=createClient.getHfCount(request);
		List<Tuple> census=createClient.getCensusData(request);
		Object result = createClient.getWardwisehf(request);
		String hfcount="";
		if (result != null) {
		    hfcount = result.toString();
		}
//		String hfcount=createClient.getWardwisehf(request).toString();
		
		Object map = null;
		if(hfcount.equals("")) {
			ObjectMapper mapper = new ObjectMapper();
			 map = null;
		}else {
			ObjectMapper mapper = new ObjectMapper();
			 map = mapper.readValue(hfcount, Object.class);
		}
		if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		String contact="Contact Information";
		String wcn="";
		if (!cont.isEmpty()) {
			contact=cont.get(0).get("content").toString();
		}
		if (!wc.isEmpty()) {
			wcn=wc.get(0).get("content").toString();
		}

		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		String v="1";
		if (!video.isEmpty()) {
			v=video.get(0).get("image").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(weather.get(0).get("temp"));
		String orgname=org.get(0).get("namenp").toString();
		String pname=org.get(0).get("provincename").toString();
		model.addAttribute("video", v);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("ward",wards);
		model.addAttribute("temp",convertToNepali(weather.get(0).get("temp")+""));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("hf",convertToNepali(hf));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("update",update);
		model.addAttribute("population",convertToNepali(census.get(0).get("population")+""));
		model.addAttribute("household",convertToNepali(census.get(0).get("total_household")+""));
		model.addAttribute("hfcount",map);
		model.addAttribute("image",image);
		model.addAttribute("pname",pname);
		model.addAttribute("acard",acard);
		model.addAttribute("contact",contact);
		model.addAttribute("wc",wcn);
		model.addAttribute("dates",convertToNepali(dates));
//		model.addAttribute("time",java.time.LocalDate.now());
		if(request("orgid").equals("100")) {
			return "test/hmisdashboard";
		}else {
			return "test/dashboardsuman1";
		}
	}
	
	
	
	
	public String  convertToNepali(String engNum){
		if(engNum==null) {
			return "";
		}
		String[] nepNum = new String[] {"०", "१", "२", "३", "४", "५", "६", "७", "८", "९"};
		String tnum = "";
		String[] engNums = engNum.split("");
		for(String i: engNums){
			try {
				int k = Integer.parseInt(i);
				tnum +=nepNum[k];
			}catch(NumberFormatException ex){
				tnum +=i;
			}
		}
		return tnum;
	}
	
	@GetMapping("/web2")
	public String webs(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> intro=createClient.getIntro(request);
		List<Tuple> cont=createClient.getContact(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> updates = createClient.getUpdates(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Tuple> acard=createClient.getcardConfig(request);
		List<Tuple> basic=createClient.getBasic(request);
		List<Tuple> tips = createClient.getTips(request);
		List<Tuple> wc = createClient.getWardContact(request);
		List<Tuple> imp = createClient.getImpInfo(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		String lat=createClient.getLat(request);
		String longs=createClient.getLong(request);
		String orgname=org.get(0).get("name").toString();
		String intros="Introduction of municipality";
		String contact="Contact Information";
		String hf=createClient.getHfCount(request);
		String hfcount=createClient.getWardwisehf(request).toString();
		ObjectMapper mapper = new ObjectMapper();
		Object map = mapper.readValue(hfcount, Object.class);
		System.out.println(acard);
//		System.out.println(image.get(0).get("image"));
//		for(int i=0;i<=hfcount.length();i++) {
//			JSONObject obj=hfcount.getJSONObject(i);
//		}
		
		List<Tuple> census=createClient.getCensusData(request);
if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		
		if(hfcount.contains("[")) {
			
		}else {
			hfcount="";
		}
		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		String wcn = "";
		String tipsn = "";
		String impn = "";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		if (!intro.isEmpty()) {
			intros=intro.get(0).get("content").toString();
		}
		if (!cont.isEmpty()) {
			contact=cont.get(0).get("content").toString();
		}
		if (!wc.isEmpty()) {
			wcn=wc.get(0).get("content").toString();
		}
		if (!tips.isEmpty()) {
			tipsn=tips.get(0).get("content").toString();
		}
		if (!imp.isEmpty()) {
			impn=imp.get(0).get("content").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
		}
		
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(tipsn);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("temp",weather.get(0).get("temp"));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("intro",intros);
		model.addAttribute("tips",tipsn);
		model.addAttribute("wc",wcn);
		model.addAttribute("imp",impn);
		model.addAttribute("hf",hf);
		model.addAttribute("ward",wards);
		model.addAttribute("update",update);
		model.addAttribute("lat",lat);
		model.addAttribute("image",image);

		model.addAttribute("acard",acard);
		model.addAttribute("longs",longs);
		model.addAttribute("hfcount",map);
		model.addAttribute("contact",contact);
		model.addAttribute("census",census.get(0));
		model.addAttribute("basic",basic);
		model.addAttribute("svgMap",sms.getSvgMap());
	
		return "test/web";
	}
	
	
	
	
	@GetMapping("/web")
	public String webs2(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
		List<Tuple> video = createClient.getvidoes(request);
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> dchart=createClient.getChartConfigHmis(request);
		List<Tuple> fchart=createClient.getChartConfigSutra(request);
		List<Tuple> intro=createClient.getIntro(request);
		
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> updates = createClient.getUpdates(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Tuple> acard=createClient.getcardConfig(request);
		List<Tuple> basic=createClient.getBasic(request);
		List<Tuple> tips = createClient.getTips(request);
		List<Tuple> cont=createClient.getContact(request);
		List<Tuple> wc = createClient.getWardContact(request);
		List<Tuple> imp = createClient.getImpInfo(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		String lat=createClient.getLat(request);
		String longs=createClient.getLong(request);
		String orgname=org.get(0).get("name").toString();
		String intros="Introduction of municipality";
		String contact="Contact Information";
		String hf=createClient.getHfCount(request);
		String hfcount=createClient.getWardwisehf(request)!=null?createClient.getWardwisehf(request).toString():"";
		ObjectMapper mapper = new ObjectMapper();
		Object map =null;
		if(!hfcount.isBlank()) {
			 map = mapper.readValue(hfcount, Object.class);
		}
		
		String v="1";
		if (!video.isEmpty()) {
			v=video.get(0).get("image").toString();
		}
//		System.out.println(acard);
//		System.out.println(image.get(0).get("image"));
//		for(int i=0;i<=hfcount.length();i++) {
//			JSONObject obj=hfcount.getJSONObject(i);
//		}
		
		List<Tuple> census=createClient.getCensusData(request);
if(hf==null) {
			
		}else {
			hf = hf.replace("[","");
			hf = hf.replace("]","");
			hf=hf.replace("\"", "");
		}
		
		if(hfcount.contains("[")) {
			
		}else {
			hfcount="";
		}
		
		String wards="0";
		String update="स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।";
		String wcn = "";
		String tipsn = "";
		String impn = "";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		if (!intro.isEmpty()) {
			intros=intro.get(0).get("content").toString();
		}
		if (!cont.isEmpty()) {
			contact=cont.get(0).get("content").toString();
		}
		if (!wc.isEmpty()) {
			wcn=wc.get(0).get("content").toString();
		}
		if (!tips.isEmpty()) {
			tipsn=tips.get(0).get("content").toString();
		}
		if (!imp.isEmpty()) {
			impn=imp.get(0).get("content").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
		}
		
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
			update=update.replace("<p>","");
			update=update.replace("</p>","");
		}
		
//		System.out.println(tipsn);
		model.addAttribute("video", v);
		model.addAttribute("orgname", orgname);
		model.addAttribute("chart",chart);
		model.addAttribute("dchart",dchart);
		model.addAttribute("fchart",fchart);
		model.addAttribute("temp",weather.get(0).get("temp"));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("intro",intros);
		model.addAttribute("tips",tipsn);
		model.addAttribute("contact",contact);
		model.addAttribute("wc",wcn);
		model.addAttribute("imp",impn);
		model.addAttribute("hf",hf);
		model.addAttribute("ward",wards);
		model.addAttribute("update",update);
		model.addAttribute("lat",lat);
		model.addAttribute("image",image);

		model.addAttribute("acard",acard);
		model.addAttribute("longs",longs);
		model.addAttribute("hfcount",map);
		
		model.addAttribute("census",census.get(0));
		model.addAttribute("basic",basic);
		model.addAttribute("svgMap",sms.getSvgMap());
		String orgid=request("orgid");
		if(orgid.equals("100")) {
			return "test/hmisportal";
		}else {
			return "test/websuman";
		}
	
		
	}
	

	
	@GetMapping("/web1")
	public String webs1(Model model,HttpServletRequest request) throws JSONException, JsonMappingException, JsonProcessingException {
		List<Tuple> org = createClient.getOrgs(request);
		List<Tuple> video = createClient.getvidoes(request);
		List<Tuple> chart=createClient.getChartConfig(request);
		List<Tuple> intro=createClient.getIntro(request);
		List<Tuple> cont=createClient.getContact(request);
		List<Tuple> ward = createClient.getWards(request);
		List<Tuple> updates = createClient.getUpdates(request);
		List<Tuple> image=createClient.getmageConfig(request);
		List<Tuple> basic=createClient.getBasic(request);
		List<Tuple> tips = createClient.getTips(request);
		List<Tuple> wc = createClient.getWardContact(request);
		List<Tuple> imp = createClient.getImpInfo(request);
		List<Map<String, Object>> weather=createClient.getWeatherInfo(request);
		String lat=createClient.getLat(request);
		String longs=createClient.getLong(request);
		String orgname=org.get(0).get("name").toString();
		String intros="Introduction of municipality";
		String contact="Contact Information";
		String hf=createClient.getHfCount(request);
		String hfcount=createClient.getWardwisehf(request).toString();
		ObjectMapper mapper = new ObjectMapper();
		Object map = mapper.readValue(hfcount, Object.class);
		String videos=video.get(0).get("image").toString();
//		System.out.println(image.get(0).get("image"));
//		for(int i=0;i<=hfcount.length();i++) {
//			JSONObject obj=hfcount.getJSONObject(i);
//		}
		
		List<Tuple> census=createClient.getCensusData(request);
		hf = hf.replace("[","");
		hf = hf.replace("]","");
		hf=hf.replace("\"", "");
		String wards="0";
		String update="<p>स्वास्थ्य ड्यासबोर्ड मुख्य रूपले ग्राफिकल चार्टहरू, नक्साहरू र तथ्याङ्कहरूको सहायताले स्वास्थ्य संकेतकहरूको प्रगतिको निरीक्षण गर्न प्रयोग हुन्छ । सान्दर्भिक चार्टहरू, नक्साहरू र तथ्याङ्कहरू पनि ड्यासबोर्डबाट डाउनलोड गर्न सकिन्छ ।</p>";
		String wcn = "";
		String tipsn = "";
		String impn = "";
		if (!ward.isEmpty()) {
			wards=ward.get(0).get("numberofward").toString();
		}
		if (!intro.isEmpty()) {
			intros=intro.get(0).get("content").toString();
		}
		if (!cont.isEmpty()) {
			contact=cont.get(0).get("content").toString();
		}
		if (!wc.isEmpty()) {
			wcn=wc.get(0).get("content").toString();
		}
		if (!tips.isEmpty()) {
			tipsn=tips.get(0).get("content").toString();
		}
		if (!imp.isEmpty()) {
			impn=imp.get(0).get("content").toString();
		}
		if (!updates.isEmpty()) {
			update=updates.get(0).get("content").toString();
		}
		model.addAttribute("orgname", orgname);
		model.addAttribute("video", videos);
		model.addAttribute("chart",chart);
		model.addAttribute("temp",weather.get(0).get("temp"));
		model.addAttribute("type",weather.get(0).get("type"));
		model.addAttribute("desc",weather.get(0).get("desc"));
		model.addAttribute("icon",weather.get(0).get("icon"));
		model.addAttribute("orgid",org.get(0).get("id").toString());
		model.addAttribute("intro",intros);
		model.addAttribute("tips",tipsn);
		model.addAttribute("wc",wcn);
		model.addAttribute("imp",impn);
		model.addAttribute("hf",hf);
		model.addAttribute("ward",wards);
		model.addAttribute("update",update);
		model.addAttribute("lat",lat);
		model.addAttribute("image",image);
		model.addAttribute("longs",longs);
		model.addAttribute("hfcount",map);
		model.addAttribute("contact",contact);
		model.addAttribute("census",census.get(0));
		model.addAttribute("basic",basic);
		return "test/webnew";
	}


	
	@GetMapping("/dboard/getProgram")
	public ResponseEntity<Map<String, Object>> getProgrma(HttpServletRequest request) {
		return createClient.getProgram();

	}
	
	@GetMapping("/dboard/getDefault")
	public ResponseEntity<Map<String, Object>> getDefault(HttpServletRequest request) {
		return createClient.getDefault();

	}
	
	@PutMapping("user/change-pasword-self/{id}")
	public ResponseEntity<Map<String, Object>> changePasswordSelf(HttpServletRequest request,@PathVariable String id) {
//		System.out.println("change Password Self"+ id);
		return createClient.changePasswordSelf(id);
	}
	
	@GetMapping("/dboard/getIndicators/{id}")
	public ResponseEntity<Map<String, Object>> getIndicators(HttpServletRequest request,@PathVariable String id) {
		return createClient.getIndicators(id);

	}
	
	@GetMapping("/dboard/getData")
	public ResponseEntity<Map<String, Object>> getData(HttpServletRequest request) throws Exception {
		return createClient.getData(request);

	}
	
	@GetMapping("/dboard/getComposite")
	public ResponseEntity<Map<String, Object>> getComposite(HttpServletRequest request) {
		return createClient.getComposite();

	}
	@GetMapping("/dboard")
	public ResponseEntity<Map<String, Object>> index(HttpServletRequest request) {
		return createClient.index();
	}
	
	@GetMapping("/getDashboard")
	public ResponseEntity<Map<String, Object>> getdashboard(HttpServletRequest request) throws Exception {
		return createClient.getDashboard();

	}
	
	@GetMapping("/getDashboardAdmin")
	public ResponseEntity<Map<String, Object>> getDashboardAdmin(HttpServletRequest request) throws Exception {
//		System.out.println("hahah");
		return createClient.getDashboardCombine();

	}
	
	@GetMapping("/getDashboardCombine")
	public ResponseEntity<Map<String, Object>> getdashboardCombine(HttpServletRequest request) throws Exception {
//		System.out.println("hahah");
		return createClient.getDashboardCombine();

	}
	
	@GetMapping("/getDashboardCombinePublic")
	public ResponseEntity<Map<String, Object>> getDashboardCombinePublic(HttpServletRequest request) throws Exception {
		return createClient.getDashboardCombinePublic();

	}
	@GetMapping("/setSetting")
	public ResponseEntity<Map<String, Object>> setSetting(HttpServletRequest request) throws Exception {
		return createClient.setSetting();

	}
	@RequestMapping("")
	public String main() {
		return "index";
	}
	
	@GetMapping("/sutra")
	@ResponseBody
	public String getSutraData() {
		
		return createClient.getSutraData();
	}
	@GetMapping("getDashboardSutra")
	@ResponseBody
	public String getDashboardSutra(){
		return createClient.getSutraDataPublic();
		
	}
	@GetMapping("getwarddetails")
	@ResponseBody
	public String getWardDetails(){
		return "Hello from Ward";
	}
	
	
//	@ResponseBody
//	@GetMapping("/test/authorize")
//	public String authorize(HttpServletRequest request) {
//		String baseUrl = "http://localhost:8011";
//		String client_id="devinstance";
//		String redirect_uri="dev-machine";
//		String state=UUID.randomUUID().toString();
//		String response_type="code";
//		String urlpart="?client_id="+client_id+"&redirect_uri="+redirect_uri+"&state="+state+"&response_type="+response_type;
//		String url = baseUrl + "/authorize"+urlpart;
//		HttpRequest requests = new HttpRequest();
//		JSONObject responses = requests.get(url);
//		System.out.println(responses);
//		return "I'm working fine";
//	}
//	
//	@ResponseBody
//	@GetMapping("/test/create-client")
//	public String createClient(HttpServletRequest request) {
//		
//		String baseUrl = "http://localhost:8011";
//		String client_id="devinstance";
//		String client_name="devinstance";
//		String redirect_uri="dev-machine";
//		String client_secret="devinstance";
//		String status="1";
//		String urlpart="?client_id="+client_id+"&redirect_uri="+redirect_uri+"&client_name="+client_name+"&client_secret="+client_secret+"&status="+status;
//		String url = baseUrl + "/add-clients"+urlpart;
//		HttpRequest requests = new HttpRequest();
//		JSONObject responses = requests.get(url);
//		System.out.println(responses);
//		return responses.toString();
//	}
//	
//
//	
//	@ResponseBody
//	@PostMapping("/client-create-test")
//	public String ClientCreationTest(HttpServletRequest request) {
//		
//		return createClient.store();
//	}
//	
//	@ResponseBody
//	@GetMapping("test-omed")
//	public void testOmed() {
//		OmedBuilder bldr = new OmedBuilder();
//		bldr.setSubject("test subject");
//		bldr.setBodyText("This is a sample test");
//		bldr.addAttachments("iid", "name", "text", "C:\\Users\\user\\Downloads\\oasDoc.txt", "bhim");
//		OMED doc = bldr.getDocument();
//		String id = ds.storeDocument(doc);
//		System.out.println("docid: "+id);
//		OMED storedData = ds.getDocumentById(id);
//		OMED storeData1 = ds.getDocumentByMessageId(doc.messageid);
//		System.out.println("1. "+OmedParser.getJsonString(storedData));
//		System.out.println("2. "+OmedParser.getJsonString(storeData1));
//		System.out.println(OmedParser.getJsonString(doc));
//	}
//	
//	@RequestMapping(value = "list-routes")
//	@ResponseBody
//	public List<EndPoint> getAllUrl() {
//		RequestMappingHandlerMapping mapping = ApplicationContextProvider.getBean(RequestMappingHandlerMapping.class);
//		// Get the corresponding information of url and class and method
//		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
//		List<EndPoint> endPoints=new ArrayList<>();
//		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
//		for (Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
//			Map<String, String> map1 = new HashMap<String, String>();
//			RequestMappingInfo info = m.getKey();
//			HandlerMethod method = m.getValue();
//			PatternsRequestCondition p = info.getPatternsCondition();
//			EndPoint ep = new EndPoint();
//			for (String url : p.getPatterns()) {
//				map1.put("url", url);
//				ep.url = url;
//			}
//			map1.put("className", method.getMethod().getDeclaringClass().getName()); // class name
//			ep.mapping = method.getMethod().getDeclaringClass().getName()+":"+method.getMethod().getName();
//			map1.put("method", method.getMethod().getName()); // method name
//			RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
//			List<String> methods=new ArrayList<>();
//			for (RequestMethod requestMethod : methodsCondition.getMethods()) {
//				map1.put("type", requestMethod.toString());
//				methods.add(requestMethod.toString());
//			}
//			ep.mapping = methods+"";//(ep.mapping+"("+methods+")");
//			endPoints.add(ep);
//			list.add(map1);
//		}
//
//		// JSONArray jsonArray = JSONArray.fromObject(list);
//		Collections.sort(endPoints);
//		return endPoints;
//	}
//	
}

class EndPoint implements Comparable<EndPoint>{
	public String url;
	public String mapping;
	@Override
	public int compareTo(EndPoint ep) {
		// TODO Auto-generated method stub
		return url.compareToIgnoreCase(ep.url);
	}

}
