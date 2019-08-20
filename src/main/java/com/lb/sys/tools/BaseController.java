package com.lb.sys.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import net.sf.json.JSONObject;


public class BaseController {
	
	private final static Log LOGGER = LogFactory.getLog(BaseController.class);
	
	/**
	 * 把浏览器参数转化放到Map集合中
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, Object> getParam(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        String method = request.getMethod();
        Enumeration<?> keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if(key!=null){
            	if (key instanceof String) {
            	    String value = request.getParameter(key.toString());
            	    if("GET".equals(method)){//前台encodeURIComponent('我们');转码后到后台还是ISO-8859-1，所以还需要转码
            	         try {
							 value =new String(value.getBytes(),"UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}    
            	    }
            		paramMap.put(key.toString(), value);
				}
            } 
        }
        return paramMap;
    }

	protected void flushResponse(HttpServletResponse response, String responseContent) {
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding("utf-8");
			// 针对ajax中页面编码为GBK的情况，一定要加上以下两句
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html;charset=UTF-8");
			writer = response.getWriter();
			if (responseContent==null || "".equals(responseContent) || "null".equals(responseContent)) {
				writer.write("");
			} else {
				writer.write(responseContent);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
	
	
	/**
	 * 返回列表分页的下角页码
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getPageNumberInfo(int total,int startIndex,int pageSize ,ModelAndView result) {
		//Math.ceil整数则为该整数，Math.ceil小数则为靠近大的整数
        int current =  (int) Math.ceil((startIndex + 1.0) / pageSize);
		result.addObject("start", startIndex);
		result.addObject("limit", pageSize);
		result.addObject("total", total);
		result.addObject("current", current);
		if(total > 0) {
			int page =  (int) Math.ceil(total/ pageSize);
			double totald= total;
			if(totald/pageSize>total/pageSize){
				page =page +1;
			}
			result.addObject("page", page);
			int startPage = 0;
			int endPage = 0;
			if (page<8) {
				startPage = 1;
				endPage = page;
			} else {
				if (current<5) {
					startPage = 1;
					endPage = page>6?6:page; 
				} else if (page-current<6) {
					startPage = page-5;
					endPage = page;
				} else {
					startPage = current - 2;
					endPage = current + 2;
				}
			}
			result.addObject("startPage", startPage);
			result.addObject("endPage", endPage);
		} else {
			result.addObject("page", 0);
		}
		return result;
	}
	
	
	
	/**
	 * 获取登录模块session对象
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	protected  HttpSession getSession(){ 
		HttpSession session =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(); 
		String id=session.getId();
	 
		//从登录模块获取session信息
		if(session != null && session.getServletContext().getContext("/logon") != null 
				&& session.getServletContext().getContext("/logon").getAttribute("sessionId_" + session.getId()) != null) {
			 
			String sessionId = session.getId();
			 
			session = (HttpSession) session.getServletContext().getContext("/logon").getAttribute("sessionId_" + sessionId);
		}
		
		return session;
	}
	
	/**
	 * 获取登录模块session中的sessionInfo
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected  Map<String, Object> getSessionInfo(){ 
		HttpSession session = this.getSession(); 
		Map<String, Object> sMap = null;
		try {
			if(session.getAttribute("sessionInfo") != null) {
				 sMap = (Map<String, Object>) session.getAttribute("sessionInfo");
			 }
		} catch (Exception e) {
			 e.printStackTrace();
		}
		return sMap;
	}
	
	/**
	 * json转bean对象
	 * RSA_data
	 */ 
	@SuppressWarnings({ "unchecked", "unused" })
	public static <T> T jsonToBean(HttpServletRequest request, Class<T> beanCalss) {  
		Map<String,Object> map = getParam(request);
		map.remove("sign");
		map.remove("timeStamp");
		LOGGER.info("jsonToBean::"+map);
		T bean =null;
		if(map!=null) {
			JSONObject jsonObject = JSONObject.fromObject(map);  
			bean = (T) JSONObject.toBean(jsonObject, beanCalss); 
		}else {
			LOGGER.error("请求无参数,无法转成map");
		}
        return bean;
	} 
	
	/**
	 * json转mMap对象
	 * @param request
	 * @return
	 */
	public static Map<String,Object> jsonToMap(HttpServletRequest request){
		Map<String,Object> map = getParam(request);
		map.remove("sign");
		map.remove("timeStamp");
        return map;
	}
	 @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getParameterMap(HttpServletRequest request) {
			// 参数Map
			Map properties = request.getParameterMap();
			// 返回值Map
			Map returnMap = new HashMap();
			Iterator entries = properties.entrySet().iterator();
			Map.Entry entry;
			String name = "";
			String value = "";
			while (entries.hasNext()) {
				entry = (Map.Entry) entries.next();
				name = String.valueOf(entry.getKey());
				Object valueObj = entry.getValue();
				if(null == valueObj){
					value = "";
				}else if(valueObj instanceof String[]){
					String[] values = (String[])valueObj;
					for(int i=0;i<values.length;i++){
						value = values[i] + ",";
					}
					value = value.substring(0, value.length()-1);
				}else{
					value = valueObj.toString();
				}
				returnMap.put(name, value);
			}
			return returnMap;
		}
	 /**
	  * 获取真实ip
	  */
	 public static String getRemoteIP(HttpServletRequest request){
		String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        	LOGGER.error("IPUtils ERROR ", e);
        } 
        return ip; /* 返回用户真实 IP, 如为多个 IP 时, 则取第一个 */
	 }
}
