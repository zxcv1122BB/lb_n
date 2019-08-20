package com.lb.activity.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.activity.model.AppDownloadGiving;
import com.lb.activity.service.IAppDownloadGivingService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/appDownloadGiving")
public class AppDownloadGivingController extends BaseController{
	
	@Autowired
	private IAppDownloadGivingService appDownloadGivingService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryAppDownloadGivingList")
	public ModelAndView queryAppDownloadGivingList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list=appDownloadGivingService.queryAppDownloadGivingList(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/addAppDownloadGiving")
	public ModelAndView addAppDownloadGiving(HttpServletRequest request){
		AppDownloadGiving appDownloadGiving = BaseController.jsonToBean(request, AppDownloadGiving.class);
		Message message = appDownloadGivingService.addAppDownloadGiving(request,appDownloadGiving);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryAppDownloadGivingById")
	public ModelAndView queryAppDownloadGivingById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer id = Integer.valueOf(map.get("id").toString());
		Map<String, Object> appDownloadGiving=appDownloadGivingService.queryAppDownloadGivingById(id);
		return ResponseUtils.jsonView(appDownloadGiving);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateAppDownloadGiving")
	public ModelAndView updateAppDownloadGiving(HttpServletRequest request) {
		AppDownloadGiving appDownloadGiving = BaseController.jsonToBean(request, AppDownloadGiving.class);
		Message message = appDownloadGivingService.updateAppDownloadGiving(request,appDownloadGiving);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteAppDownloadGiving")
	public ModelAndView deleteAppDownloadGiving(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer id = Integer.valueOf(map.get("id").toString());
		Message message = appDownloadGivingService.deleteAppDownloadGiving(request,id);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/isStartAppDownloadGiving")
	public ModelAndView isStartAppDownloadGiving(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer id = Integer.valueOf(map.get("id").toString());
		Byte state =Byte.valueOf(String.valueOf(map.get("state")));
		Message message =appDownloadGivingService.isStartAppDownloadGiving(request,id,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}
