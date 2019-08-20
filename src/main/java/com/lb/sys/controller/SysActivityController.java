package com.lb.sys.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysActivity;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysActivityService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

/**
 * 优惠活动API
 *
 */
@RestController
@RequestMapping(value="/sysActivity")
public class SysActivityController extends BaseController{
	
	private final Log LOGGER = LogFactory.getLog(SysActivityController.class);

	@Autowired
	private ISysActivityService sysActivityService;
	
	//添加优惠活动
	@RequestMapping(method=RequestMethod.POST,value="/addSysActivity")
	public ModelAndView addSysActivity(HttpServletRequest request) {
		Map<String, Object> paramMap = BaseController.jsonToMap(request);
		SysActivity sysActivity = new SysActivity();
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		sysActivity.setCreateUser(user.getUserName());
		sysActivity.setTitle(String.valueOf(paramMap.get("title")));
		sysActivity.setTitleUrl(String.valueOf(paramMap.get("titleUrl")));
		sysActivity.setContentUrl(String.valueOf(paramMap.get("contentUrl")));
		sysActivity.setStartTime(DateUtils.StringToDate(String.valueOf(paramMap.get("startTime"))));
		sysActivity.setEndTime(DateUtils.StringToDate(String.valueOf(paramMap.get("endTime"))));
		int flag = sysActivityService.insertSelective(sysActivity);
		return flag>0?ResponseUtils.jsonView(200,"添加成功  "):ResponseUtils.jsonView(260,"添加失败");
	}
	
	//根据主键id查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysActivityById")
	public ModelAndView querySysActivityById(HttpServletRequest request) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String sysActivityId = String.valueOf(jsonToMap.get("id"));
		if(sysActivityId!=null&&!"".equals(sysActivityId)) {
			SysActivity sysActivity = sysActivityService.selectByPrimaryKey(Long.parseLong(sysActivityId));
			return (sysActivity==null)?ResponseUtils.jsonView(263,"查无此数据"):ResponseUtils.jsonView(sysActivity);
		}else {
			LOGGER.error("没有查询的主键");
			return ResponseUtils.jsonView(263,"无查询主键");
		}
	}
	
	//更新优惠活动图片
	@RequestMapping(method=RequestMethod.POST,value="/updateSysActivity")
	public ModelAndView updateSysActivity(HttpServletRequest request) {
		Map<String, Object> paramMap = BaseController.jsonToMap(request);
		if(paramMap!=null) {
			SysActivity sysActivity = new SysActivity();
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			sysActivity.setUpdateUser(user.getUserName());
			sysActivity.setTitle(String.valueOf(paramMap.get("title")));
			sysActivity.setTitleUrl(String.valueOf(paramMap.get("titleUrl")));
			sysActivity.setContentUrl(String.valueOf(paramMap.get("contentUrl")));
			sysActivity.setStartTime(DateUtils.StringToDate(String.valueOf(paramMap.get("startTime"))));
			sysActivity.setEndTime(DateUtils.StringToDate(String.valueOf(paramMap.get("endTime"))));
			sysActivity.setId(Long.valueOf(paramMap.get("id").toString()));
			int resultInt = sysActivityService.updateByPrimaryKeySelective(sysActivity);
			return resultInt>0?ResponseUtils.jsonView(200,"更新成功"):ResponseUtils.jsonView(261,"优惠活动更新失败");
		}else {
			LOGGER.error("没有可更新数据");
			return ResponseUtils.jsonView(261,"优惠活动更新失败");
		}
	}
	
	//根据id逻辑删除
	@RequestMapping(method=RequestMethod.POST,value="/deleteSysActivityById")
	public ModelAndView deleteSysActivityById(HttpServletRequest request) {
		Map<String, Object> jsonToMap = BaseController.jsonToMap(request);
		String sysActivityId = String.valueOf(jsonToMap.get("id"));
		if(sysActivityId!=null&&!"".equals(sysActivityId)) {
			int resultInt = sysActivityService.deleteByPrimaryKey(Long.parseLong(sysActivityId));
			return resultInt>0?ResponseUtils.jsonView(200,"删除成功"):ResponseUtils.jsonView(262,"删除失败");
		}else {
			LOGGER.error("没有删除的主键");
			return ResponseUtils.jsonView(262,"删除失败");
		}
	}
	
	//分页查询
	@RequestMapping(method=RequestMethod.GET,value="/querySysActivityList")
	public ModelAndView querySysActivityList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<SysActivity> sysActivityList = sysActivityService.selectSysActivityList(map);
		PageUtils<SysActivity> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysActivityList);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	//批量删除batchSysActivityByid
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "batchSysActivityByid", method = RequestMethod.POST)
	public ModelAndView batchSysActivityByid(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map!=null && map.size()>0) {
			 List<String> ids = JSONArray.toList((JSONArray) map.get("sysActivityids"),String.class, new JsonConfig());
			int flag = sysActivityService.batchSysActivityByid(ids);
			return flag>0?ResponseUtils.jsonView(200,"批量删除成功"):ResponseUtils.jsonView(263,"批量删除失败");
		}else {
			LOGGER.error("无公告信息批量删除id");
			return ResponseUtils.jsonView(263,"批量删除失败",null);
		}
	}
	
}
