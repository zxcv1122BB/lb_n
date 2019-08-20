 package com.lb.member.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.IBetsumLogService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/betsumLog")
public class BetsumLogController extends BaseController{
	@Autowired
	private IBetsumLogService betsumLogService;
	//查询打码量变动记录
	@RequestMapping(method=RequestMethod.GET,value="/queryBetsumLogList")
	public ModelAndView queryBetsumLogList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> list=betsumLogService.queryBetsumLogList(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(200,"请求成功",pageInfo);
	}
}
