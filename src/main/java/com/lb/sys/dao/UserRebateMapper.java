package com.lb.sys.dao;

import java.util.Map;

public interface UserRebateMapper {
	
	//List<Map<String,Object>> qryBetSum(Map<String,Object> paramMap);
	
	int updateUserCoin(Map<String,Object> paramMap);
	
	int updateGameBetStatus(Map<String,Object> paramMap);
	
	int insertCoinLog(Map<String,Object> paramMap);
	
	int updateStatusBatch(Map<String,Object> paramMap);
	
	int insertRebateLog(Map<String,Object> paramMap);
}
