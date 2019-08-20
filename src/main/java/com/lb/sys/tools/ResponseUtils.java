package com.lb.sys.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import net.sf.json.JSONObject;

public class ResponseUtils {
	
	public static final String RES_CODE = "code";
	public static final String RES_MSG = "msg";
	public static final String RES_BODY = "body";
	
	public static final int DEFAULT_CODE = 200;
	public static final String DEFAULT_MSG = "成功";

	public static final String REDIRECT = "redirect:";
	
	
	public static ModelAndView modelView(String viewName, String k, Object v){
		return new ModelAndView(viewName, k, v);
	}
	
	public static ModelAndView modelView(String viewName, Map<String, Object> data){
		return new ModelAndView(viewName, data);
	}
	
	public static ModelAndView modelView(String viewName){
		return new ModelAndView(viewName);
	}

	public static ModelAndView redirectView(String viewName){
		RedirectView rv = new RedirectView(viewName);
		return new ModelAndView(rv);
	}

	public static ModelAndView redirectView(String viewName, String k, Object v){
		Map<String, Object> data = new HashMap<>();
		data.put(k, v);
		return redirectView(viewName, data);
	}

	public static ModelAndView redirectView(String viewName, Map<String, Object> data){
		RedirectView rv = new RedirectView(viewName);
		rv.setAttributesMap(data);
		return new ModelAndView(rv);
	}
	
	public static ModelAndView jsonView(Map<String, Object> data){
		MappingJackson2JsonView jv = new MappingJackson2JsonView();
		jv.setObjectMapper(JSONUtils.getMapper());
		jv.setAttributesMap(data);
		ModelAndView mv = new ModelAndView(jv);
		return mv;
	}
	
	public static ModelAndView jsonView(int code, String msg){
		return jsonView(createBody(code, msg));
	}
	
	public static ModelAndView jsonView(Object body){
		return jsonView(createBody(DEFAULT_CODE, DEFAULT_MSG, body));
	}
	
	public static ModelAndView jsonView(int code, String msg, Object body){
		return jsonView(createBody(code, msg, body));
	}
	
	public static Map<String, Object> createBody(int code, String msg, Object body){
		Map<String, Object> res = new HashMap<>();
		res.put(RES_CODE, code);
		res.put(RES_MSG, msg);
		if(body != null){
			res.put(RES_BODY, body);
		}
		return res;
	}
	
	public static Map<String, Object> createBody(Object body){
		return createBody(DEFAULT_CODE, DEFAULT_MSG, body);
	}
	
	public static Map<String, Object> createBody(int code, String msg){
		return createBody(code, msg, null);
	}
	
	public static void responseException(HttpServletResponse response,Object code,String msg) throws IOException {
		Map<String, Object> map = new HashMap<>();
		map.put("code", code);
		map.put("msg",msg);
		response.getWriter().println(JSONObject.fromObject(map));
	}

}
