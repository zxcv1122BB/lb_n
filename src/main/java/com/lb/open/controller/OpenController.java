package com.lb.open.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.tools.ResponseUtils;
import com.lsopen.open.DataTools;
import com.lsopen.open.OpenTools;


@RestController
@RequestMapping("/OpenManage")
public class OpenController {
   
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// 开奖
	@RequestMapping(method = RequestMethod.GET, value = "/Open")
	public  ModelAndView Open(HttpServletRequest request, HttpServletResponse response) {
		String openType = request.getParameter("openType");
		String openID = request.getParameter("openID");
		if(openType==null) openType = "";
		if(openID==null) openID = "";
		DataTools.setJdbcTemplate(jdbcTemplate);
		if(!"".equals(openType) && "".equals(openID))
		{
			return ResponseUtils.jsonView(201, "参数openID不能为空");
		}		
		String dealResult =  OpenTools.dealOpen(openType,openID);
		if(dealResult.indexOf("success")>-1)
		{
			return ResponseUtils.jsonView(200, "处理成功");
		}
		else if("HasNotEndMatch".equals(dealResult)) 
		{
			return ResponseUtils.jsonView(200, "当前投注记录存在未结束的比赛，不能进行开奖处理");
		}
		else if("NoBetRecord".equals(dealResult)) 
		{
			return ResponseUtils.jsonView(200, "不存在未开奖或可开奖处理的投注记录");
		}else if(dealResult.indexOf("error")>-1) 
		{
			return ResponseUtils.jsonView(202, "处理异常");
		}
		return ResponseUtils.jsonView(200, "处理成功");
	}
}
