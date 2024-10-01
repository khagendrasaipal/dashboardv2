package org.saipal.dboard.contorllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.saipal.dboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashBoardController {
	
	@Autowired
	DashboardService ds;
	//get updated data from here
	@ResponseBody
	@PostMapping("dashboard/get-ind-data")
	public ResponseEntity<Map<String, Object>> getData(HttpServletRequest request) {
		return ds.getData(ds.request("orgid"));
	}
	
	@ResponseBody
	@GetMapping("dashboard/save-data")
	public void saveData() {
		ds.saveData();
	}
	
	
	
	@GetMapping("/dashboard/getIndicators")
	public ResponseEntity<Map<String, Object>> getIndicators() {
		return ds.getIndicators();
	}
	
	@GetMapping("/dashboard/getSlider")
	public ResponseEntity<Map<String, Object>> getSlider() {
		return ds.getSlider();
	}
	
	@GetMapping("/dashboard/getSliders")
	public ResponseEntity<Map<String, Object>> getSliders() {
		return ds.getSliders();
	}
	
	@GetMapping("/dashboard/getFyConfig")
	public ResponseEntity<Map<String, Object>> getFyConfig() {
		return ds.getFyConfig();
	}
	
	@GetMapping("/dashboard/getTodayLogs")
	public ResponseEntity<Map<String, Object>> getTodayLogs() {
		return ds.getTodayLogs();
	}
	
	
	@ResponseBody
	@PostMapping("dashboard/saveConfig")
	public ResponseEntity<Map<String, Object>> saveConfig(HttpServletRequest request) {
		return ds.saveConfig();
	}
	
	
	
	@GetMapping("/dashboard/getConfig")
	public ResponseEntity<Map<String, Object>> getConfig() {
		return ds.getConfig();
	}
	
	
	@GetMapping("/dashboard/SaveSetting")
	public ResponseEntity<Map<String, Object>> SaveSetting() {
		return ds.SaveSetting();
	}
	
	@DeleteMapping("/dashboard/deleteCharts/{id}")
	public ResponseEntity<Map<String, Object>> destroy(HttpServletRequest request, @PathVariable String id) {
		return ds.destroy(id);
	}
}
