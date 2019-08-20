package com.lb.report.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.report.model.GlobalCount;
import com.lb.report.model.TeamCount;
import com.lb.sys.tools.model.Message;

public interface ProxyDistributionService {

	/**
	 * 新增代理商分销信息
	 * @param paramMap
	 * @return
	 */
	Message insertProxyDistribution(Map<String,Object> paramMap);
	
	/**
	 * 代理商分销金额返点
	 * @param paramMap 参数 ：时间区间（startDate,endDate）、状态（status）
	 * @return
	 */
	Message rebateProxyDistribution(Map<String,Object> paramMap);

	List<Map<String, Object>> getAgentRebateList(Map<String, Object> map);

	Integer rollBack(Map<String, Object> map);

	Map<String, Object> queryCountGK();

	List<Map<String, Object>> teamCount(Map<String, Object> map);

	int isExsitAgency(String string);

	GlobalCount globalCount( Map<String, Object> map);

	TeamCount queryTotalTeamCount(Map<String, Object> map);
	
	Map<String,Object> queryHomeTodayReports();
}
