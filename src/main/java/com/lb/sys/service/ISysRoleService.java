package com.lb.sys.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.sys.model.SysRole;
import com.lb.sys.tools.model.Message;

public interface ISysRoleService {
    
    Message deleteByPrimaryKey(Long roleId);
   
    Message insertSelective(HttpServletRequest request, SysRole record);

    List<SysRole> selectByExample();
    
    SysRole selectByPrimaryKey(Long roleId);
  
    Message updateByPrimaryKeySelective(HttpServletRequest request, SysRole record);

	Message queryRoleByRoleName(String roleName);

	Message isStartRole(HttpServletRequest request, Map<String, Object> map);

	List<SysRole> queryRealyRole(Map<String,Object> map);
}