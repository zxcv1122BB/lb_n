package com.lb.activity.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.activity.model.TurntablePrize;
import com.lb.activity.service.ITurntablePrizeService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/turntablePrize")
public class TurntablePrizeController extends BaseController{
	
	@Autowired
	private ITurntablePrizeService turntablePrizeService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryTurntablePrizeList")
	public ModelAndView queryTurntablePrizeList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<TurntablePrize> list=turntablePrizeService.queryTurntablePrizeList(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<TurntablePrize> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/addTurntablePrize")
	public ModelAndView addTurntablePrize(HttpServletRequest request){
		TurntablePrize turntablePrize = BaseController.jsonToBean(request, TurntablePrize.class);
		Message message = turntablePrizeService.addTurntablePrize(request,turntablePrize);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryTurntablePrizeById")
	public ModelAndView queryTurntablePrizeById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer id = Integer.valueOf(map.get("id").toString());
		TurntablePrize turntablePrize=turntablePrizeService.queryTurntablePrizeById(id);
		return ResponseUtils.jsonView(turntablePrize);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateTurntablePrize")
	public ModelAndView updateTurntablePrize(HttpServletRequest request) {
		TurntablePrize turntablePrize = BaseController.jsonToBean(request, TurntablePrize.class);
		Message message = turntablePrizeService.updateTurntablePrize(request,turntablePrize);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteTurntablePrize")
	public ModelAndView deleteTurntablePrize(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer id = Integer.valueOf(map.get("id").toString());
		Message message = turntablePrizeService.deleteTurntablePrize(request,id);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/isStartTurntablePrize")
	public ModelAndView isStartTurntablePrize(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer id = Integer.valueOf(map.get("id").toString());
		Byte state =Byte.valueOf(String.valueOf(map.get("state")));
		Message message =turntablePrizeService.isStartTurntablePrize(request,id,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}
