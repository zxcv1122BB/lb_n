package com.lb.member.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.model.DepositGivingStrategy;
import com.lb.member.service.IDepositGivingStrategyService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/depositGivingStrategy")
public class DepositGivingStrategyController extends BaseController{
	@Autowired
	private IDepositGivingStrategyService depositGivingStrategyService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryDepositGivingStrategyList")
	public ModelAndView queryMemberDepositList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> list=depositGivingStrategyService.queryAllDepositGivingStrategy();
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/addDepositGivingStrategy")
	public ModelAndView addDepositGivingStrategy(HttpServletRequest request){
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message= depositGivingStrategyService.addDepositGivingStrategy(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryDepositGivingStrategyById")
	public ModelAndView queryDepositGivingStrategyById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer strategyId = Integer.valueOf(map.get("strategyId").toString());
		DepositGivingStrategy strategy=depositGivingStrategyService.queryDepositGivingStrategyById(strategyId);
		return ResponseUtils.jsonView(strategy);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateDepositGivingStrategy")
	public ModelAndView updateDepositGivingStrategy(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message= depositGivingStrategyService.updateDepositGivingStrategy(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteDepositGivingStrategy")
	public ModelAndView deleteDepositGivingStrategy(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer strategyId =  Integer.valueOf(map.get("strategyId").toString());
		Message message= depositGivingStrategyService.deleteDepositGivingStrategy(request,strategyId);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/isStartDepositGivingStrategy")
	public ModelAndView isStartDepositGivingStrategy(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer strategyId = Integer.valueOf(map.get("strategyId").toString());
		Byte state =Byte.valueOf(String.valueOf(map.get("state")));
		Message message= depositGivingStrategyService.isStartDepositGivingStrategy(request,strategyId,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/codeInformation")
	public ModelAndView codeInformation(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		List<Map<String, Object>> list=depositGivingStrategyService.codeInformation(map);
		return ResponseUtils.jsonView(list);
	}
}
