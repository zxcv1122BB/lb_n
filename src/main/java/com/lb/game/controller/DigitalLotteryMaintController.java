package com.lb.game.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.redis.JedisClient;
import com.lb.sys.service.IDigitalLotteryMaintService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.GetPropertiesValue;
import com.lb.sys.tools.JSONUtils;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

/**
 * @describe 数字彩开奖数据维护更新接口
 */

@RestController
@RequestMapping("/maint")
public class DigitalLotteryMaintController extends BaseController {

	@Autowired IDigitalLotteryMaintService iDigitalLotteryMaintService;
	@Autowired
	private JedisClient jedis;
	
	/**
	 * 获取某个数字彩的开奖信息
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/getNewDigitalLotteryList")
	public ModelAndView getNewDigitalLotteryList(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());	
		//1.后面执行的查询就是一个分页查询语句
		List<Map<String,Object>> list = iDigitalLotteryMaintService.getNewDigitalLotteryList(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);		
		return list != null && list.size() > 0 ? 
				ResponseUtils.jsonView(200, "获取成功",pageInfo)
				:
				ResponseUtils.jsonView(201, "暂无数据",pageInfo) ;
	}
	
	/**
	 * 获取数字彩的所有一级玩法以及重庆时时彩的所有期号(注：默认选中重庆时时彩)
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/getDigitalLotteryInfoList")
	public ModelAndView getDigitalLotteryInfoList(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		Map<String,Object> resultMap = iDigitalLotteryMaintService.getDigitalLotteryInfoList(map);
				
		return resultMap != null && resultMap.size() > 0 ? 
				ResponseUtils.jsonView(200, "获取成功",resultMap)
				:
				ResponseUtils.jsonView(201, "暂无数据",resultMap) ;
	}
	
	/**
	 * 获取某个数字彩的的所有期号
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/getDigitalLotteryIssueList")
	public ModelAndView getDigitalLotteryIssueList(HttpServletRequest request) {
		// 获取参数
		Map<String, Object> map = this.jsonToMap(request);
		if(map == null || map.get("typeId") == null) {
			return ResponseUtils.jsonView(333, "参数错误");
		}
		List<Map<String,Object>> list = iDigitalLotteryMaintService.getDigitalLotteryIssueList(map);
				
		return list != null && list.size() > 0 ? 
				ResponseUtils.jsonView(200, "获取成功",list)
				:
				ResponseUtils.jsonView(201, "暂无数据",list) ;
		
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/saveDigitalColor")
	public ModelAndView saveDigitalColor(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		if(!map.containsKey("one_type_id") || !map.containsKey("issue") 
				|| !map.containsKey("luck_number")) {
			return ResponseUtils.jsonView(201,"参数错误");	
		}
		if(StringUtil.isBlank(map.get("issue")) || StringUtil.isBlank(map.get("one_type_id"))
				|| StringUtil.isBlank(map.get("luck_number"))) {
			return ResponseUtils.jsonView(201,"参数值不能为空");
		}
		String issue = String.valueOf(map.get("issue"));
		String oneTypeId = String.valueOf(map.get("one_type_id"));
		//获取投注期或未开始期
		Map<String,Object> newBetIssueInfo = getNewBetIssueInfo(oneTypeId);
		if(newBetIssueInfo==null || newBetIssueInfo.size()==0) {
			return ResponseUtils.jsonView(201,"未知错误，请联系管理员");
		}
		String betIssue = String.valueOf(newBetIssueInfo.get("issue"));
		//判断新增的期号是否等于或大于投注期号
		if(issue.compareTo(betIssue)>=0) {
			return ResponseUtils.jsonView(201,"您所添加的期号当前正处于投注或还未开售");
		}
		map.put("id", issue+"_"+oneTypeId);
		Message message = iDigitalLotteryMaintService.saveOpenDataInfo(map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg()) ;
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping(method = RequestMethod.POST, value = "/updateDigitalColor")
	public ModelAndView updateDigitalColor(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		if(!map.containsKey("one_type_id") || !map.containsKey("issue") 
				|| !map.containsKey("luck_number")) {
			return ResponseUtils.jsonView(201,"参数错误") ;	
		}
		map.put("id", map.get("issue")+"_"+map.get("one_type_id"));
		Map<String,Object> oldOpenData = iDigitalLotteryMaintService.qryOpenDataById(map.get("issue")+"_"+map.get("one_type_id"));
		oldOpenData.put("luck_number", map.get("luck_number"));
		Message message = iDigitalLotteryMaintService.updateOpenDataInfo(oldOpenData);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg()) ;
	}
	
	
	private Map<String,Object> getNewBetIssueInfo(String oneTypeId){
		Map<String, Object> result = new HashMap<>();
		//某一彩种最新一期
		Map<String, String> proMap = GetPropertiesValue.getMapValue("DigitalRedisKey");
		String 	key = proMap.get("hisNewestIssueOpenData");
		String json = jedis.hget(key, oneTypeId);
		Map<String, Object> jsonMap = JSONUtils.jsonToMap(json);
		if(jsonMap!=null && jsonMap.size()>0) {
			String currentIssueInfo = jedis.hget("LS_RedisKey_DigitalColor_Issue",oneTypeId);
			if (!StringUtil.isBlank(currentIssueInfo)) {
				Map<String, Object> currentIssue = JSONUtils.jsonToMap(currentIssueInfo);
				String start_time = currentIssue.get("start_time").toString();
				String deadline = currentIssue.get("deadline").toString();
				if (DateUtils.getCompareDate(start_time, deadline, "yyyy-MM-dd HH:mm:ss",
						DateUtils.getDateString(new Date()))) {
					// 处于当前期的销售时间
					result = currentIssue;
					result.put("saleStatus", "ON_SALE");
				} else if (deadline.compareTo(DateUtils.getDateString(new Date())) < 0) {
					// 当前期的截止时间<当前时间
					Map<String, String> digitalIssues = jedis.hgetAll("LS_RedisKey_DigitalColor_Issue_" + oneTypeId);
					// redis中存在该期
					if (digitalIssues.containsKey(currentIssue.get("issue").toString())) {
						TreeMap<String, String> sortMap = new TreeMap<>(digitalIssues);
						Map<String, Object> newestIssue = getNewestIssueInRedis(sortMap);
						if (newestIssue != null && !newestIssue.isEmpty()) {
							result = newestIssue;
						}
					}
				}
				if (start_time.compareTo(DateUtils.getDateString(new Date())) > 0) {
					// 暂未开售的情况
					result = currentIssue;
				}
			} else {
				Map<String, String> digitalIssues = jedis.hgetAll("LS_RedisKey_DigitalColor_Issue_" + oneTypeId);
				if (digitalIssues != null && !digitalIssues.isEmpty()) {
					TreeMap<String, String> sortMap = new TreeMap<>(digitalIssues);
					Map<String, Object> newestIssueInRedis = getNewestIssueInRedis(sortMap);
					if (newestIssueInRedis != null && !newestIssueInRedis.isEmpty()) {
						result = newestIssueInRedis;
					}
				}
			}
		}
		return result;
	}
	
	private Map<String, Object> getNewestIssueInRedis(Map<String, String> digitalIssues) {
		Map<String, Object> result = new HashMap<>();
		for (String issueKey : digitalIssues.keySet()) {
			String currentIssueInfo = digitalIssues.get(issueKey);
			Map<String, Object> currentIssueMap = JSONUtils.jsonToMap(currentIssueInfo);
			String deadline = currentIssueMap.get("deadline").toString();
			String start_time = currentIssueMap.get("start_time").toString();
			String now = DateUtils.getDateString(new Date());
			boolean timeFlag = DateUtils.getCompareDate(start_time, deadline, "yyyy-MM-dd HH:mm:ss", now);
			// 判断当前时间是否在start_time与deadline之间 true则为最新一期
			if (timeFlag) {
				result = currentIssueMap;
				break;
			}
			// 当前时间第一次小于等于start_time 则是跨天的最新一期
			if (now.compareTo(start_time) <= 0) {
				currentIssueMap.put("saleStatus", "NO_SALE");
				result = currentIssueMap;
				break;
			}
		}
		return result;
	}
}
