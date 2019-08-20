package com.lb.member.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.IPBlacklistService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
/**
 * jh IP黑名单,避免频繁访问
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/IPBlacklist")
public class IPBlacklistController extends BaseController{ 
	
	@Autowired
	private IPBlacklistService iPBlacklistService;
	
	@RequestMapping(method=RequestMethod.GET,value="/getIpBlacklist")
	public ModelAndView getIpBlacklist(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> list=iPBlacklistService.selectBlackIpBystatusAndIP(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	} 
	
	/**
	 * 添加ip黑名单
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/addIpBlacklist")
	public ModelAndView addIpBlacklist(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = iPBlacklistService.insertIPBlacklist(map);
		return result>0?ResponseUtils.jsonView(200,"添加成功"):ResponseUtils.jsonView(711,"添加失败");
	}
	
	/**
	 * 修改ip黑名单
	 * @param request
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST,value="/updateIpBlacklist")
	public ModelAndView updateIpBlacklist(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		map.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Integer result = iPBlacklistService.updateIpBlackList(map);
		return result>0?ResponseUtils.jsonView(200,"修改成功"):ResponseUtils.jsonView(712,"修改失败");
	} 
	
	@RequestMapping(method=RequestMethod.POST,value="/deleteBlacklist")
	public ModelAndView deleteBlacklist(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = iPBlacklistService.deleteBlacklistByid(Integer.valueOf(map.get("id").toString()));
		return result>0?ResponseUtils.jsonView(200,"删除成功"):ResponseUtils.jsonView(713,"删除失败");
	} 
	@RequestMapping(method=RequestMethod.POST,value="/isExistIp")
	public ModelAndView isExistIp(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer result = iPBlacklistService.isExistIp(map);
		return result<=0?ResponseUtils.jsonView(200,"ip可添加"):ResponseUtils.jsonView(713,"此ip存在");
	} 

}
