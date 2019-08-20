/**
 * 
 */
package com.lb.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysRegisterConfigure;
import com.lb.sys.service.ISysRegisterConfigureService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;

/**
 * @author wz
 * @describe 系统会员注册及代理注册接口控制类
 * @date 2017年9月20日
 */
@RestController
@RequestMapping("/sys")
public class SysRegisterConfigureController extends BaseController{
	
	@Autowired
	private ISysRegisterConfigureService sysRegisterConfigureService;
	/**
	 * 查询会员注册及代理注册设置信息
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.GET,value="/queryRegisterConfigure")
	public ModelAndView queryRegisterConfigure(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		Integer type = Integer.valueOf(map.get("type").toString());
		List<SysRegisterConfigure> list = sysRegisterConfigureService.queryRegisterConfigure(type);
		return ResponseUtils.jsonView(list);
	}
	
	/**
	 * 修改会员注册及代理注册配置
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/updateMemberRegisterConfigure")
	public ModelAndView updateRegisterConfigure(HttpServletRequest request)
	{
		SysRegisterConfigure configure = this.jsonToBean(request, SysRegisterConfigure.class);
		int result = sysRegisterConfigureService.updateRegisterConfigure(configure);
		ModelAndView model = null;
		if(result > 0) {
			model =ResponseUtils.jsonView(200, "修改成功");
		}else {
			model =ResponseUtils.jsonView(555, "修改失败");
		}
		return model;
	}
	
}
