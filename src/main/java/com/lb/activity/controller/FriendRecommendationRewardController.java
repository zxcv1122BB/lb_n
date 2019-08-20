package com.lb.activity.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.activity.service.IFriendRecommendationRewardService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

@RestController
@RequestMapping("/friendRecommendationReward")
public class FriendRecommendationRewardController extends BaseController{
	@Autowired
	private IFriendRecommendationRewardService friendRecommendationRewardService;
	
	@RequestMapping(method=RequestMethod.GET,value="/queryFriendRecommendationRewardList")
	public ModelAndView queryFriendRecommendationRewardList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//1.后面执行的查询就是一个分页查询语句
		List<Map<String, Object>> list=friendRecommendationRewardService.queryFriendRecommendationRewardList(map);
		//2.创建PageInfo封装分页信息,最多显示pageSize页
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	
}
