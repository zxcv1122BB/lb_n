package com.lb.sys.controller;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@RequestMapping("/authorization")
public class AuthorizationController extends BaseController{
	@Autowired
	private ISysMenuService sysMenuService;
	
	private final static Log LOGGER = LogFactory.getLog(AuthorizationController.class);
	
	//需要传入roleId
	@RequestMapping(method=RequestMethod.GET,value="/queryRoleMenuList")
	public ModelAndView queryRoleMenuList(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		//当前页
		Integer pageIndex = Integer.valueOf(map.get("pageIndex").toString());
		//每一页数据条数
		Integer pageNum = Integer.valueOf(map.get("pageNum").toString());
		//角色Id
		Long roleId =Long.parseLong(String.valueOf(map.get("roleId")));
		Map<String, Object> param = new HashMap<>();
		param.put("roleId", roleId);
		List<SysMenu> menuList=sysMenuService.queryRoleMenuList(param);
		PageUtils<SysMenu> pageInfo =  new PageUtils<>(pageIndex,pageNum,menuList);
		return ResponseUtils.jsonView(pageInfo);
	}

	//查询授权列表，包含所有菜单，通过roleId查询，isPublic为标记：1是指角色包含次菜单，0指不包含
	@RequestMapping(method=RequestMethod.GET,value="/queryAuthorizationMenu")
	public ModelAndView queryAuthorizationMenu(HttpServletRequest request) {
		SysUser user = (SysUser) request.getSession().getAttribute(LoginController.LOGINUSER);
		Map<String, Object> map = BaseController.jsonToMap(request);
		if(map == null || user == null || user.getRoleId() == null) {
			LOGGER.error("该用户未登录或没有该权限");
			return ResponseUtils.jsonView(333,"权限异常");
		}
		map.put("adminRoleId", user.getRoleId());
		List<Map<String, Object>> menuList = sysMenuService.getRealyRoleList(map);
		return menuList!=null && menuList.size() > 0 ? ResponseUtils.jsonView(200,"获取成功",menuList) : ResponseUtils.jsonView(201,"暂无数据",menuList);
	}
	//此处传入参数为roleId和menuId
	@RequestMapping(method=RequestMethod.POST,value="/batchDelete")
	public ModelAndView batchDelete(HttpServletRequest request) {
		Map<String, Object> map = BaseController.jsonToMap(request);
		Integer[] menuIdss=(Integer[]) map.get("menuIds");
		List<Long> list =new ArrayList<>();
		for (Integer inte : menuIdss) {
			list.add(Long.valueOf(String.valueOf(inte)));
		}
		map.put("menuIds", list);
		Message message= sysMenuService.batchDelete(map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
	@RequestMapping(method=RequestMethod.POST,value="/batchAuthorizationMenu")
	public ModelAndView batchAuthorizationMenu(HttpServletRequest request) {
		Map<String, Object> hashMap = BaseController.jsonToMap(request);
		String menuIds =String.valueOf(hashMap.get("menuIds")) ;
		List<Long> list =new ArrayList<>();
		if(!StringUtils.isEmpty(menuIds)) {
			String[] split = menuIds.split(",");
			for (String inte : split) {
				list.add(Long.valueOf(inte));
			}
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("menuIds", list);
		map.put("roleId", hashMap.get("roleId"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = sdf.format(new Date());
		map.put("date",format);//授权时间
		Message message= sysMenuService.batchAuthorization(map);
		return ResponseUtils.jsonView(message.getCode(),message.getMsg(),null);
	}
}
	

	

