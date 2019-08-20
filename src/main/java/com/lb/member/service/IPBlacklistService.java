package com.lb.member.service;

import java.util.List;
import java.util.Map;

public interface IPBlacklistService {

	Integer insertIPBlacklist(Map<String,Object> map);
	
	Integer updateIpBlackList(Map<String,Object> map);
	
	List<Map<String,Object>> selectBlackIpByIp(String ip);
	
	List<Map<String,Object>> selectBlackIpBystatusAndIP(Map<String,Object> map);
	
	Integer deleteBlacklistByid(Integer id);

	Integer isExistIp(Map<String, Object> map);
}
