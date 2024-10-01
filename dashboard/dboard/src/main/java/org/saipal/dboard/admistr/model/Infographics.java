package org.saipal.dboard.admistr.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.saipal.fmisutil.parser.RequestParser;

public class Infographics {
	
	public String id;
	public String title;
	public String subtitle;
	public String image;
	public String description;
	public String status;
	public String time;
	
	public void loadData(RequestParser doc) {
		for (Field f : this.getClass().getFields()) {
			String fname = f.getName();
			try {
				f.set(this, doc.getElementById(fname).value);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> searchables() {
		return Arrays.asList("type","status","title","subtitle");
	}

	public static Map<String, String> rules() {
		Map<String, String> rules = new HashMap<>();
		rules.put("image", "required");
		rules.put("status", "required");
		return rules;
	}

}
