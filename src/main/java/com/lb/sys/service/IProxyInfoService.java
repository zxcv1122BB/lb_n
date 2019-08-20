package com.lb.sys.service;


import java.util.List;
import java.util.Map;

import com.lb.sys.model.ProxyInfo;
import com.lb.sys.model.UserVipModel;

/**
 * 代理管理service
 * @author Administrator
 *
 */
public interface IProxyInfoService {
	
    int deleteByProxyInfo(ProxyInfo proxy);

    int insertProxyInfo(ProxyInfo proxy);

    int updateByProxyInfo(ProxyInfo proxy);
    
    int updateByStatus(ProxyInfo proxy);

	List<UserVipModel> selectByProxyInfo(Map<String, Object> map);

	List<Map<String, Object>> getProxyUserList(Map<String, Object> param);

	List<Map<String, Object>> selectProxyInfoAll();

	int checkAccount(Map<String, Object> map);

	boolean updatePassword(Map<String, Object> map);

	ProxyInfo getProxyById(Integer pid);
	
	String queryProxyIds(Integer type,Integer uid);
}
