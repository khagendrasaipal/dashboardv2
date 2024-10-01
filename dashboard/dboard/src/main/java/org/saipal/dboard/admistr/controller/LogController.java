package org.saipal.dboard.admistr.controller;

import javax.servlet.http.HttpServletRequest;

import org.saipal.dboard.admistr.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {
	
	@Autowired
	private LogService ls;
	
	@PostMapping("/dashboard")
	@ResponseBody
	public void logDashboardScreenShow(HttpServletRequest request) {
		ls.logDashboardScreenShow(request);
	}
	
	@PostMapping("/portal")
	@ResponseBody
	public void logPortalView(HttpServletRequest request) {
		ls.logPortalView(request);
	}
}
