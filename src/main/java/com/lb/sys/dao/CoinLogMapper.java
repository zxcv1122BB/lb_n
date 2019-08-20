package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;

import com.lb.member.model.CoinLog;
import com.lb.member.model.CoinLogExample;
import com.lb.report.model.OperationAnalysis;
import com.lb.report.model.TeamCount;

public interface CoinLogMapper {
  
    long countByExample(CoinLogExample example);

  
    int deleteByExample(CoinLogExample example);

  
    int deleteByPrimaryKey(Integer id);

  
    int insert(CoinLog record);

  
    int insertSelective(CoinLog record);

 
    List<CoinLog> selectByExample(CoinLogExample example);

   
    CoinLog selectByPrimaryKey(Integer id);

   
    int updateByExampleSelective(@Param("record") CoinLog record, @Param("example") CoinLogExample example);

   
    int updateByExample(@Param("record") CoinLog record, @Param("example") CoinLogExample example);

   
    int updateByPrimaryKeySelective(CoinLog record);

   
    int updateByPrimaryKey(CoinLog record);

	List<Map<String, Object>> queryCoinLogList(Map<String, Object> map);

	int insertAddAndSubtractMoney(HttpServletRequest request, CoinLog coinLog);
	
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
	 * 查询统计当前上一个月各项总金额信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> queryMonthsSumAmountInfo(Map<String,Object> paramMap);
	
	List<OperationAnalysis> getDepositList(Map<String,Object> map);
	
	Integer addCoinLogRollBack(Map<String,Object> param);


	Map<String, Object> queryProxyRebateRatioDate(Map<String, Object> hash);


	OperationAnalysis getDepositListTotal(Map<String, Object> map);


	Map<String, Object> queryProxyByUserName(@Param("userName") String userName);


	int updateProxyCoin(Map<String, Object> usermap);


	List<Map<String, Object>> getCoinOperateType();
	
	/**
	 * 查询批量投注返利的账变信息 
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryUserRebateCoinLog(String batch_no);
	
	int insertOfURebateRollback(List<Map<String, Object>> list);
}