package com.lb.member.controller;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.IBetsumLogService;
import com.lb.member.service.ICoinLogService;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/calculate")
public class CalculateConntroller extends BaseController {
	@Autowired
	private UserModelService userService;
	@Autowired
	private ICoinLogService coinLogService;
	@Autowired
	private IBetsumLogService betsumLogService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryUserByUserName")
	public ModelAndView queryUserByUserName(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String userName = String.valueOf(map.get("userName"));
		Map<String, Object> userModel=userService.queryUserBetsum(userName);
		return ResponseUtils.jsonView(200,"",userModel);
	}
	//会员加扣款操作
	@RequestMapping(method=RequestMethod.POST,value="/addAndSubtractMoney")
	public ModelAndView addAndSubtractMoney(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message=coinLogService.addAndSubtractMoney(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	//会员加减打码量
	@RequestMapping(method=RequestMethod.POST,value="/addAndSubtractCode")
	public ModelAndView addAndSubtractCode(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message=betsumLogService.addAndSubtractCode(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryProxyByUserName")
	public ModelAndView queryProxyByUserName(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String userName = String.valueOf(map.get("userName"));
		Map<String, Object> userModel=coinLogService.queryProxyByUserName(userName);
		return ResponseUtils.jsonView(200,"",userModel);
	}
	//代理加扣款操作
	@RequestMapping(method=RequestMethod.POST,value="/proxyAddAndSubtractMoney")
	public ModelAndView proxyAddAndSubtractMoney(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message=coinLogService.proxyAddAndSubtractMoney(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}
