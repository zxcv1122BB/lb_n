package com.lb.interceptor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.tools.BaseController;

import net.sf.json.JSONObject;

@Component
public class TimestampInterceptor implements HandlerInterceptor{
	private final Log LOGGER = LogFactory.getLog(TimestampInterceptor.class);
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
		Map<String, Object> jsonToMap = BaseController.getParam(request);
		if(jsonToMap.get("timeStamp")==null||"".equals(jsonToMap.get("timeStamp").toString())) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			//设置响应数据
			Map<String, Object> map = new HashMap<>();
			map.put("code", "666");
			map.put("msg", "请传入时间戳");
			//发送响应数据
			response.getWriter().println(JSONObject.fromObject(map));
			
			LOGGER.error("时间戳解析失败");
			return false;
		}
		Long timeStamp=Long.valueOf(jsonToMap.get("timeStamp").toString());
		Long ct = new Date().getTime();
		//接口访问时间限制10min
		if(ct-timeStamp<1000*60*10) {
//			LOGGER.info("时间戳解析成功");
			return true;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		//设置响应数据
		Map<String, Object> map = new HashMap<>();
		map.put("code", "666");
		map.put("msg", "接口失效");
		//发送响应数据
		response.getWriter().println(JSONObject.fromObject(map));
		
		LOGGER.error("时间戳解析失败");
		return false;
	}
}
