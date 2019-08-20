package com.lb.sys.controller;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.lb.sys.model.SysMenu;
import com.lb.sys.model.SysUser;
import com.lb.sys.service.ISysMenuService;
import com.lb.sys.tools.BaseController;
import com.lb.sys.tools.PageUtils;
import com.lb.sys.tools.ResponseUtils;
import com.lb.sys.tools.model.Message;
@RestController
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController {

	@Autowired
	private ISysMenuService sysMenuService;
	//查询所有的启用菜单
	@RequestMapping(method=RequestMethod.GET,value="/sysMenuSubList")
	public ModelAndView sysMenuSubList(HttpServletRequest request) {
		List<Map<String, Object>> sysMenuList = sysMenuService.queryAll();
		return ResponseUtils.jsonView(sysMenuList);
	}
	//添加菜单
	@RequestMapping(method=RequestMethod.POST,value="/addSysMenu")
	public ModelAndView addSysMenu(HttpServletRequest request) {
		SysMenu sysMenu = BaseController.jsonToBean(request, SysMenu.class);
		Message message=sysMenuService.insertSelective(request,sysMenu);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	//通过id查询菜单
	@RequestMapping(method=RequestMethod.GET,value="/querySysMenuById")
	public ModelAndView querySysMenuById(HttpServletRequest request) {
		Map<String, Object> map= BaseController.jsonToMap(request);
		Long menuId =Long.parseLong(String.valueOf(map.get("menuId")));
		SysMenu sysMenu=sysMenuService.selectByPrimaryKey(menuId);
		return ResponseUtils.jsonView(sysMenu);
	}
	//更新菜单
	@RequestMapping(method=RequestMethod.POST,value="/updateSysMenu")
	public ModelAndView updateSysMenu(HttpServletRequest request) {
		SysMenu sysMenu = BaseController.jsonToBean(request, SysMenu.class);
		Message message=sysMenuService.updateByPrimaryKeySelective(request,sysMenu);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/deleteSysMenu")
	public ModelAndView deleteSysMenu(HttpServletRequest request) {
		Map<String, Object> map= BaseController.jsonToMap(request);
		Long menuId =Long.parseLong(String.valueOf(map.get("menuId")));
		Message message=sysMenuService.deleteByPrimaryKey(menuId);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	//验证是否存在该菜单
	@RequestMapping(method=RequestMethod.GET,value="/queryByMenuName")
	public ModelAndView queryByMenuName(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Message message=sysMenuService.queryByMenuName(map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
 
	@RequestMapping(method=RequestMethod.GET,value="/queryAllSysMenu")
	public ModelAndView queryAllSysMenu(HttpServletRequest request) {
		List<SysMenu> selectByExample = sysMenuService.querySysMenuList();
		return ResponseUtils.jsonView(selectByExample);
	}
	//模糊查询
	@RequestMapping(method=RequestMethod.GET,value="/queryLike")
	public ModelAndView queryLike(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		List<Map<String, Object>> sysMenuList = sysMenuService.queryLike(map);
		PageUtils<Map<String, Object>> pageInfo =  new PageUtils<>(pageIndex,pageNum,sysMenuList);
		return ResponseUtils.jsonView(pageInfo);
	}
	//获得用户所属角色的菜单
	@RequestMapping(method=RequestMethod.GET,value="/getAuthoSysMenuList")
	public ModelAndView getAuthoSysMenuList(HttpServletRequest request) {
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		Long roleId =user.getRoleId();
		List<Map<String, Object>> sysMenuList = sysMenuService.getSysMenuListByRoleId(roleId);
		return ResponseUtils.jsonView(sysMenuList);
	}
	//是否启用菜单（递归启用其子菜单）
	@RequestMapping(method=RequestMethod.POST,value="/isStartMenu")
	public ModelAndView isStartMenu(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Long menuId =Long.parseLong(String.valueOf(map.get("menuId")));
		Short state =Short.parseShort(String.valueOf(map.get("state")));
		Message message= sysMenuService.isStartMenu(request,menuId,state);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}