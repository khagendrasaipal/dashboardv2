package org.saipal.dboard.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Tuple;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.saipal.dboard.admistr.model.CompositeIndicatorValue;
import org.saipal.fmisutil.util.DbResponse;
import org.saipal.fmisutil.util.HttpRequest;
import org.saipal.fmisutil.util.Messenger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DashboardService extends AutoService {
	// get indicator id, fy,
	// if fiscal year is multiple show palika data
	// if fiscal year is single show wardwise dat
	@Value("${apis.hmis.baseurl}")
	String hmisBaseUrl;

	@Value("${apis.sutra.baseurl}")
	String sutraBaseUrl;

	@Value("${apis.sutra.secret}")
	String sutraKey;

	@Value("${apis.hmis.client}")
	String client;

	@Value("${apis.hmis.secret}")
	String secret;
	
	
	
	
	
	public ResponseEntity<Map<String, Object>> getData(String orgid) {
		String type = request("type");
		if (type.equals("f")) {
			return getDataFinance(orgid);
		}
		Tuple t = db.getSingleResult("select uid,namenp from organization where id=?", Arrays.asList(orgid));
		String charttype = request("charttype");
		String puid = t.get("uid") + "";
		Map<String, String> wards = new HashMap<>();
		String od;
		String yd;
		String id;
		String indicators = request("mindicator").replace("[", "").replace("]", "");
		String ntype;
		System.out.println("combo "+indicators);
		Map<String, Map<String, String>> indreg = new HashMap<>();
		if (indicators.contains(",")) {
//			System.out.println("select * from  indicators where uid in (" + indicators.replace("\"", "'") + ")");
			List<Tuple> tind = db
					.getResultList("select * from  indicators where uid in (" + indicators.replace("\"", "'") + ")");
			 ntype=tind.get(0).get("value_type")+"";
			for (Tuple tt : tind) {
				indreg.put(tt.get("uid") + "", Map.of("nameen", tt.get("name") + "", "namenp", tt.get("namenp") + ""));
			}
		} else {
			
			Tuple tind = db.getSingleResult("select * from  indicators where uid = '" + indicators + "'");
			 ntype=tind.get("value_type")+"";
			indreg.put(tind.get("uid") + "",
					Map.of("nameen", tind.get("name") + "", "namenp", tind.get("namenp") + ""));
		}
		String fys = request("myear").replace("[", "").replace("]", "");
		int wardFlag = 0;
		if (fys.contains(",")) {
//			System.out.println("oe oe ");
			wardFlag = 0;
			// show data only for palika
			od = "filter=ou:" + puid;
			yd = "dimension=pe:" + fys.replace(",", "April;") + "April";
			id = "dimension=dx:" + indicators.replace(",", ";").replace("\"", "");
		} else {
			wardFlag = 1;
//			System.out.println("i am agin here");
			// show the data for all wards
			wards = getWardInfo(puid);
			od = "dimension=ou:" + String.join(";", wards.keySet());
			yd = "filter=pe:" + fys.replace(",", "April;") + "April";
			id = "dimension=dx:" + indicators.replace(",", ";").replace("\"", "");
		}
		
		String url = hmisBaseUrl + "/api/analytics.json?" + od + "&" + id + "&" + yd;

		
		JSONObject data = requestData(url);
		System.out.println(data.toString());
		List<String> cats = new ArrayList<>();
		List<Object> series = new ArrayList<>();
		Map<String, Map<String, String>> valReg = new HashMap<>();
		if (data != null) {
			try {
				JSONArray rows = data.getJSONArray("rows");
//				System.out.println(rows.toString());
				if (rows != null) {
					for (int i = 0; i < rows.length(); i++) {
						JSONArray rd = rows.getJSONArray(i);
						if (valReg.containsKey(rd.get(0))) {
							valReg.get(rd.get(0) + "").put((rd.get(1) + "").replace("April", ""), rd.get(2) + "");
						} else {
							Map<String, String> dt = new HashMap<>();
							dt.put((rd.get(1) + "").replace("April", ""), rd.get(2) + "");
							valReg.put(rd.get(0) + "", dt);
						}
					}
					if (rows.length() > 0) {
						if (wardFlag == 0) {
							// Multiyear palika data
							// Keep years in X-axis
							cats = getFullFys(fys);
							String[] inds;
							List<String> yrs;
							if (indicators.contains(",")) {
								inds = indicators.replace("\"", "").split(",");
							} else {
								inds = new String[] { indicators };
							}
							if (fys.contains(",")) {
								yrs = Arrays.asList(fys.split(","));
							} else {
								yrs = Arrays.asList(fys);
							}
							Collections.sort(yrs);
							// loop indicators
							// loop Years
							for (String ind : inds) {
								HashMap<String, Object> sd = new HashMap<>();
								sd.put("name", indreg.get(ind).get("namenp"));
								sd.put("type", charttype);
								List<Double> dt = new ArrayList<>();
								for (String yr : yrs) {
									if (valReg.containsKey(ind)) {
										if (valReg.get(ind).containsKey(yr)) {
											dt.add(Double.parseDouble(valReg.get(ind).get(yr)));
										} else {
											dt.add(0d);
										}
									} else {
										dt.add(0d);
									}
								}
								sd.put("data", dt);
								series.add(sd);
							}
						} else {
							// Single Year Wardwise data
							// keep Wards in X-axis
							if (wards != null) {
								cats = wards.values().stream().collect(Collectors.toList());
								String[] inds;
								List<String> yrs;
								if (indicators.contains(",")) {
									inds = indicators.replace("\"", "").split(",");
								} else {
									inds = new String[] { indicators };
								}
								if (fys.contains(",")) {
									yrs = Arrays.asList(fys.split(","));
								} else {
									yrs = Arrays.asList(fys);
								}
								Collections.sort(yrs);
								// loop indicators
								// loop Wards
								for (String ind : inds) {
									HashMap<String, Object> sd = new HashMap<>();
									sd.put("name", indreg.get(ind).get("namenp"));
									sd.put("type", charttype);
									List<Double> dt = new ArrayList<>();
									for (String wd : wards.keySet()) {
										if (valReg.containsKey(ind)) {
											if (valReg.get(ind).containsKey(wd)) {
												dt.add(Double.parseDouble(valReg.get(ind).get(wd)));
											} else {
												dt.add(0d);
											}
										} else {
											dt.add(0d);
										}
									}
									sd.put("data", dt);
									series.add(sd);
								}
							}
						}
					}
				}
//				System.out.println(cats);
//				System.out.println(series);
				ntype = ntype.equals("n")?"No.":"%";
				// System.out.println(createTable(cats, series));
				if (charttype.equals("table")) {
					return Messenger.getMessenger().setData(createTable(cats, series)).success();
				}
				List<Map<String, Object>> list = new ArrayList<>();
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("cats", cats);
				mapadmlvl.put("series", series);
				mapadmlvl.put("title", Map.of("text", t.get("namenp")));
				mapadmlvl.put("subtitle", Map.of("text", "Source:HMIS"));
				mapadmlvl.put("ntype", ntype);
				list.add(mapadmlvl);
				return Messenger.getMessenger().setData(list).success();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Messenger.getMessenger().error();
	}

	private ResponseEntity<Map<String, Object>> getDataFinance(String orgid) {
		
		Tuple t = db.getSingleResult("select uid,sutraid,namenp from organization where id=?",Arrays.asList(orgid));
		String charttype=request("fcharttype");
		String puid = t.get("sutraid")+"";
		Map<String, String> wards = new HashMap<>();
		String od;
		String yd;
		String id;
		String indicators = request("findicator");
		//broad-sector,sector,sectorial-activity
		Map<String,String> repType = Map.of("1","broad-sector","2","sector","3","sectorial-activity","4","broad-sector");
		Map<String,Map<String,String>> indreg = new HashMap<>();
		
		String fys = request("fyear");
		String fFy = fys+(Integer.parseInt(fys)+1);
		String fFs = fys+"/"+(Integer.parseInt(fys)+1);
		
		String url = sutraBaseUrl + "/new/api/budget/"+repType.get(indicators)+"/index.asp"+"?key="+sutraKey+"&fiscalyear="+fFy+"&adminid="+puid;
		System.out.println(url);
		HttpRequest req = new HttpRequest();
		JSONObject response = req.get(url);
		try {
			List<String> cats = new ArrayList<>();
			List<Object> series = new ArrayList<>();
			if(response.getInt("status_code")==200) {
				JSONArray data = response.getJSONArray("data");
				if(indicators.equals("1")) {
					Double tbudget = 0d;
					Double texp = 0d;
					for(int i=0;i<data.length();i++) {
						 tbudget += Double.parseDouble(data.getJSONObject(i).getString("totalbudget"));
				          texp +=Double.parseDouble(data.getJSONObject(i).getString("expenditure"));
					}
					cats.add("Budget");
					cats.add("Expenditure");
					
					HashMap<String, Object> sd = new HashMap<>();
					sd.put("name", "Budget");
					sd.put("type", charttype);
					List<Double> dt = new ArrayList<>();
					dt.add(tbudget);
					series.add(sd);
					
					sd = new HashMap<>();
					sd.put("name", "Expenditure");
					sd.put("type", charttype);
					dt = new ArrayList<>();
					dt.add(texp);
					series.add(sd);
				}else {
					List<String> srs= Arrays.asList("Budget","Expenditure");
					int catFlag = 0;
					for(String sr:srs) {
						HashMap<String, Object> sd = new HashMap<>();
						sd.put("name", sr);
						sd.put("type", charttype);
						List<Double> dt = new ArrayList<>();
						for(int i=0;i<data.length();i++) {
							if(catFlag==0) {
								cats.add(data.getJSONObject(i).getString("namenp"));
							}
							//dt = ;
							 if(sr.equals("Budget")) {
								 dt.add(Double.parseDouble(data.getJSONObject(i).getString("totalbudget")));
							 }else {
								 dt.add(Double.parseDouble(data.getJSONObject(i).getString("expenditure")));
							 }
						}
						catFlag++;
						sd.put("data",dt);
						series.add(sd);
					}
				}
			}
			if (charttype.equals("table")) {
				return Messenger.getMessenger().setData(createTable(cats, series)).success();
			}
			List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> mapadmlvl = new HashMap<>();
			mapadmlvl.put("cats", cats);
			mapadmlvl.put("series", series);
			mapadmlvl.put("title", Map.of("text", t.get("namenp")+" ("+fFs+")"));
			mapadmlvl.put("subtitle", Map.of("text", "Source:SUTRA"));
			list.add(mapadmlvl);
			return Messenger.getMessenger().setData(list).success();
		}catch (JSONException e) {
			return Messenger.getMessenger().error();
		}
	}
	
	

	public String createTable(List<String> cats, List<Object> series) {
		String table = "<table class='table table-bordered table-striped'>";
		String rows = "";
		String thead = "<tr><td></td>";
		for (int i = 0; i < cats.size(); i++) {
			rows += "<tr><td>" + cats.get(i) + "</td>";
			for (int j = 0; j < series.size(); j++) {
				Map<String, Object> o = (Map<String, Object>) series.get(j);
				if (i == 0) {
					thead += "<th>" + o.get("name") + "</th>";
				}
				List<Double> l = (List<Double>) o.get("data");
				rows += "<td>" + l.get(i) + "</td>";
			}
			rows += "</tr>";
		}
		thead += "</tr>";
		table += thead + rows + "</table>";
		return table;
	}

	public List<String> getFullFys(String fys) {
		if (!fys.contains(",")) {
			return Arrays.asList(fys + "/" + ((Integer.parseInt(fys) + 1) + "").substring(2));
		} else {
			List<String> fyrs = new ArrayList<>();
			String[] fy = fys.split(",");
			for (String f : fy) {
				fyrs.add(f + "/" + ((Integer.parseInt(f) + 1) + "").substring(2));
			}
			Collections.sort(fyrs);
			return fyrs;
		}
	}

	public void saveData() {

	}

	public Map<String, String> getWardInfo(String palikaId) {
		String url = hmisBaseUrl + "/api/organisationUnits/" + palikaId + "?includeChildren=true";
		JSONObject data = requestData(url);
//		System.out.println(data);
		Map<String, String> wards = new HashMap<>();
		LinkedHashMap<String, String> rwards = new LinkedHashMap<>();
		if (data != null) {
			if (data.length() > 0) {
				JSONArray orgUnits;
				try {
					orgUnits = data.getJSONArray("organisationUnits");
					// wards = new LinkedHashMap<>();
					for (int i = 0; i < orgUnits.length(); i++) {
						if (i == 0)
							continue;
						wards.put(orgUnits.getJSONObject(i).getString("shortName"),
								orgUnits.getJSONObject(i).getString("id"));

					}
					List<String> wlist = wards.keySet().stream().collect(Collectors.toList());
					String[] swl = new String[40];
					for (String wn : wlist) {
						String twn = wn.trim();
						System.out.println(twn);
						if(!twn.matches(".*[-].*")) {
							if(Character.isDigit(twn.charAt(twn.length()-1))){
								int wni = Integer.parseInt(wn.replaceAll("[^0-9]", ""));
							
								swl[wni] = wn;
							}
							
						}
						
						
					}
					for (String n : swl) {
						if (n != null) {
							rwards.put(wards.get(n), n);
						}
					}
					return rwards;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	

	public ResponseEntity<Map<String, Object>> getIndicators() {
		Map<String, Object> retMap = new HashMap<>();
		List<Tuple> tl = db.getResultList("select * from indicators where publish=1 order by name");
		if (tl != null) {
			List<Object> lst = new ArrayList<>();
			for (Tuple t : tl) {
				lst.add(Map.of("name", t.get("name"), "uid", t.get("uid")));
			}
			retMap.put("data", lst);
			return ResponseEntity.ok(retMap);
		}
		return ResponseEntity.ok(retMap);
	}
	
	public ResponseEntity<Map<String, Object>> getSlider() {
//		auth.get
		Map<String, Object> retMap = new HashMap<>();
		List<Tuple> tl = db.getResultList("select * from infographics where status=1 and orgid=100 order by id");
		if (tl != null) {
			List<Object> lst = new ArrayList<>();
			for (Tuple t : tl) {
				lst.add(Map.of("title", t.get("title"), "image", t.get("image")));
			}
			retMap.put("data", lst);
			return ResponseEntity.ok(retMap);
		}
		return ResponseEntity.ok(retMap);
	}
	
	public ResponseEntity<Map<String, Object>> getSliders() {
//		auth.get
		Map<String, Object> retMap = new HashMap<>();
		List<Tuple> tl = db.getResultList("select * from infographics where status=1 and orgid="+auth.getOrgId()+" order by id");
		if (tl != null) {
			List<Object> lst = new ArrayList<>();
			for (Tuple t : tl) {
				lst.add(Map.of("title", t.get("title"), "image", t.get("image")));
			}
			retMap.put("data", lst);
			return ResponseEntity.ok(retMap);
		}
		return ResponseEntity.ok(retMap);
	}
	
	public ResponseEntity<Map<String, Object>> getFyConfig() {
		Map<String, Object> retMap = new HashMap<>();
		List<Tuple> tl = db.getResultList("select * from fy_config where id=1 ");
		if (tl != null) {
			List<Object> lst = new ArrayList<>();
			for (Tuple t : tl) {
				lst.add(Map.of("name", t.get("value"), "id", t.get("id")));
			}
			retMap.put("data", lst);
			return ResponseEntity.ok(retMap);
		}
		return ResponseEntity.ok(retMap);
	}

	public JSONObject requestData(String url) {
		String credentials = Base64.getEncoder().encodeToString((client + ":" + secret).getBytes());
//		System.out.println(credentials);
		System.out.println(url);
		HttpRequest req = new HttpRequest();
		try {
			JSONObject res = req.setHeader("Authorization", "Basic " + credentials)
					.setHeader("Accept", "application/json").get(url);
//			System.out.println("here");
//			System.out.println(res.toString());
			if (res.getInt("status_code") == 200) {
				JSONObject data = res.getJSONObject("data");
				return data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ResponseEntity<Map<String, Object>> saveConfig() {
		String sql = "";
		String myear = request("myear");
		String mindicator = request("mindicator");
		String orgid = request("orgid");
		String charttype = request("charttype");
		String order = request("order");
		String name = request("name");
		String time = request("time");
		String cid=request("cid");

		sql = "INSERT INTO dcharts (mindicator,myear,charttype,orders,name,orgid,time,cid) VALUES (?,?,?,?,?,?,?,?)";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(mindicator, myear, charttype, order, name, orgid, time,cid));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			System.out.println(rowEffect.getMessage());
			return Messenger.getMessenger().error();
		}
	}

	
	public ResponseEntity<Map<String, Object>> getConfig() {
		String orgid = request("orgid");
		String cid= request("cid");
		String dt=request("dt");
		String sql="";
		if(dt.equals("1")) {
			 sql = "select id,name,orders,myear,mindicator,charttype,status,cid from dcharts where orgid=? and cid=? and status=1 order by orders";
		}else {
			 sql = "select id,name,orders,myear,mindicator,charttype,status,cid from dcharts where orgid=? and cid=? order by orders";
		}
		
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(orgid,cid));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("id", t.get("id"));
				mapadmlvl.put("name", t.get("name"));
				mapadmlvl.put("order", t.get("orders"));
				mapadmlvl.put("myear", t.get("myear"));
				mapadmlvl.put("type", t.get("cid"));
				mapadmlvl.put("mindicator", t.get("mindicator"));
				mapadmlvl.put("charttype", t.get("charttype"));
				mapadmlvl.put("status", t.get("status"));
				mapadmlvl.put("cid", "cc" + t.get("id"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().setData(list).success();
		}
	}

	public ResponseEntity<Map<String, Object>> SaveSetting() {
		DbResponse rowEffect;
		String id = request("id");
		String sql1 = "select * from dcharts where id=?";
		List<Tuple> admlvl = db.getResultList(sql1, Arrays.asList(id));
		String stat = "";
		if (Integer.parseInt(admlvl.get(0).get("status").toString()) == 1) {
			stat = "0";
		} else {
			stat = "1";
		}
		String sql = "UPDATE dcharts set status=? where id=?";
		rowEffect = db.execute(sql, Arrays.asList(stat, id));

		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}

	public ResponseEntity<Map<String, Object>> destroy(String id) {
		String sql = "delete from dcharts where id  = ?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(id));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	
	public ResponseEntity<Map<String, Object>> getTodayLogs() {
		String usertype=request("usertype");
		String orgid=auth.getOrgId();
		System.out.println(orgid+" orgid");
		String sql2 = "select adm_id  from organization where id=?";
		List<Tuple> orgs = db.getResultList(sql2, Arrays.asList(orgid));
		String pid=orgs.get(0).get("adm_id").toString();
		//		String user=auth.getu
//		String dt = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String dt=request("dates");
		String td= dt.replaceAll("-","");
		String sql="";
		List<Tuple> admlvl=null;
		if(usertype.equals("hmis")) {
			 sql = "select id,orgname,(case WHEN login>0 then 'Yes' else 'No' end) as login,(case WHEN screen>0 then 'Yes' else 'No' end) as screen from(select organization.id,organization.name as orgname,log.action,sum(case WHEN log.action='login' then 1 else 0 end) as login,sum(case WHEN log.action='screen' and (log.info LIKE '%4K%' or log.info LIKE '%tv%' or log.info LIKE '%fangcheng %' or log.info LIKE '%Android%') then 1 else 0 end) as screen  from organization left join log on log.orgid=organization.id and log.dateint=? where organization.adm_level=4  GROUP by organization.id) a order by orgname";
			 admlvl = db.getResultList(sql, Arrays.asList(td));
		}
		
		if(usertype.equals("province")) {
			 sql = "select id,orgname,(case WHEN login>0 then 'Yes' else 'No' end) as login,(case WHEN screen>0 then 'Yes' else 'No' end) as screen from(select organization.id,organization.name as orgname,log.action,sum(case WHEN log.action='login' then 1 else 0 end) as login,sum(case WHEN log.action='screen' and (log.info LIKE '%4K%' or log.info LIKE '%tv%' or log.info LIKE '%fangcheng %' or log.info LIKE '%Android%') then 1 else 0 end) as screen  from organization left join log on log.orgid=organization.id and log.dateint=? join admin_local_level_structure on admin_local_level_structure.vcid=organization.adm_id where admin_local_level_structure.provinceid=? and organization.adm_level=4 GROUP by organization.id) a order by orgname";
			 admlvl = db.getResultList(sql, Arrays.asList(td,pid));
		}
		

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("id", t.get("id"));
				mapadmlvl.put("orgname", t.get("orgname"));
				mapadmlvl.put("login", t.get("login"));
				mapadmlvl.put("screen", t.get("screen"));
				
				
				
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	public String getNepaliDate() {
		String curEngDate = DateTimeFormatter.ofPattern("YYY-MM-dd").format(LocalDateTime.now());
		System.out.println(curEngDate);
		String nepDate = db.getSingleResult("select nepdate(?)",Arrays.asList(curEngDate)).get(0)+"";
		return nepDate.replace("/", "-");
	}

}
