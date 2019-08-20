package com.lb.interceptor;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.controller.LoginController;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.LoginCache;

import net.sf.json.JSONObject;
/**
 * 单点登录拦截器操作
 * @author jiangheng
 *
 */
public class SignalLoginInterceptor implements HandlerInterceptor{
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(sysUser==null) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			Map<String, Object> map = new HashMap<>();
			map.put("code", "600");
			map.put("msg", "你已经在其他浏览器中登录");
			response.getWriter().println(JSONObject.fromObject(map));
			return false;
		}
		String userName = sysUser.getUserName();
		if(!LoginCache.checkToken(userName, request)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			Map<String, Object> map = new HashMap<>();
			map.put("code", "600");
			map.put("msg", "你已经在其他浏览器中登录");
			response.getWriter().println(JSONObject.fromObject(map));
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
