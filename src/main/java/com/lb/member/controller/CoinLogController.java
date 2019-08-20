package com.lb.member.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.ICoinLogService;
import com.lb.report.model.OperationAnalysis;
import com.lb.sys.dao.ProxyDistributionMapper;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/coinLog")
public class CoinLogController extends BaseController{
	@Autowired
	private ICoinLogService coinLogService;
	@Autowired
	private ProxyDistributionMapper proxyDistributionMapper;
	//账变记录
	@RequestMapping(method=RequestMethod.GET,value="/queryCoinLogList")
	public ModelAndView queryCoinLogList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> list=coinLogService.queryCoinLogList(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(200,"请求成功",pageInfo);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/getCoinOperateType")
	public ModelAndView getCoinOperateType(HttpServletRequest request) {
		List<Map<String, Object>> list=coinLogService.getCoinOperateType();
		return ResponseUtils.jsonView(200,"请求成功",list);
	}
	
	/**
	 * 存储分析
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET,value="/getDepositList")
	public ModelAndView getDepositList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		if (map.get("agentCount") != null&&!"".equals(map.get("agentCount"))) {
			int count = proxyDistributionMapper.isExsitAgency(map.get("agentCount").toString());
			if (count <= 0) {
				return ResponseUtils.jsonView(201, "不存在此代理");
			}
		}
		List<OperationAnalysis> list=coinLogService.getDepositList(map);
		PageUtils<OperationAnalysis> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		OperationAnalysis coinLogMap=coinLogService.getDepositListTotal(map);
		map.clear();
		map.put("pageInfo", pageInfo);
		map.put("coinLogMap", coinLogMap);
		return ResponseUtils.jsonView(200,"请求成功",map);
	}
}
