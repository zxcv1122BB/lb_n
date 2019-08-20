/**
 * 
 */
package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface SignInfoManageMapper {

	List<Map<String,Object>> querySignInfoList(Map<String,Object> param);
	int addSignInfo(Map<String,Object> param);
	int updateSignInfo(Map<String,Object> param);
	int removeSignInfo(Map<String,Object> param);
	int realyRemoveSignInfo(Map<String,Object> param);
	List<Map<String,Object>> getUserSignList(Map<String,Object> param);
	Map<String,Object> getUserSignCount(Map<String,Object> param);
	int getStartedSignCount(Map<String,Object> param);
}
