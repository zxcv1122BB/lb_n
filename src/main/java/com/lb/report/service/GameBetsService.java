package com.lb.report.service;

import java.util.List;
import java.util.Map;

import com.lb.report.model.GameBetsPOJO;

public interface GameBetsService {

	/**
	 * 查询今日、昨日中奖金额、投注金额以及盈亏(投注金额-中奖金额) 
	 * @return
	 */
	GameBetsPOJO queryTodayAndYesterDayMoney();

	List<Map<String, Object>> getLotteryList(Map<String, Object> map);

	Map<String, Object> getLotteryListTotal(Map<String, Object> map);
	
	List<Map<String,Object>> qryGamebetsBetSum(Integer uid);
	
}
