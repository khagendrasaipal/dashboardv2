package org.saipal.dboard.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.saipal.fmisutil.auth.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class RequestInterceptor implements HandlerInterceptor {
	
	@Autowired
	Authenticated auth;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		// String requestURI = request.getRequestURI();
		// String uri = requestURI;
		// if (!request.getContextPath().isBlank()) {
		// uri = uri.replace(request.getContextPath(), "");
		// }
		// service.logForm(uri);
		// check if the user has permission on the specific menu
		if (auth.getUserId() != null) {
			// if (!aService.checkPermission(requestURI.substring(1))) {
			// response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			// return false;
			// }
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// if (modelAndView != null) {
		// if (modelAndView.getViewName() != null) {
		// if (!modelAndView.getViewName().contains("redirect:")) {
		// String baseUrl = ContextHolder.getBaseUrl();
		// modelAndView.addObject("baseUrl", baseUrl);
		// }
		// }
		// }
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		response.setCharacterEncoding("utf-8");
	}

}