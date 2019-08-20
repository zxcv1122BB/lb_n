package com.lb.activity.controller;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.activity.model.BigTurntable;
import com.lb.activity.model.TurntablePrize;
import com.lb.activity.service.IBigTurntableService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/bigTurntable")
public class BigTurntableController extends BaseController{
	@Autowired
	private IBigTurntableService bigTurntableService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryBigTurntableList")
	public ModelAndView queryBigTurntableList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list=bigTurntableService.queryBigTurntableList();
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryAllBigTurntable")
	public ModelAndView queryAllBigTurntable(HttpServletRequest request) {
		List<Map<String, Object>> list=bigTurntableService.queryBigTurntableList();
		return ResponseUtils.jsonView(list);
	}
	@RequestMapping(method=RequestMethod.POST,value="/addBigTurntable")
	public ModelAndView addBigTurntable(HttpServletRequest request) throws ParseException{
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map.get("vipId")!=null) {
			long count=bigTurntableService.queryBigTurntableVip(Integer.valueOf(map.get("vipId").toString()));
			if(count>0) {
				return ResponseUtils.jsonView(323,"存在此Vip等级的大转盘");
			}
		}else {
			return ResponseUtils.jsonView(323,"请输入Vip等级");
		}
		Message message = bigTurntableService.addBigTurntable(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryBigTurntableById")
	public ModelAndView queryBigTurntableById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer bid = Integer.valueOf(map.get("bid").toString());
		Map<String, Object> hs=bigTurntableService.queryBigTurntableById(bid);
		return ResponseUtils.jsonView(hs);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateBigTurntable")
	public ModelAndView updateBigTurntable(HttpServletRequest request) {
		BigTurntable bigTurntable = BaseController.jsonToBean(request, BigTurntable.class);
		Message message = bigTurntableService.updateBigTurntable(request,bigTurntable);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteBigTurntable")
	public ModelAndView deleteBigTurntable(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer bid = Integer.valueOf(map.get("bid").toString());
		Message message = bigTurntableService.deleteBigTurntable(request,bid);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/isStartBigTurntable")
	public ModelAndView isStartBigTurntable(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer bid = Integer.valueOf(map.get("bid").toString());
		Byte state =Byte.valueOf(String.valueOf(map.get("state")));
		Message message =bigTurntableService.isStartBigTurntable(request,bid,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST ,value="/isExistBigTurntable")
	public ModelAndView isExistBigTurntable(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		List<TurntablePrize> list= bigTurntableService.isExistBigTurntable(map);
		//如果list为空,则返回""作为前端判断
		if(list==null) {
			return ResponseUtils.jsonView("");
		}
		return ResponseUtils.jsonView(list);
	}
	@RequestMapping(method=RequestMethod.POST ,value="/getTurntablePrize")
	public ModelAndView getTurntablePrize(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message= bigTurntableService.getTurntablePrize(request,map);
		//如果list为空,则返回""作为前端判断
		if(message.getObject()==null) {
			return ResponseUtils.jsonView("");
		}
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),message.getObject());
	}
}
