package com.lb.sys.dao;

import java.util.Map;

public interface LoginDetailMapper {
   
	Integer insertSelective(Map<String,Object> loginDetail);

	Integer updateByUsernameSelective(Map<String,Object> loginDetail);

	Integer getDepositCount(Integer state);

	Integer getWithdrawCount(Integer state);
	
	Integer updateLastTimeAndIp(Map<String,Object> param);

	Integer insertAgencySelective(Map<String, Object> map);

	Integer updateAgencyByUsernameSelective(Map<String, Object> map);

	Integer updateAgencyLastTimeAndIp(Map<String, Object> map);
  
}