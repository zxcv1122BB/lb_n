package com.lb.sys.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysRole;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysRoleService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;

@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController{
	@Autowired
	private ISysRoleService sysRoleService;

	@RequestMapping(method=RequestMethod.GET,value="/querySysRoleList")
	public ModelAndView querySysRoleList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getRoleId() == null) {
			return ResponseUtils.jsonView(333,"权限异常");
		}
		map.put("adminRoleId", user.getRoleId());
		List<SysRole> selectByExample = sysRoleService.queryRealyRole(map);
		PageUtils<SysRole> pageInfo =  new PageUtils<>(pageIndex,pageNum,selectByExample);
		return ResponseUtils.jsonView(pageInfo);
	}
	@RequestMapping(method=RequestMethod.GET,value="/querySysRole")
	public ModelAndView querySysRole(HttpServletRequest request) {
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		if(user == null || user.getRoleId() == null) {
			return ResponseUtils.jsonView(333,"权限异常");
		}
		Map<String,Object> param = new HashMap<>();
		param.put("adminRoleId", user.getRoleId());
		List<SysRole> selectByExample = sysRoleService.queryRealyRole(param);
		return ResponseUtils.jsonView(selectByExample);
	}
	@RequestMapping(method=RequestMethod.POST,value="/addRole")
	public ModelAndView addRole(HttpServletRequest request) {
		SysRole role = BaseController.jsonToBean(request, SysRole.class);
		Message message= sysRoleService.insertSelective(request,role);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	
	@RequestMapping(method=RequestMethod.GET,value="/queryRoleById")
	public ModelAndView queryRoleById(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Long roleId =Long.parseLong(String.valueOf(map.get("roleId")));
		SysRole role=sysRoleService.selectByPrimaryKey(roleId);
		return ResponseUtils.jsonView(role);
	}
	@RequestMapping(method=RequestMethod.POST,value="/updateRole")
	public ModelAndView updateRole(HttpServletRequest request) {
		SysRole role = BaseController.jsonToBean(request, SysRole.class);
		Message message= sysRoleService.updateByPrimaryKeySelective(request,role);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	
	@RequestMapping(method=RequestMethod.POST,value="/deleteRole")
	public ModelAndView deleteRole(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Long roleId =Long.parseLong(String.valueOf(map.get("roleId")));
		Message message= sysRoleService.deleteByPrimaryKey(roleId);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/isStartRole")
	public ModelAndView isStartRole(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message= sysRoleService.isStartRole(request,map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.GET,value="/queryRoleByRoleName")
	public ModelAndView queryRoleByRoleName(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		String roleName = String.valueOf(map.get("roleName"));
		Message message=sysRoleService.queryRoleByRoleName(roleName);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}

}
	

	

