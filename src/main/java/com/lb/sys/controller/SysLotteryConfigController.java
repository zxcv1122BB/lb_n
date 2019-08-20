package com.lb.sys.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysLotteryConfig;
import com.lb.sys.service.ISysLotteryConfigService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/sysLotteryConfig")
public class SysLotteryConfigController extends BaseController{
	@Autowired
	private ISysLotteryConfigService sysLotteryConfigService;
	
	
	@RequestMapping(method=RequestMethod.GET,value="/querySysLotteryConfigList")
	public ModelAndView querySysFbConfigureList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<SysLotteryConfig> menuList=sysLotteryConfigService.querySysLotteryConfigList(map);
		PageUtils<SysLotteryConfig> pageInfo =  new PageUtils<>(pageIndex,pageNum,menuList);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	//添加
	@RequestMapping(method=RequestMethod.POST,value="/add")
	public ModelAndView add(HttpServletRequest request) {
		SysLotteryConfig sysFbConfigure = BaseController.jsonToBean(request, SysLotteryConfig.class);
		Message message=sysLotteryConfigService.add(request,sysFbConfigure);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	
	//更新
	@RequestMapping(method=RequestMethod.POST,value="/update")
	public ModelAndView update(HttpServletRequest request) {
		SysLotteryConfig sysFbConfigure = BaseController.jsonToBean(request, SysLotteryConfig.class);
		Message message=sysLotteryConfigService.update(request,sysFbConfigure);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/delete")
	public ModelAndView delete(HttpServletRequest request) {
		Map<String, Object> map= BaseController.jsonToMap(request);
		Integer id =Integer.parseInt(String.valueOf(map.get("id")));
		Message message=sysLotteryConfigService.delete(id);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}
	

	

