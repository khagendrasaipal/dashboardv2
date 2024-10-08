package org.saipal.dboard.admistr.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.saipal.dboard.admistr.model.Infographics;
import org.saipal.dboard.admistr.service.InfographicsService;
import org.saipal.fmisutil.util.Messenger;
import org.saipal.fmisutil.util.ValidationService;
import org.saipal.fmisutil.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("infographics")
public class InfographicsController {
	@Autowired
	ValidationService validationService;
	
	@Autowired
	 InfographicsService objService;
	
	@GetMapping("")
	public ResponseEntity<Map<String, Object>> index(HttpServletRequest request) {
		return objService.index();
	}
	
	@PostMapping("")
	public ResponseEntity<Map<String, Object>> store(HttpServletRequest request) {
		Validator validator = validationService.validate(Infographics.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return objService.store();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> edit(HttpServletRequest request, @PathVariable String id) {
		return objService.edit(id);

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, Object>> update(HttpServletRequest request, @PathVariable String id) {
		Validator validator = validationService.validate(Infographics.rules());
		if (validator.isFailed()) {
			return Messenger.getMessenger().setData(validator.getErrorMessages()).error();
		} else {
			return objService.update(id);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> destroy(HttpServletRequest request, @PathVariable String id) {
		return objService.destroy(id);
	}
	
}
