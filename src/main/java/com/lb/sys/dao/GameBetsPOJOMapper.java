package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.report.model.GameBetsPOJO;

public interface GameBetsPOJOMapper {

	/**
	 * 查询今日、昨日中奖金额、投注金额以及盈亏(投注金额-中奖金额) 
	 * @return
	 */
	GameBetsPOJO queryTodayAndYesterDayMoney();
	
	/**
	 * 根据时间区间查询用户投注信息 
	 * @param paramterMap
	 * @return
	 */
	List<Map<String,Object>> queryGameBetsInfo(Map<String,Object> paramterMap);
	List<Map<String,Object>> getLotteryList(Map<String, Object> map);

	Map<String, Object> getLotteryListTotal(Map<String, Object> map);
	
	int getLotteryUserNum(@Param("oneTypeId")String oneTypeId, @Param("issue")String issue);
	
	List<Map<String,Object>> getUserLotteryInfo(@Param("oneTypeId")String oneTypeId, @Param("issue")String issue);
	
	int updateOrderRebateStatus(String batch_no);
	
	//[批量投注返利回滚] —— 按批次号查询订单
	List<Map<String,Object>> qryBetSumByBatchNo(String batch_no);
	
	//根据用户id 查询打码量相应的投注记录
	List<Map<String,Object>> qryGamebetsBetSum(@Param("uid")Integer uid);
}