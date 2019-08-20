package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface WebListenerMapper {

	/**
	 * 
	 *@describe:获取所有的项目
	 * @return
	 */
	Map<String,Object> selectWebList(String webName);
	
	//selectWebTitleList
	List<Map<String,Object>> selectWebTitleList();
}
