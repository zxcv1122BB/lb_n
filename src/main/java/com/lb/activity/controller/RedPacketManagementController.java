package com.lb.activity.controller;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.activity.model.RedPacketManagement;
import com.lb.activity.service.IRedPacketManagementService;
import com.lb.sys.pojo.RedPacketPojo;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/redPacketManagement")
public class RedPacketManagementController extends BaseController{
	@Autowired
	private IRedPacketManagementService redPacketManagementService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryRedPacketManagementList")
	public ModelAndView queryRedPacketManagementList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list=redPacketManagementService.queryAllRedPacketManagement();
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/addRedPacketManagement")
	public ModelAndView addRedPacketManagement(HttpServletRequest request) throws ParseException{
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map.get("redPacketMoney")!=null&&map.get("redPacketNum")!=null&&map.get("moneyMin")!=null) {
			if(new BigDecimal(map.get("redPacketMoney").toString()).compareTo(new BigDecimal(map.get("redPacketNum").toString()).multiply(new BigDecimal(map.get("moneyMin").toString())))<0) {
				return ResponseUtils.jsonView(310,"红包金额设置错误",null);
			}
			Message message = redPacketManagementService.addRedPacketManagement(request,map);
			return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		}
		return ResponseUtils.jsonView(310,"请补全参数",null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryRedPacketManagementById")
	public ModelAndView queryRedPacketManagementById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer redPacketId = Integer.valueOf(map.get("redPacketId").toString());
		RedPacketManagement redPacket=redPacketManagementService.queryRedPacketManagementById(redPacketId);
		return ResponseUtils.jsonView(redPacket);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateRedPacketManagement")
	public ModelAndView updateRedPacketManagement(HttpServletRequest request) {
		RedPacketManagement redPacket = BaseController.jsonToBean(request, RedPacketManagement.class);
		Message message= redPacketManagementService.updateRedPacketManagement(request,redPacket);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteRedPacketManagement")
	public ModelAndView deleteRedPacketManagement(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer redPacketId = Integer.valueOf(map.get("redPacketId").toString());
		Message message= redPacketManagementService.deleteRedPacketManagement(request,redPacketId);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/isStartRedPacketManagement")
	public ModelAndView isStartRedPacketManagement(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer redPacketId = Integer.valueOf(map.get("redPacketId").toString());
		Byte state =Byte.valueOf(String.valueOf(map.get("state")));
		Message message= redPacketManagementService.isStartRedPacketManagement(request,redPacketId,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST ,value="/isExistRedPacket")
	public ModelAndView isExistRedPacket(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		List<RedPacketPojo> list= redPacketManagementService.isExistRedPacket(map);
		//如果list为空,则返回""作为前端判断
		if(list==null) {
			return ResponseUtils.jsonView("");
		}
		return ResponseUtils.jsonView(list);
	}
	@RequestMapping(method=RequestMethod.POST ,value="/fetchRedPacket")
	public ModelAndView fetchRedPacket(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message= redPacketManagementService.fetchRedPacket(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),message.getObject());
	}
}
