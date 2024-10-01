package org.saipal.dboard.config;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.saipal.dboard.service.AuthService;
import org.saipal.dboard.service.SystemConfigService;
import org.saipal.dboard.service.UserService;
import org.saipal.dboard.util.FmisUtil;
import org.saipal.fmisutil.auth.Authenticated;
import org.saipal.fmisutil.parser.RequestParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
@Order(2)
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	org.saipal.dboard.util.JwtUtil jUtil;

	@Autowired
	UserService userDetailService;

	Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Autowired
	SystemConfigService service;

	@Autowired
	AuthService aService;

	@Autowired
	WebApplicationContext context;
	
	@Autowired
	Authenticated auth;
	
	@Autowired
	RequestParser doc;

	AntPathMatcher matcher = new AntPathMatcher();

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String requestURI = request.getRequestURI();
		for (String pattern : PathConfigs.excludeResource) {
			if (matcher.match(pattern, requestURI)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		log.info("Request Uri -> " + requestURI);
		String remoteAdr = request.getHeader("X-FORWADED-FOR");
		if (remoteAdr == null || "".equals(remoteAdr)) {
			remoteAdr = request.getRemoteAddr();
		}
		String tenantName = FmisUtil.getTenantName(request);
		ContextHolder.setBaseUrl(request.getScheme() + "://" + tenantName);
		if (service.valideAndSetTenant(tenantName)) {
			String jwt = getTokenfromRequest(request);
			if (jwt != null) {
				try {
					/*
					 * @Author: Lifecracker87 Accesses token from request header Validates the token
					 * Set authorized data to a Authrepo, which is accessable from everywhere in the
					 * project using Authenticated servcice
					 */
					String userid = jUtil.extractInfo(jwt);
					log.info("logged in user: " + userid);
					UsernamePasswordAuthenticationToken springAuthToken = new UsernamePasswordAuthenticationToken(
							userid, null, new ArrayList<>());
					springAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(springAuthToken);
					// auth.setToken(jwt);
					auth.setUserId(userid);
					auth.setAppId(ContextHolder.getTenant());
					auth.setOrgId(userDetailService.getOrgId(userid, ContextHolder.getTenant()));
					auth.setJti(jUtil.extractId(jwt));
					auth.setAllUserStates();
					
					// sets data to extrainfo accessible from session
					// auth.setUserstate("requestid",userDetailService.newidint()+"");
					// auth.setUserstate("language","Np");
					// auth.setUserstate("baseUrl",FmisUtil.getBaseUrl(request)+"");
					// auth.setUserstate(userDetailService.getUserInfo(userid));
					// draws state data from database
					// auth.setOrgId(auth.getUserState("orgid"));
				} catch (ExpiredJwtException e) {
					// aService.removeCookie(request, response, "jwt");
					// aService.removeCookie(request, response, "sessionid");
					setHeaderForEx(response, 0, "Token Exprired");
					return;
				} catch (UnsupportedJwtException e) {
					// aService.removeCookie(request, response, "jwt");
					// aService.removeCookie(request, response, "sessionid");
					setHeaderForEx(response, 0, "Token not supported");
					return;
				} catch (MalformedJwtException e) {
					// aService.removeCookie(request, response, "jwt");
					// aService.removeCookie(request, response, "sessionid");
					setHeaderForEx(response, 0, "Malformed token");
					return;
				} catch (SignatureException e) {
					// aService.removeCookie(request, response, "jwt");
					// aService.removeCookie(request, response, "sessionid");
					setHeaderForEx(response, 0, "Invalid signature in the token");
					return;
				} catch (IllegalArgumentException e) {
					// aService.removeCookie(request, response, "jwt");
					// aService.removeCookie(request, response, "sessionid");
					setHeaderForEx(response, 0, "Illigal token");
					return;
				}
			}
			filterChain.doFilter(request, response);
		}
	}

	public String getTokenfromRequest(HttpServletRequest request) {
		// check if token is contained in Header or Request
		if (request.getHeader("Authorization") == null) {
			if (doc.getElementById("_token").getValue().isBlank()) {
				// check if token is contained in cookie
				// String token = getFromCookie(request, "jwt");
				// if (token != null) {
				// return token;
				// }
				// return null;
			}
			return doc.getElementById("_token").getValue();
		}
		return request.getHeader("Authorization").replace("Bearer ", "");
	}

	public String getFromCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					return cookie.getValue();
				}
			}
		}
		return null;

	}

	public void setHeaderForEx(HttpServletResponse response, int code, String mesage) throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getOutputStream().print(message(code, mesage));
	}

	public String message(int code, String mesage) {
		return "{\"status\":" + code + ",\"message\":\"" + mesage + "\"}";
	}

}
