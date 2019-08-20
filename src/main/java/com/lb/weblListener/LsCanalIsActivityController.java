package com.lb.weblListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.service.IWebListenerSerivice;
import com.lb.sys.tools.HttpClientUtil;
import com.lb.sys.tools.JSONUtils;
import com.lb.sys.tools.ResponseUtils;


/**
 * 
 * @date:2017年11月20日下午4:35:46
 * @describe:监测lsCanal是否处于运行状态
 */
@RestController
public class LsCanalIsActivityController {
	private final Logger logger = LoggerFactory.getLogger(LsCanalIsActivityController.class);
	@Autowired
	private IWebListenerSerivice webListenerSerivice;

	/**
	 * 
	 *@describe:获取所有的项目和访问接口
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/webListener")
	public ModelAndView webListener(HttpServletRequest request) {
		Map<String,Map<String,Object>> map = new HashMap<>();
		Integer code = 201;
		List<Map<String, Object>> weblist = webListenerSerivice.selectWebTitleList();
		if(weblist!=null && weblist.size()>0) {
			for(int i = 0;i<weblist.size();i++) {
				Map<String,Object> webMap = weblist.get(i);
				String url = webMap.get("link_url")!=null?webMap.get("link_url").toString():"";
				String webName = webMap.get("web_name")!=null?webMap.get("web_name").toString():"";
				String descInfo = webMap.get("desc_info")!=null?webMap.get("desc_info").toString():"";
				url = url + "/health";
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("status", "DOWN");
				try {
					String result = HttpClientUtil.doGetT(url, null);
					if(StringUtils.isNotEmpty(result)) {
						paramMap = JSONUtils.jsonToMap(result);
					}
				} catch (Exception e) {
					logger.error("请求【"+descInfo+"】检测接口异常："+e.getMessage());
					
				}
				paramMap.put("desc", descInfo);
				map.put(webName, paramMap);
			}	
			code = 200;
		}else {
			logger.info("暂无相关项目加入健康检测");
			Map<String,Object> errMap = new HashMap<>();
			errMap.put("des", "暂无相关项目加入健康检测");
			map.put("error", errMap);
		}
		return ResponseUtils.jsonView(code,"请求成功",map);
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/selectWebTitleList")
	public ModelAndView selectWebTitleList(HttpServletRequest request) {
		List<Map<String, Object>> list = webListenerSerivice.selectWebTitleList();
		if(!list.isEmpty()) {
			return ResponseUtils.jsonView(list);
		}else {
			return ResponseUtils.jsonView(902,"获取失败");
		}
	}
	
}
