package com.lb.rebate.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.rebate.service.impl.UserRebateServiceImpl;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping(value="/userRebate")
public class UserRebateController extends BaseController{
	@Autowired UserRebateServiceImpl userRebateImpl;
	@RequestMapping(method=RequestMethod.POST,value="/qryRebateLog")
	public ModelAndView qryRebateLog(HttpServletRequest request) {
		Map<String, Object> reqMap = BaseController.jsonToMap(request);		
		Integer pageIndex = Integer.valueOf(reqMap.get("pageIndex").toString());//当前页
		Integer pageNum = Integer.valueOf(reqMap.get("pageNum").toString());//每一页数据条数
		List<Map<String, Object>> rebateLogList = userRebateImpl.qryRebateLog();
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,rebateLogList);
		return rebateLogList != null && rebateLogList.size() > 0 ? 
				ResponseUtils.jsonView(200, "获取成功",pageInfo):ResponseUtils.jsonView(201, "暂无数据") ;
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/userRebateRollBack")
	public ModelAndView update(HttpServletRequest request) {		
		Map<String, Object> reqMap = BaseController.jsonToMap(request);		
		if(reqMap.get("batch_no") == null || reqMap.get("rebate_type") == null ) 
			return ResponseUtils.jsonView(133, "参数异常", "请检查参数：batch_no");
		String batch_no = reqMap.get("batch_no").toString();
		int type = Integer.valueOf(reqMap.get("rebate_type")+"");
		boolean flag = false;
		if(type==1) {
			flag = userRebateImpl.batchRollBack(batch_no);
		}else if(type==2){
			flag = userRebateImpl.batchBetRebate(batch_no);
		}		
		return flag ? ResponseUtils.jsonView(200, "操作成功", flag) 
				: ResponseUtils.jsonView(133, "操作失败", flag);
	}
}
