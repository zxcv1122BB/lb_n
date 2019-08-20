/**
 * 
 */
package com.lb.sign.service;

import java.util.List;
import java.util.Map;

public interface SignInfoManageService {
	
	List<Map<String,Object>> querySignInfoList(Map<String,Object> param);
	int addSignInfo(Map<String,Object> param);
	int updateSignInfo(Map<String,Object> param);
	int removeSignInfo(Map<String,Object> param);
	int realyRemoveSignInfo(Map<String,Object> param);
	Map<String,Object> getUserSignList(Map<String,Object> param);
	int getStartedSignCount (Map<String,Object> param);
}
