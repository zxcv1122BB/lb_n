package com.lb.member.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.member.service.IMemberWithdrawService;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/memberWithdraw")
public class MemberWithdrawController extends BaseController{
	@Autowired
	private IMemberWithdrawService memberWithdrawService;
	@RequestMapping(method=RequestMethod.GET,value="/queryMemberWithdrawList")
	public ModelAndView queryMemberWithdrawList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> list=memberWithdrawService.queryMemberWithdrawList(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,list);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.POST,value="/withdrawIsLock")
	public ModelAndView withdrawIsLock(HttpServletRequest request) {
		try {
			Map<String, Object> map = BaseController.jsonToMap(request);
			Message message=memberWithdrawService.withdrawIsLock(request,map);
			return ResponseUtils.jsonView(message.getCode(),message.getMsg());
		} catch (Exception e) {
			return ResponseUtils.jsonView(201,e.getMessage());
		}
	}
	@RequestMapping(method=RequestMethod.GET,value="/withdrawQuery")
	public ModelAndView withdrawQuery(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Map<String, Object>  message=memberWithdrawService.withdrawQuery(map);
		return ResponseUtils.jsonView(message);
	}
	@RequestMapping(method=RequestMethod.POST,value="/withdrawHandle")
	public ModelAndView withdrawHandle(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message=memberWithdrawService.withdrawHandle(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg());
	}
	@RequestMapping(method=RequestMethod.POST,value="/withdrawUserInfo")
	public ModelAndView withdrawUserInfo(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if("1".equals(map.get("userType"))||"2".equals(map.get("userType"))||map.get("userName")!=null) {
			Map<String, Object> userInfoMap=memberWithdrawService.withdrawUserInfo(map);
			return ResponseUtils.jsonView(200,"",userInfoMap);
		}
		return ResponseUtils.jsonView(233,"参数错误");
	}
	
	/**
	 * 管理员操作会员提款接口
	 * */
	@RequestMapping(method=RequestMethod.POST,value="/memberDrawingManage")
	public ModelAndView memberDrawingManage(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(StringUtil.isBlank(map.get("state")) || StringUtil.isBlank(map.get("id"))) {
			return ResponseUtils.jsonView(333,"参数错误");
		}
		//从session中获取当前操作人
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		map.put("userId", sysUser.getUserId());
		map.put("sysUserName", sysUser.getUserName());
		map.put("operateIp", request.getRemoteAddr());
		map.put("roleId",sysUser.getRoleId());
		Message result = null;
		try {
			result = memberWithdrawService.memberDrawingManage(map);
		} catch (Exception e) {
			e.printStackTrace();
			result = new Message(555, "处理失败");
		}
		return ResponseUtils.jsonView(result.getCode(),result.getMsg());
	}
}
