package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface ReportMonthSummaryMapper {
	
	/**
	 * 分页查询每月各项的总结统计
	 * @return
	 */
	List<Map<String,Object>> queryReportMonthSummaryList();
	
	/**
	 * 新增每月各项统计信息 
	 * @param paramterMap
	 * @return
	 */
	int insert(Map<String,Object> paramterMap);
	
	
}
