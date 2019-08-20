package com.lb.report.service;

import java.util.List;
import java.util.Map;

public interface ReportMonthSummaryService {
	
	/**
	 * 分页查询每月各项的总结统计 
	 * @return
	 */
	List<Map<String, Object>> queryReportMonthSummaryList();
	
}
