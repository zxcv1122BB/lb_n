package com.lb.record.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.record.service.OpenRecordService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

/**
 * 用于开奖记录管理的接口
 * 
 * @author ASUS
 *
 */
@RestController
@RequestMapping("/openRecord")
public class OpenRecordController {

	@Autowired
	private OpenRecordService openRecordService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/queryDigitType")
	public ModelAndView queryDigitType(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = openRecordService.queryDigitType();
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", list)
				: ResponseUtils.jsonView(201, "暂无数据", null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/queryDigitRecord")
	public ModelAndView queryDigitRecord(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		String openState = null;
		if(map.get("openState") != null && !"".equals(map.get("openState"))) {
			openState = map.get("openState").toString();
		}
		map.put("openState", openState);
		List<Map<String, Object>> list = openRecordService.queryDigitRecord(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", pageInfo)
				: ResponseUtils.jsonView(201, "暂无数据", null);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/queryOpenRecord")
	public ModelAndView queryOpenRecord(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		String openState = null;
		if(map.get("openState") != null && !"".equals(map.get("openState"))) {
			openState = map.get("openState").toString();
		}
		map.put("openState", openState);
		List<Map<String, Object>> list = openRecordService.queryOpenRecord(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", pageInfo)
				: ResponseUtils.jsonView(201, "暂无数据", null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/queryMatchType")
	public ModelAndView queryMatchType(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = openRecordService.queryMatchType();

		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", list)
				: ResponseUtils.jsonView(201, "暂无数据", null);
	}
}
