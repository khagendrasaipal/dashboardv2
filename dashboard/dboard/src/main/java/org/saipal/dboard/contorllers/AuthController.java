package org.saipal.dboard.contorllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.saipal.dboard.admistr.service.LogService;
import org.saipal.dboard.service.AutoService;
import org.saipal.dboard.service.SystemConfigService;
import org.saipal.dboard.service.User;
import org.saipal.dboard.service.UserService;
import org.saipal.dboard.util.FmisUtil;
import org.saipal.dboard.util.JwtUtil;
import org.saipal.fmisutil.auth.Authenticated;
import org.saipal.fmisutil.util.DB;
import org.saipal.fmisutil.util.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

	@Autowired
	PasswordEncoder en;
	
	@Autowired
	private SystemConfigService sysService;

	@Autowired
	JwtUtil jUtil;

	@Autowired
	DB db;
	
	@Autowired
	AutoService autoService;
	
	@Autowired
	Authenticated auth;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LogService ls;
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && !auth.getClass().equals(AnonymousAuthenticationToken.class)) {
			return "redirect:apphome";
		}
		String ajaxString = request.getHeader("X-Requested-With");
		if (ajaxString != null && ajaxString.equalsIgnoreCase("XMLHttpRequest")) {
			response.getWriter().print("script $('#login').window('open');$('#login').window('refresh', '"
					+ FmisUtil.getBaseUrl(request) + "/relogin');");
			// return "script $('#login').window('open');$('#login').window('refresh',
			// '/relogin'); ";
			return null;
		}
		String error = null;
		if (model.getAttribute("errorMessage") != null) {
			error = model.getAttribute("errorMessage").toString();
		}
		Tuple tuple = sysService.getInfo();
		model.addAttribute("AppName", tuple.get("Appname").toString());
		model.addAttribute("AppFullName", tuple.get("AppFullName").toString());
		model.addAttribute("CopyRight", tuple.get("CopyRight").toString());
		model.addAttribute("errorMessage", error);
		return "login";
	}

	@RequestMapping("/logout-done")
	public String logoutDone(HttpServletRequest request, HttpServletResponse response) {
		autoService.removeCookie(request, response, "jwt");
		auth.clearAllStates();
		return "redirect:login";

	}

	@GetMapping("/ng-login")
	public String ngLogin(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		return "nglogin";
	}
	
	@ResponseBody
	@PostMapping(value="/sign-in")
	public ResponseEntity<Map<String, Object>> signIn(HttpServletRequest request) {
		String username = userService.request("username");
		String password = userService.request("password");
		if(username!=null && password != null) {
			User data;
			if(userService.userType(username)==1) {
				 data = userService.auth(username, password);
			}else {
				 data = userService.loginUsingDhis2();
			}
			if (data != null) {
				final String jwt = jUtil.generateToken(data.getUserid());
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(data.getUserid(),
						null, new ArrayList<>());			
				if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext()
						.getAuthentication().getClass().equals(AnonymousAuthenticationToken.class)) {
					SecurityContextHolder.getContext().setAuthentication(token);
					auth.setUserstateOnLogin(data.getUserid(),jUtil.extractId(jwt),userService.getUserInfo(data.getUserid()));
					ls.logLogins(data.getUserid(), data.getOrgid());
					return Messenger.getMessenger().setMessage("Login Successful").setData(Map.of("user",data,"token",jwt)).success();
				}
				return Messenger.getMessenger().setMessage("Internal Error").error();
			}
			return Messenger.getMessenger().setMessage("Invalid Username or Password").error();
		}
		return Messenger.getMessenger().setMessage("Invalid Request").error();
	}
	
	
	@ResponseBody
	@PostMapping(value="/sign-in-admin")
	public ResponseEntity<Map<String, Object>> signInadmin(HttpServletRequest request) {
		String username = userService.request("username");
		String password = "11";
		if(username!=null ) {
			User data;
//			if(userService.userType(username)==1) {
				 data = userService.auths(username, password);
//			}else {
//				 data = userService.loginUsingDhis2();
//			}
			if (data != null) {
				final String jwt = jUtil.generateToken(data.getUserid());
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(data.getUserid(),
						null, new ArrayList<>());			
				if (SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext()
						.getAuthentication().getClass().equals(AnonymousAuthenticationToken.class)) {
					SecurityContextHolder.getContext().setAuthentication(token);
					auth.setUserstateOnLogin(data.getUserid(),jUtil.extractId(jwt),userService.getUserInfo(data.getUserid()));
					ls.logLogins(data.getUserid(), data.getOrgid());
					return Messenger.getMessenger().setMessage("Login Successful").setData(Map.of("user",data,"token",jwt)).success();
				}
				return Messenger.getMessenger().setMessage("Internal Error").error();
			}
			return Messenger.getMessenger().setMessage("Invalid Username or Password").error();
		}
		return Messenger.getMessenger().setMessage("Invalid Request").error();
	}
	
	@ResponseBody
	public String encryptPassword() {
		if (true) {
			List<Tuple> userList = db.getResultList(
					"select uid,dbo.decript(password) as password,loginname from hr_user_info where userstatus=1");
			for (Tuple t : userList) {
				db.execute("update hr_user_info set password1='" + en.encode(t.get("password") + "")
						+ "' where uid='" + t.get("uid") + "'");
			}

			return "All Password encoded successfully";
		}
		return "";
	}

	@GetMapping("/temp-login/{userId}")
	public String tempLogin(@PathVariable String userId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> data = userService.findUserByUserId(userId);
		if (data.get("message") != null) {
			response.getWriter().print("user not found");
			return null;
		}
		data.get("user");
		return "redirect:applications";

	}

	@GetMapping("/temp-login-destroy")
	public String destroyTempLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object userID = autoService.session("olduserid");
		if (userID != null) {
			return "redirect:applications";
		} else {
			response.getWriter().print("cannot re-login");
			return null;
		}
	}

	@GetMapping("/relogin")
	public String relogin() {
		return "frmrelogin";
	}

	@GetMapping("/en-pass/{password}")
	@ResponseBody
	public String getBcrypt(@PathVariable String password) {
		LOG.info("passoword:" + password);
		return en.encode(password);
	}

	@GetMapping("/en-pass/{id}/{password}")
	@ResponseBody
	public String updatePassword(@PathVariable BigDecimal id, @PathVariable String password) {
		LOG.info("Password:" + password);
		db.execute("update hr_user_info set password1='" + en.encode(password) + "' where uid='" + id + "'");
		return "Password Changed Successfully";
	}

	@GetMapping("")
	public String defaultRoute(HttpServletRequest request) {
		return "index";
	}
	
	@PostMapping("get-user-token")
	@ResponseBody
	public Map<String,Object> getUserToken() {
		String userId = userService.request("userid");
		Map<String,Object> user = userService.findUserByUserId(userId);
		if(user.containsKey("user")) {
			String token = jUtil.generateToken(userId);
			user.put("token",token);
			return user;
		}else {
			return user;
		}
		
	}
}
