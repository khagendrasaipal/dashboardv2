package org.saipal.dboard.admistr.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.saipal.dboard.admistr.model.Category;
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
public class CategoryService extends AutoService {

	@Autowired
	DB db;

	@Autowired
	Authenticated auth;

	private String table = "category";

	public ResponseEntity<Map<String, Object>> index() {
		String condition = "";
		if (!request("searchTerm").isEmpty()) {
			List<String> searchbles = Category.searchables();
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

	public ResponseEntity<Map<String, Object>> store() {
		String sql = "";
		Category model = new Category();
		model.loadData(document);
		sql = "INSERT INTO category (code,name,dataType) VALUES (?,?,?)";
		DbResponse rowEffect = db.execute(sql,
				Arrays.asList(model.code, model.name));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
	
	public ResponseEntity<Map<String, Object>> edit(String id) {

		String sql = "select id,code,name,dataType from "
				+ table + " where id=?";
		Map<String, Object> data = db.getSingleResultMap(sql, Arrays.asList(id));
		return ResponseEntity.ok(data);
	}

	
	public ResponseEntity<Map<String, Object>> getCensus() {
		String sqls = "select * from organization where id="+auth.getOrgId();
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sqls, argList);
		
		String sql = "select population,house_hold from ll_details  where vcid=?";
		Map<String, Object> data = db.getSingleResultMap(sql, Arrays.asList(tList.get(0).get("adm_id")));
		return ResponseEntity.ok(data);
	}
	
	public ResponseEntity<Map<String, Object>> updatecensus() {
		DbResponse rowEffect;
		String sqls = "select * from organization where id="+auth.getOrgId();
		List<String> argList = new ArrayList<String>();
		List<Tuple> tList = db.getResultList(sqls, argList);
		
		String sql = "UPDATE ll_details set population=?,house_hold=? where vcid=?";
		rowEffect = db.execute(sql,
				Arrays.asList(request("population"), request("house_hold"),tList.get(0).get("adm_id")));

		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}

	}
	
	
	public ResponseEntity<Map<String, Object>> update(String id) {
		DbResponse rowEffect;
		Category model = new Category();
		model.loadData(document);

		String sql = "UPDATE category set code=?,name=?,dataType=? where id=?";
		rowEffect = db.execute(sql,
				Arrays.asList(model.code, model.name,model.dataType,id));

		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}

	}

	public ResponseEntity<Map<String, Object>> destroy(String id) {

		String sql = "delete from category where id  = ?";
		DbResponse rowEffect = db.execute(sql, Arrays.asList(id));
		if (rowEffect.getErrorNumber() == 0) {
			return Messenger.getMessenger().success();

		} else {
			return Messenger.getMessenger().error();
		}
	}
}
