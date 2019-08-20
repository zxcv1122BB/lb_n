package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface LotteryPlanMapper {

	List<Map<String, Object>> selectIssuePlan(Map<String, Object> param);

	Map<String, Object> selectPlanMethod(String playId);

	int insertLotteryData(Map<String, Object> data);
	
	int insertLotteryPlan(Map<String, Object> data);
	
	List<Map<String, Object>> getAllPlay(Map<String, Object> map);
	
}
