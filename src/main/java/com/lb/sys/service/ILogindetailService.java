package com.lb.sys.service;

import java.util.Map;


public interface ILogindetailService {


	Integer insertSelective(Map<String,Object> loginDetail);
	
	Integer updateByUsernameSelective(Map<String,Object> loginDetail);

	Map<String, Object> getDepositAndWithdrawCount();
	
	Integer updateLastTimeAndIp(Map<String,Object> param);
	
	public void offlineHandle(String userName);

	Integer insertAgencySelective(Map<String, Object> map);

	public void agencyofflineHandle(String userName);
}
