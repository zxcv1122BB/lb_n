package com.lb.member.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.report.model.OperationAnalysis;
import com.lb.report.model.TeamCount;
import com.lb.sys.tools.model.Message;

public interface ICoinLogService {

	List<Map<String, Object>> queryCoinLogList(Map<String, Object> map);

	Message addAndSubtractMoney(HttpServletRequest request, Map<String, Object> map);
	

	/**
	 * 财务报表数据统计
	 * @param paramMap
	 * @return
	 */
	List<TeamCount> queryFinanceList(Map<String,Object> paramMap);
	
	/**
	 * 财务报表历史数据统计 
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> queryHistoryFinanceList(Map<String,Object> paramMap);
	
	/**
	 * 计算上一周各用户反水金额=(投注金额-中奖金额)*配置比例
	 * @return
	 */
	Message toWeekReturnWaterDatas();

	List<OperationAnalysis> getDepositList(Map<String, Object> map);

	OperationAnalysis getDepositListTotal(Map<String, Object> map);

	BigDecimal queryDepositGiveScheme(BigDecimal amount, Integer recharge_times);

	int getVIPId(String uid, BigDecimal amount);

	Map<String, Object> queryProxyByUserName(String userName);

	Message proxyAddAndSubtractMoney(HttpServletRequest request, Map<String, Object> map);

	List<Map<String, Object>> getCoinOperateType();
	
	
}
