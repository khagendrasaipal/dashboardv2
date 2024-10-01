package org.saipal.dboard.services;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Tuple;

import org.saipal.dboard.service.AutoService;
import org.springframework.stereotype.Component;


@Component
public class SvgMapService extends AutoService{

	public String getSvgMap() {
		String llgid = request("llgid");
		llgid = "100612250451902230";
		Tuple svgProps = db.getSingleResult("select * from p_map_svg_details where palikaid=?",Arrays.asList(llgid));
		String svg = getSvgFormat(svgProps);
		List<Tuple> wardProps = db.getResultList("select * from palika_map where llgid=?",Arrays.asList(llgid));
		String paths="";
		String labels = "<g id=\"Labels\"><g id=\"NEPAL_WARDS_-_Default\">";
		for(Tuple t : wardProps) {
			paths +=getPath(t);
			labels +=getLabel(t);
		}
		labels +="</g></g>";
		return svg.replace("{{WPATHS}}",paths).replace("{{WLABELS}}",labels);
	}
	
	private String getSvgFormat(Tuple t) {
		return "<svg width=\"623.25354pt\" height=\"434.23937pt\" viewBox=\""+t.get("viewbox")+"\"  enable-background=\""+t.get("enablebg")+"\""
				+ "	version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" >"
				+ "	<g id=\"Layers\">"
				+ "		<g id=\"NEPAL_WARDS\">"
				+ "			<clipPath id=\""+t.get("palikaid")+"\">"
				+ "				<path d=\""+t.get("clippathd")+"\"/>"
				+ "			</clipPath>"
				+"{{WPATHS}}"
				+"{{WLABELS}}"
				+"</g></g></svg>";
				
	}
	private String getPath(Tuple t) {
		return "<path class=\"ward-path\" onclick=\"getWardDetails('"+t.get("llgid")+"','"+t.get("ward_no")+"')\" clip-path=\"url(#"+t.get("llgid")+")\" fill=\"#73B273\" fill-rule=\"evenodd\" stroke=\"#343434\" stroke-width=\"0.95987\" stroke-miterlimit=\"10\" stroke-linecap=\"round\" stroke-linejoin=\"round\" d=\""+t.get("d")+"\"/>";
	}
	
	private String getLabel(Tuple t) {
		return "<g font-family=\"'Arial'\" font-size=\"1\" kerning=\"0\" font-weight=\"400\" fill=\"#000000\" clip-path=\"url(#"+t.get("llgid")+")\" >"
				+ "					<text text-anchor=\"start\" transform=\"matrix("+t.get("lm")+")\" >"
				+ "						<tspan direction=\"ltr\" unicode-bidi=\"embed\" x=\"0 \">"+t.get("ward_no")+"</tspan>"
				+ "					</text>"
				+ "				</g>";
	}
}
