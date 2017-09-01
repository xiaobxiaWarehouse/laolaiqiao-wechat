package com.codi.laolaiqiao.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (request.getSession().getAttribute("user_login_session") == null) {
			if (request.getRequestURI()!= null) {
				if (request.getRequestURI().contains("/wechat/register") || request.getRequestURI().contains("/wechat/index") || request.getRequestURI().contains("/wechat/core") || request.getRequestURI().contains("/user/")) {
					return true;
				}
			}
			
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
