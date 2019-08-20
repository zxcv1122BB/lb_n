package com.lb.openprize.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.openprize.service.IOpenRandomNumService;
import com.lb.redis.JedisClient;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.GetPropertiesValue;
import com.lb.sys.tools.JSONUtils;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;

@RestController
@RequestMapping("/systemColor")
public class OpenRandomNumController {

	@Autowired
	private JedisClient jedis;
	@Autowired
	private IOpenRandomNumService openService;

	@RequestMapping(method = RequestMethod.POST, value = "/preinstallLuckNum")
	public ModelAndView preinstallLuckNum(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if (map != null && map.size() > 0 && !StringUtil.isBlank(map.get("issue"))
				&& !StringUtil.isBlank(map.get("oneTypeId")) && !StringUtil.isBlank(map.get("luckNumber"))) {
			String msg = openService.preinstallLuckNum(map);
			return ResponseUtils.jsonView(200, msg);
		} else {
			return ResponseUtils.jsonView(201, "参数错误");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/openSetLuckNum")
	public ModelAndView openSetLuckNum(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if (map != null && map.size() > 0 && !StringUtil.isBlank(map.get("issue"))
				&& !StringUtil.isBlank(map.get("oneTypeId")) && !StringUtil.isBlank(map.get("luckNumber"))) {
			String issue = map.get("issue").toString();
			String oneTypeId = map.get("oneTypeId").toString();
			String luckNumber = map.get("luckNumber").toString();
			String msg = openService.openSetLuckNum(issue, oneTypeId, luckNumber);
			return ResponseUtils.jsonView(200, msg);
		} else {
			return ResponseUtils.jsonView(201, "参数错误");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getSystemColorType")
	public ModelAndView getSystemColorType(HttpServletRequest request) {
		List<Map<String, String>> result = openService.getSystemColorType();
		return result != null && result.size() > 0 ? ResponseUtils.jsonView(200, "获取成功", result)
				: ResponseUtils.jsonView(201, "暂无数据");
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/querySystemColorNum")
	public ModelAndView querySystemColorNum(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		// 当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		// 每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		// 显示的页数
		//Integer pageSize = Integer.valueOf(map.get("pageSize").toString());
		List<Map<String, Object>> list = openService.querySystemColorNum(map);
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return list.size() > 0 ? ResponseUtils.jsonView(200, "获取数据成功", pageInfo)
				: ResponseUtils.jsonView(201, "暂无数据", null);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/updateSetLuckNum")
	public ModelAndView updateSetLuckNum(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String msg = openService.updateSetLuckNum(map);
		return ResponseUtils.jsonView(200, msg);
	}
	
	//查询某一彩种最新20条期号信息
	@RequestMapping(method = RequestMethod.GET, value = "/getTopTwentyNewIssues")
	public ModelAndView getTopTwentyNewIssues(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String id = map.get("oneTypeId")!=null?map.get("oneTypeId").toString():"";
		String num = map.get("num")!=null?map.get("num").toString():"0";
		if("".equals(id) || id==null) {
			return ResponseUtils.jsonView(201,"参数值不能为空");
		}
		Integer size = Integer.valueOf(num);
		List<String> newIssueList = new ArrayList<>();
		//某一彩种所有的期号
		Map<String, String> digitalIssues = jedis.hgetAll("LS_RedisKey_DigitalColor_Issue_" + id);
		//某一彩种最新一期
		Map<String, String> proMap = GetPropertiesValue.getMapValue("DigitalRedisKey");
		String 	key = proMap.get("hisNewestIssueOpenData");
		String json = jedis.hget(key, id);
		Map<String, Object> jsonMap = JSONUtils.jsonToMap(json);
		if(digitalIssues!=null && digitalIssues.size()>0 && jsonMap!=null && jsonMap.size()>0) {
			String newIssue = jsonMap.get("issue").toString();
			//筛选是否是最新的一期
			Map<String, Object> currentIssueMap = getNewestIssueInRedis(digitalIssues,newIssue);
			if(currentIssueMap!=null && currentIssueMap.size()>0) {
				newIssue = currentIssueMap.get("issue").toString();
			}
			List<String> issueList = new ArrayList<>();
			Set<String> keys = digitalIssues.keySet();
			issueList.addAll(keys);
			//排序
			Collections.sort(issueList);
			if(issueList.size()>size) {
				int position = issueList.indexOf(newIssue);
				newIssueList = issueList.subList(position, position+size);
			}
		}
		return newIssueList != null && newIssueList.size() > 0 ? ResponseUtils.jsonView(200, "获取成功", newIssueList)
				: ResponseUtils.jsonView(201, "暂无数据");
	}
	
	// 从redis中得到该玩法最新一期期号
	private Map<String, Object> getNewestIssueInRedis(Map<String, String> digitalIssues, String currentIssue) {
		String currentIssueInfo = digitalIssues.get(currentIssue);
		Map<String, Object> currentIssueMap = JSONUtils.jsonToMap(currentIssueInfo);
		String deadline = currentIssueMap.get("deadline").toString();
		String start_time = currentIssueMap.get("start_time").toString();
		String now = DateUtils.getDateString(new Date());
		boolean timeFlag = DateUtils.getCompareDate(start_time, deadline, "yyyy-MM-dd HH:mm:ss", now);
		// 判断当前时间是否在start_time与deadline之间 true则为最新一期
		if (timeFlag) {
			return currentIssueMap;
			// 当前时间第一次小于等于start_time 则是跨天的最新一期
		}else {
			String nextIssue = currentIssueMap.get("nextIssue").toString();
			if (StringUtil.isBlank(nextIssue)) {
				return new HashMap<>();
			} else {
				return getNewestIssueInRedis(digitalIssues, nextIssue);
			}
		}
	}
}
