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

import com.lb.activity.service.IPreferentialCardService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/preferentialCard")
public class PreferentialCardController extends BaseController{
	@Autowired
	private IPreferentialCardService preferentialCardService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryPreferentialCardList")
	public ModelAndView queryPreferentialCardList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list=preferentialCardService.queryPreferentialCard(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	//batch指批次
	//account指优惠卡账号
	//count指张数
	//money指单张面额
	//vips指使用范围（用户级别）
	@RequestMapping(method=RequestMethod.POST,value="/addPreferentialCard")
	public ModelAndView addPreferentialCard(HttpServletRequest request) throws ParseException{
		Map<String, Object> map =  BaseController.jsonToMap(request);
		if(map.get("money")!=null&&!"".equals(map.get("money").toString())
				&&new BigDecimal(map.get("money").toString()).compareTo(new BigDecimal(0))>0
				&&map.get("count")!=null&&!"".equals(map.get("count").toString())
				&&Integer.valueOf(map.get("count").toString())>0) {
			Message message = null;
			try {
				message = preferentialCardService.addPreferentialCard(request,map);
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseUtils.jsonView(310,"操作失败",null);
			}
			return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		}
		return ResponseUtils.jsonView(310,"请补全参数",null);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/updatePreferentialCard")
	public ModelAndView updatePreferentialCard(HttpServletRequest request) {
		Map<String, Object> redPacket = BaseController.jsonToMap(request);
		Message message=null;
		try {
			message= preferentialCardService.updatePreferentialCard(request,redPacket);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.jsonView(310,"操作失败",null);
		}
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
		
	}
	@RequestMapping(method=RequestMethod.POST,value="/deletePreferentialCard")
	public ModelAndView deletePreferentialCard(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer redPacketId =Integer.valueOf(map.get("id").toString());
		Message message= null;
		try {
			message= preferentialCardService.deletePreferentialCard(request,redPacketId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.jsonView(310,"操作失败",null);
		}
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}
