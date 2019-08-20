package com.lb.sys.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.sys.model.SysMenu;
import com.lb.sys.tools.model.Message;

public interface ISysMenuService{
  
    Message deleteByPrimaryKey(Long menuId);
    
    Message insertSelective(HttpServletRequest request, SysMenu record);
   
    SysMenu selectByPrimaryKey(Long menuId);

    Message updateByPrimaryKeySelective(HttpServletRequest request, SysMenu record);

	List<SysMenu> queryMenuListByParentId(Map<String, Object> hashMap);

	List<Map<String, Object>> getSysMenuListByRoleId(Long roleId);

	List<SysMenu> queryRoleMenuList(Map<String, Object> param);

	int selectByMenuIdRoleId(Map<String, Object> param);

	int insertRoleMenuRel(Map<String, Object> param);

	int deleteRelById(Map<String, Object> param);

	List<SysMenu> querySysMenuList();

	List<Map<String, Object>> queryAuthorizationMenu(Map<String, Object> map);

	Message batchAuthorization(Map<String, Object> param);

	Message batchDelete(Map<String, Object> param);

	Message isStartMenu(HttpServletRequest request, Long menuId, Short state);

	List<Map<String, Object>> queryLike(Map<String, Object> map);

	List<Map<String, Object>> queryAll();

	String queryMenuPath(HttpServletRequest request,Long valueOf);

	Message queryByMenuName(Map<String, Object> map);

	List<Map<String,Object>> getRealyRoleList(Map<String,Object> map);
}