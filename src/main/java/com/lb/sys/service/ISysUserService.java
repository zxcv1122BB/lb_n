package com.lb.sys.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.sys.model.SysUser;
import com.lb.sys.model.SysUserR;
import com.lb.sys.tools.model.Message;

public interface ISysUserService {
    
    Message insertSelective(HttpServletRequest request,SysUser record);
   
    List<SysUser> selectByExample();

    SysUser selectByPrimaryKey(Long userId);

	Message isStartUsing(HttpServletRequest request, Long userId, Short state);

	Message updateByPrimaryKeySelective(HttpServletRequest request, SysUser record);

	Map<String, Object> checkLogin(SysUser loginUser);

	Message queryUserByUserName(String userName);

	List<SysUserR> isExist(Map<String, Object> map);

	Message deleteByPrimaryKey(HttpServletRequest request, Long userId);

	Map<String,Object> queryGoogleCodeStateByUsername(String username);
		
	int updateGoogleCodeStateByUsername(Map<String,Object> map);
	
	//将密码错误的技术计入redis
	boolean insertCountToRedis(String key,String UID);
	//验证一个用户是否为锁定的状态
	boolean isLocking(String key,String uid);

	boolean isPassword(Map<String, Object> map);

	boolean updatePassword(Map<String, Object> map);
}