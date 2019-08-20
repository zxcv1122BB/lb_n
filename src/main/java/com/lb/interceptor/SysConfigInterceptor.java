package com.lb.interceptor;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.dao.SysConfigureMapper;

import net.sf.json.JSONObject;

@Component
public class SysConfigInterceptor implements HandlerInterceptor{
	private final Log LOGGER = LogFactory.getLog(SysConfigInterceptor.class);
	
	@Autowired
	private SysConfigureMapper sysConfigureMapper;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		if (sysConfigureMapper == null) {// 解决service为null无法注入问题
			BeanFactory factory = WebApplicationContextUtils
					.getRequiredWebApplicationContext(request.getServletContext());
			sysConfigureMapper = (SysConfigureMapper) factory.getBean("sysConfigureMapper");
		}
		Byte status = sysConfigureMapper.websiteSwitchStatus();
		LOGGER.error(status);
		if(Byte.valueOf("0").equals(status)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			//设置响应数据
			Map<String, Object> map = new HashMap<>();
			map.put("code", "777");
			map.put("msg", "系统维护");
			//发送响应数据
			response.getWriter().println(JSONObject.fromObject(map));
			LOGGER.error("系统维护");
			return false;
		}
		return true;
	}
}
