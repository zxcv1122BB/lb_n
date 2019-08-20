package com.lb.game.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.game.service.impl.GameTypeServiceImpl;
import com.lb.sys.dao.DigitalGamePlayMapper;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;
@Controller
@RestController
@RequestMapping("/game")
public class DigitalGamePlayController  {
	@Autowired DigitalGamePlayMapper digital;
	@Autowired GameTypeServiceImpl gameType;
	@RequestMapping(method = RequestMethod.POST, value = "/getGameInfo")
	public ModelAndView getGameInfo(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = BaseController.jsonToMap(request);
		
		Integer id =  map.containsKey("id")?Integer.valueOf(map.get("id").toString()) : null;
		Integer act_id =  map.containsKey("act_id")?Integer.valueOf(map.get("act_id").toString()) : null;
		
		try {
			if(map.get("openno")!=null&&!"".equals(map.get("openno"))) {
				map.put("openno", map.get("openno").toString().trim().replace("-", ""));
			}
			// 调用查询方法
			List<Map<String, Object>> all = new ArrayList<>();
			
			if(!StringUtil.isBlank(map.get("pageIndex")) && !StringUtil.isBlank(map.get("pageNum")) && !StringUtil.isBlank(map.get("pageSize"))) {
				// 当前页
				Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString()) ;
				// 每一页数据条数
				Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
				if (act_id==1) {				 
					all = digital.qryGameType();
				}else if (act_id==2) {
					all = digital.qryGameGroup(id);
				}
				else if (act_id==3) {
					all = digital.qryGamePlayed(id);
				}
				PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,all);
				return   ResponseUtils.jsonView(200, "获取数据成功",pageInfo);
			}else {
				if (act_id==1) {				 
					all = digital.qryGameType();
				}else if (act_id==2) {
					all = digital.qryGameGroup(id);
				}
				else if (act_id==3) {
					all = digital.qryGamePlayed(id);
				}
				return   ResponseUtils.jsonView(200, "获取数据成功",all);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.jsonView(201, "访问出现错误，请稍后再试",null) ;
		}
		
	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateGameInfo")
	public ModelAndView updateGameInfo(HttpServletRequest request) {
		
		Map<String, Object> paramMap = BaseController.jsonToMap(request);//this.jsonToMap(request);
		int act_id = Integer.valueOf(paramMap.get("act_id").toString());
		int id = Integer.valueOf(paramMap.get("id").toString());
		if(paramMap.containsKey("status")) {
			paramMap.put("status",String.valueOf(paramMap.get("status").toString()));
		}
		
		//System.err.println("[参数]:"+paramMap.entrySet());
		//Integer id =  paramMap.containsKey("id")?Integer.valueOf(paramMap.get("id").toString()):null;
		try {			
			int all = 0;
			if(act_id==0) {
				//all = digital.qryGameKind(id);
			}else if (act_id==1) {
				boolean batchCanada28Issue = false;
				if(41!=id) {
					/*基础配置数据，修改导致坍塌性Bug。故只开放
						id=41，加拿大28的起始期数据修改权限。
					*/
					paramMap.remove("current_issue");
					paramMap.remove("start_time");
					paramMap.remove("end_time");
					all = digital.updateGameType(paramMap);
				}else {
					List<Map<String, Object>> qryGameType = digital.qryGameType();
					boolean isUpdateIssue = !StringUtil.isBlank(paramMap.get("current_issue"))
							&& StringUtil.isBlank(paramMap.get("start_time"))
							&& !StringUtil.isBlank(paramMap.get("end_time"));
					if(isUpdateIssue)//需要重新生成
						batchCanada28Issue = gameType.batchCanada28Issue(qryGameType,paramMap);
					if(batchCanada28Issue || !isUpdateIssue)//更新成功或者不需要更新
						all = digital.updateGameType(paramMap);
				}
				
			}else if (act_id==2) {
				all = digital.updateGameGroup(paramMap);
			}
			else if (act_id==3) {
				if("0".equals(paramMap.get("status")+"")) {
					paramMap.put("status",2);
				}
				all = digital.updateGamePlayed(paramMap);
			}
			return   ResponseUtils.jsonView( all == 1 ? 200 : 133, all == 1 ? "更新成功":"更新失败");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseUtils.jsonView(201, "更新失败，请稍后再试",e.getMessage()) ;
		}
	}
	
	
}
