package com.lb.interceptor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.controller.LoginController;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.MyMD5Util; 

import net.sf.json.JSONObject;

/**
 * 签名验证,进行拦截
 * @author Administrator
 *
 */
public class SignValidateInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(SignValidateInterceptor.class);

	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception {
		 String serverName = request.getRequestURI();
		 Map<String,Object> strMap = BaseController.getParam(request);
	     Map<String, Object> map = new HashMap<>();
		 if(strMap.containsKey("sign")) {
			 //签名
			String sign = strMap.get("sign").toString();
			strMap.remove("sign");
			 //key 排序
			Set<String> keysSet = strMap.keySet();
	        Object[] keys = keysSet.toArray();
	        Arrays.sort(keys);
			StringBuilder temp = new StringBuilder();
			boolean first = true;
	        for (Object key : keys) {
	            if (first) {
	                first = false;
	            } else {
	                temp.append("&");
	            }
	            temp.append(key).append("=");
	            Object value = strMap.get(key);
	            String valueString = "";
	            if (null != value) {
	                valueString = filter(String.valueOf(value));
	                temp.append(valueString);
	            }
	        }
			System.err.println(temp);
	        SysUser sysUser = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			String userName = sysUser.getUserName();
	        String validateSing = MyMD5Util.encodePassword(temp.toString(),userName).toUpperCase();
	        if(!serverName.equals("/getDepositAndWithdrawCount")) {
	        	 logger.info("【正在请求接口为：】："+serverName+"，【前端sign值】："+sign+"，"
	 	        		+ "【后端sign值】："+validateSing+"，【签名是否一致】："+sign.equals(validateSing));
	        }
	        if(!sign.equals(validateSing)) {
	        	//设置响应数据
	    		response.setCharacterEncoding("UTF-8");
	    		response.setContentType("application/json; charset=utf-8");
	    		map.put("code", "667");
	    		map.put("msg", "接口失效");
	    		//发送响应数据
	    		response.getWriter().println(JSONObject.fromObject(map));
	    		return false;
	        }
	        return true;
		}
		//设置响应数据
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		map.put("code", "667");
		map.put("msg", "接口失效");
		//发送响应数据
		response.getWriter().println(JSONObject.fromObject(map));
		return false;
	}
	
	
	public static String filter(String str) {
	    String regEx = "[`~!$%^&()\\-+={}':;,\"\'\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s]";
	    Pattern p = Pattern.compile(regEx);
	    Matcher m = p.matcher(str);
	    return m.replaceAll("").trim();
	}
	
}

       