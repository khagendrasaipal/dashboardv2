package org.saipal.dboard.contorllers;

import org.saipal.dboard.services.SvgMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SvgMapController {
	@Autowired
	SvgMapService sms;
	
	@ResponseBody
	@GetMapping("/get-svgmap")
	public String getSvgMap() {
		return sms.getSvgMap();
	}
}
