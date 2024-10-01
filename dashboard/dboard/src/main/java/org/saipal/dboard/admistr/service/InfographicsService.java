package org.saipal.dboard.admistr.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.saipal.dboard.admistr.model.BasicInformation;
import org.saipal.dboard.admistr.model.Infographics;
import org.saipal.fmisutil.auth.Authenticated;
import org.saipal.fmisutil.service.AutoService;
import org.saipal.fmisutil.util.DB;
import org.saipal.fmisutil.util.DbResponse;
import org.saipal.fmisutil.util.Messenger;
import org.saipal.fmisutil.util.Paginator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class InfographicsService extends AutoService{
	
	@Autowired
	DB db;

	@Autowired
	Authenticated auth;
	
	private String table = "infographics";
	
	public ResponseEntity<Map<String, Object>> index() {
		String condition = "";
		if (!request("searchTerm").isEmpty()) {
			List<String> searchbles = Infographics.searchables();
			condition += "and (";
			for (String field : searchbles) {
				condition += field + " LIKE '%" + db.esc(request("searchTerm")) + "%' or ";
			}
			condition = condition.substring(0, condition.length() - 3);
			condition += ")";
		}
		condition += " and(orgid= '"+  session("orgid")+"') ";
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
				.select("id,title,subtitle,image,description,status")
				.sqlBody("from " + table + condition).paginate();
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return Messenger.getMessenger().error();
		}
	}

	
	public ResponseEntity<Map<String, Object>> store() {
		String sql = "";
		Infographics model = new Infographics();
		model.loadData(document);
		sql = "INSERT INTO infographics (title,subtitle,description, image,status,orgid, created_by,time) VALUES (?,?,?,?,?,?,?,?)";
		DbResponse rowEffect = db.execute(sql,
				Arrays.asList(model.title, model.subtitle,model.description,model.image ,model.status, session("orgid"),session("empid"),model.time));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	public ResponseEntity<Map<String, Object>> edit(String id) {

		String sql = "select id,title,subtitle,image,description,status,time from "
				+ table + " where id=?";
		Map<String, Object> data = db.getSingleResultMap(sql, Arrays.asList(id));
		System.out.println(data.get("indicator"));
		return ResponseEntity.ok(data);
	}
	
	public ResponseEntity<Map<String, Object>> update(String id) {
		DbResponse rowEffect;
		Infographics model = new Infographics();
		model.loadData(document);

		String sql = "UPDATE infographics set title=?,subtitle=?,description=?,image=?,status=?,orgid=?,created_by=?,time=? where id=?";
		rowEffect = db.execute(sql,
				Arrays.asList(model.title, model.subtitle,model.description,model.image,model.status,session("orgid"),session("empid"),model.time,id));

		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}

	}
	
	public ResponseEntity<Map<String, Object>> destroy(String id) {

		String sql = "delete from infographics where id  = ?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(id));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	
}
