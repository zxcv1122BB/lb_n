/**
 * 
 */
package com.lb.sign.controller;

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

import com.alibaba.fastjson.JSONObject;
import com.lb.redis.JedisClient;
import com.lb.sign.service.SignInfoManageService;
import com.lb.sys.controller.LoginController;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;

/**
 * @describe 签到活动策略管理
 */
@RestController
@RequestMapping("/sign")
public class SignInfoManageController extends BaseController {

	private final static Log LOGGER = LogFactory.getLog(SignInfoManageController.class);
	
	@Autowired private SignInfoManageService signInfoManageService;
	
	@Autowired private JedisClient jedisClient;
	
	private final static String SIGN_REDIS_KEY = "SIGN_INFO";
	/**
	 * 获取签到策略信息列表
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/querySignInfoList")
	public ModelAndView querySignInfoList(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null) {
			return ResponseUtils.jsonView(333, "未登录管理员账号");
		}
		map.put("uid", user.getUserId());
		map.put("username", user.getUserName());
		map.put("roleId", user.getRoleId());
		List<Map<String,Object>> list = signInfoManageService.querySignInfoList(map);
		PageUtils<Map<String,Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	
	/**
	 * 增加签到策略信息
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/addSignInfo")
	public ModelAndView addSignInfo(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		
		if(map != null && map.size() > 0) {
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			map.put("createBy", user.getUserName());
			int result = signInfoManageService.addSignInfo(map);
			if(result > 0) {
				return ResponseUtils.jsonView(200,"增加成功");
			}
		}
		return ResponseUtils.jsonView(555,"增加失败");
	}
	
	/**
	 * 修改签到策略信息
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/updateSignInfo")
	public ModelAndView updateSignInfo(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		if(map != null && !StringUtil.isBlank(map.get("signId"))) {
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			map.put("updateBy", user.getUserName());
			map.put("roleId", user.getRoleId());
			int flag = 0;
			if(!StringUtil.isBlank(map.get("status")) 
					&& "1".equals(map.get("status").toString())) {
				flag = signInfoManageService.getStartedSignCount(map);
			}
			if(flag > 0) {
				return ResponseUtils.jsonView(555,"该时间范围已有签到活动启动了");
			}
			int result = signInfoManageService.updateSignInfo(map);
			if(result > 0) {
				if(!StringUtil.isBlank(map.get("status"))) {
					if("1".equals(map.get("status").toString())) {
						List<Map<String,Object>> list = signInfoManageService.querySignInfoList(map);
						if(list != null) {
							for(Map<String,Object> listMap: list) {
								if(listMap != null && !StringUtil.isBlank(listMap.get("id"))) {
									jedisClient.hset(SIGN_REDIS_KEY, listMap.get("id").toString(), JSONObject.toJSONString(listMap));
								}
							}
						}
					}else if("0".equals(map.get("status").toString())) {
						jedisClient.hdel(SIGN_REDIS_KEY, map.get("signId")+"");
					}
				}
				return ResponseUtils.jsonView(200,"修改成功");
			}
		}
		LOGGER.info("修改签到策略失败--->paramMap:"+map);
		return ResponseUtils.jsonView(555,"修改失败");
	}
	
	/**
	 * 删除签到策略信息
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/removeSignInfo")
	public ModelAndView removeSignInfo(HttpServletRequest request) {
		Map<String, Object> map = this.jsonToMap(request);
		
		if(map != null && !StringUtil.isBlank(map.get("signId"))) {
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			map.put("updateBy", user.getUserId());
			int result = 0;
			jedisClient.hdel(SIGN_REDIS_KEY, map.get("signId")+"");
			if(user.getRoleId() != null && user.getRoleId() == 1) {
				result = signInfoManageService.realyRemoveSignInfo(map);
			}else {
				result = signInfoManageService.removeSignInfo(map);
			}
			if(result > 0) {
				return ResponseUtils.jsonView(200,"删除成功");
			}
		}
		return ResponseUtils.jsonView(555,"删除失败");
	}
	/**
	 * 获取某个签到策略下的用户签到信息
	 * */
	@SuppressWarnings("static-access")
	@RequestMapping(method=RequestMethod.POST,value="/getUserSignList")
	public ModelAndView getUserSignList(HttpServletRequest request) {
		Map<String, Object> resultMap = null;
		Map<String, Object> map = this.jsonToMap(request);
		
		if(map != null && map.size() > 0) {
			SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
			map.put("updateBy", user.getUserId());
			if(user.getRoleId() != null && user.getRoleId() == 1) {
				resultMap = signInfoManageService.getUserSignList(map);
			}
		}
		return resultMap!=null?ResponseUtils.jsonView(200,"获取成功",resultMap):ResponseUtils.jsonView(201,"暂无签到数据");
	}
}
