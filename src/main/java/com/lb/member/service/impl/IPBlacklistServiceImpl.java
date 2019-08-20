package com.lb.member.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.member.service.IPBlacklistService;
import com.lb.sys.dao.IPBlacklistMapper;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class) 
public class IPBlacklistServiceImpl implements IPBlacklistService {
	
	@Autowired
	private IPBlacklistMapper ipBlacklistMapper;

	public Integer insertIPBlacklist(Map<String, Object> map) {
		
		return ipBlacklistMapper.insertIPBlacklist(map);
	}

	public Integer updateIpBlackList(Map<String, Object> map) {
		
		return ipBlacklistMapper.updateIpBlackList(map);
	}
	
	public List<Map<String,Object>> selectBlackIpByIp(String ip){
		return ipBlacklistMapper.selectBlackIpByIp(ip);
	}


	public List<Map<String, Object>> selectBlackIpBystatusAndIP(Map<String, Object> map) {
		
		return ipBlacklistMapper.selectBlackIpBystatusAndIP(map);
	}
	
	public Integer deleteBlacklistByid(Integer id) {
		
		return ipBlacklistMapper.deleteBlacklistByid(id);
	}

	@Override
	public Integer isExistIp(Map<String, Object> map) {
		return ipBlacklistMapper.isExistIp(map);
	}

}
