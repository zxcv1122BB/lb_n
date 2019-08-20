package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface PayQuickMapper {

	List<Map<String, Object>> queryPayList();
	
	Integer delPayQuickType(Integer id);
	
	Integer insertPayQuickType(Map<String, Object> map);
	
	Integer updatePayQuickTypeByid(Map<String, Object> map);

}
