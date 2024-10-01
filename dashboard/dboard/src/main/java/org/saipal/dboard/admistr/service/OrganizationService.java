package org.saipal.dboard.admistr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.saipal.dboard.admistr.model.Organization;
import org.saipal.fmisutil.auth.Authenticated;
import org.saipal.fmisutil.service.AutoService;
import org.saipal.fmisutil.util.DB;
import org.saipal.fmisutil.util.DbResponse;
import org.saipal.fmisutil.util.Messenger;
import org.saipal.fmisutil.util.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OrganizationService extends AutoService {
	@Autowired
	DB db;

	@Autowired
	Authenticated auth;

	private String table = "organization";

	public ResponseEntity<Map<String, Object>> index() {
		String condition = "";
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
				.select("id,code,name")
				.sqlBody("from " + table + condition).paginate();
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	public ResponseEntity<Map<String, Object>> index1() {
		String condition = " where  adm_level=4 ";
		if (!request("searchTerm").isEmpty()) {
			List<String> searchbles = Organization.searchables();
			condition += "and (";
			for (String field : searchbles) {
				condition += field + " LIKE '%" + db.esc(request("searchTerm")) + "%' or ";
			}
			condition = condition.substring(0, condition.length() - 3);
			condition += ")";
		}
//		if (!condition.isBlank()) {
//			condition = " where  adm_level=4 " + condition;
//		}
		String sort = "";
		if(!request("sortKey").isBlank()) {
			if(!request("sortDir").isBlank()) {
				sort = request("sortKey")+" "+request("sortDir");
			}
		}

		Paginator p = new Paginator();
		Map<String, Object> result = p.setPageNo(request("page")).setPerPage(request("perPage"))
				.setOrderBy(sort)
				.select("id,uid,code,name,namenp")
				.sqlBody("from " + table + condition ).paginate();
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return Messenger.getMessenger().error();
		}
	}

	public ResponseEntity<Map<String, Object>> store() {
		String sql = "";
		Organization model = new Organization();
		model.loadData(document);
		if(auth.getUserId().equals("16") || auth.getUserId().equals("145")) {
		sql = "INSERT INTO organization (code, name, adm_level, adm_id, parent,namenp,sutraid,uid) VALUES (?,?,?,?,?,?,?,?)";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(model.code, model.name, model.adm_level, model.adm_id,
				model.parent,model.namenp,model.adm_id,model.uid));
		if (rowEffect.getErrorNumber() == 0) {
			
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
		}else {
			return Messenger.getMessenger().error();
		}
		
	}
	
	public ResponseEntity<Map<String, Object>> getAdminlvl() {
		String sql = "select cast(levelid as CHAR) as levelid,levelnameen,levelnamenp from admin_level where disabled=? and approved=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(0, 1));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("levelid", t.get("levelid"));
				mapadmlvl.put("levelnamenp", t.get("levelnamenp"));
				mapadmlvl.put("levelnameen", t.get("levelnameen"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().error();
		}
	}

	public ResponseEntity<Map<String, Object>> getFederal() {
		String sql = "select cast(id as CHAR) as id,nameen,namenp from admin_federal where disabled=? and approved=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(0, 1));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("id", t.get("id"));
				mapadmlvl.put("namenp", t.get("namenp"));
				mapadmlvl.put("nameen", t.get("nameen"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
//			return Messenger.getMessenger().error();
			return Messenger.getMessenger().setData(list).success();
		}
	}

	public ResponseEntity<Map<String, Object>> getParent(String id) {
		String sql = "select id,name from organization where adm_id=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(id));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("id", t.get("id"));
				mapadmlvl.put("name", t.get("name"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().setData(list).success();
//			return Messenger.getMessenger().error();
		}
	}
	
	public ResponseEntity<Map<String, Object>> getParentOrgs() {
		String admin=request("adminid");
		String level=request("levelid");
		String sql = "select cast(orgidint as CHAR) as orgidint,orgnameen,orgnamenp from admin_org_strs where disabled=? and approved=? and adminlevel=? and adminid=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(0, 1,level,admin));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("orgidint", t.get("orgidint"));
				mapadmlvl.put("orgnamenp", t.get("orgnamenp"));
				mapadmlvl.put("orgnameen", t.get("orgnameen"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().setData(list).success();
		}
	}

	public ResponseEntity<Map<String, Object>> getOrglevel() {
		String sql = "select cast(levelid as CHAR) as levelid,levelnameen,levelnamenp from admin_org_level where disabled=? and approved=?";
		List<Tuple> admlvl = db.getResultList(sql, Arrays.asList(0, 1));

		List<Map<String, Object>> list = new ArrayList<>();
		if (!admlvl.isEmpty()) {
			for (Tuple t : admlvl) {
				Map<String, Object> mapadmlvl = new HashMap<>();
				mapadmlvl.put("levelid", t.get("levelid"));
				mapadmlvl.put("levelnamenp", t.get("levelnamenp"));
				mapadmlvl.put("levelnameen", t.get("levelnameen"));
				list.add(mapadmlvl);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().error();
		}
	}


	public ResponseEntity<Map<String, Object>> edit(String id) {

		String sql = "select id as orgidint,code,name,namenp,uid, adm_level, cast(adm_id as char) as adm_id,parent from "
				+ table + " where id=?";
		Map<String, Object> data = db.getSingleResultMap(sql, Arrays.asList(id));
		return ResponseEntity.ok(data);
	}

	public ResponseEntity<Map<String, Object>> update(String id) {
		DbResponse rowEffect;
		Organization model = new Organization();
		model.loadData(document);
		if(auth.getUserId().equals("16") || auth.getUserId().equals("145")) {
		String sql = "UPDATE organization set code=?,namenp=?,name=?,adm_level=?,adm_id=?,parent=?,sutraid=?,uid=? where id=?";
		rowEffect = db.execute(sql, Arrays.asList(model.code,model.namenp, model.name, model.adm_level, model.adm_id,
				model.parent,model.adm_id,model.uid,id));

		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
		}else {
			return Messenger.getMessenger().error();
		}

	}

	public ResponseEntity<Map<String, Object>> destroy(String id) {
		if(auth.getUserId().equals("16") || auth.getUserId().equals("145")) {
		String sql = "delete from organization where id  = ?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(id));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
		}else {
			return Messenger.getMessenger().error();
		}
	}

	public ResponseEntity<Map<String, Object>> getAdmin(String id) {
		
		String sql="";
		if(id.equals("1")) {
    	    sql = "select id,namenp,nameen as name from admin_federal where disabled=? and approved=?";
		}
		if(id.equals("2")) {
			 sql = "select pid as id,nameen as name,namenp from admin_province where disabled=? and approved=?";
		}
		if(id.equals("4")) {
			 sql = "select cast(vcid as char) as id,nameen as name,namenp from admin_local_level_structure where disabled=? and approved=? order by nameen";
		}
		List<Tuple> province = db.getResultList(sql, Arrays.asList(0, 1));
		List<Map<String, Object>> list = new ArrayList<>();
		if (!province.isEmpty()) {
			for (Tuple t : province) {
				
				Map<String, Object> mapProvince = new HashMap<>();
				mapProvince.put("id", t.get("id"));
				mapProvince.put("namenp", t.get("namenp"));
				mapProvince.put("name", t.get("name"));
				list.add(mapProvince);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().setData(list).success();

		}
	}

	public ResponseEntity<Map<String, Object>> getOrgs() {
		  String sql = "select usertype from users where orgid=?";
		  List<Tuple> province = db.getResultList(sql, Arrays.asList(session("orgid")));
		  
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> mapProvince = new HashMap<>();
		mapProvince.put("orgid", session("orgid"));
		mapProvince.put("orgname", session("orgname"));
		mapProvince.put("usertype", province.get(0).get("usertype"));
		list.add(mapProvince);
		return Messenger.getMessenger().setData(list).success();
	}

	public ResponseEntity<Map<String, Object>> getorganization() {
		String id=auth.getUserId();
		String sql1 = "select usertype,orgid  from users where id=?";
			List<Tuple> users = db.getResultList(sql1, Arrays.asList(id));
			String usertype=users.get(0).get("usertype").toString();
			String orgid=users.get(0).get("orgid").toString();
			
			String sql2 = "select adm_id  from organization where id=?";
			List<Tuple> orgs = db.getResultList(sql2, Arrays.asList(orgid));
			String pid=orgs.get(0).get("adm_id").toString();
			String sql="";
			if(usertype.equals("hmis")) {
				  sql = "select id,name,uid  from organization where adm_level not in(1,2) order by name";
			}
			
			if(usertype.equals("province")) {
				  sql = "select organization.id,organization.name,organization.uid  from organization join admin_local_level_structure on admin_local_level_structure.vcid=organization.adm_id where admin_local_level_structure.provinceid="+pid;
			}
		
		List<Tuple> province = db.getResultList(sql, Arrays.asList());
		List<Map<String, Object>> list = new ArrayList<>();
		if (!province.isEmpty()) {
			for (Tuple t : province) {
				
				Map<String, Object> mapProvince = new HashMap<>();
				mapProvince.put("id", t.get("id"));
				mapProvince.put("uid", t.get("uid"));
				mapProvince.put("name", t.get("name"));
				list.add(mapProvince);
			}
			return Messenger.getMessenger().setData(list).success();

		} else {
			return Messenger.getMessenger().setData(list).success();

		}
	}

	public ResponseEntity<Map<String, Object>> getorg() {
		 String sql = "select id,name,uid  from organization where sutraid!=11 order by name";
		 List<Tuple> province = db.getResultList(sql, Arrays.asList());
			List<Map<String, Object>> list = new ArrayList<>();
			if (!province.isEmpty()) {
				for (Tuple t : province) {
					
					Map<String, Object> mapProvince = new HashMap<>();
					mapProvince.put("id", t.get("id"));
					mapProvince.put("uid", t.get("uid"));
					mapProvince.put("name", t.get("name"));
					list.add(mapProvince);
				}
				return Messenger.getMessenger().setData(list).success();

			} else {
				return Messenger.getMessenger().setData(list).success();

			}
	}
	
	public ResponseEntity<Map<String, Object>> getFyrange() {
		 String sql = "select from_yr,to_yr  from dashboard_fy ";
		 List<Tuple> province = db.getResultList(sql, Arrays.asList());
			List<Map<String, Object>> list = new ArrayList<>();
			if (!province.isEmpty()) {
				for (Tuple t : province) {
					
					Map<String, Object> mapProvince = new HashMap<>();
					mapProvince.put("from", t.get("from_yr"));
					mapProvince.put("to", t.get("to_yr"));
//					mapProvince.put("name", t.get("name"));
					list.add(mapProvince);
				}
				return Messenger.getMessenger().setData(list).success();

			} else {
				return Messenger.getMessenger().setData(list).success();

			}
	}

}
