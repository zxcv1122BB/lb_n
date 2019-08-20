package com.lb.sys.service;

import java.util.List;
import java.util.Map;

public interface IWebListenerSerivice {

	Map<String,Object> selectWebList(String webName);
	
	List<Map<String,Object>> selectWebTitleList();
}
