/**
 * 
 */
package com.lb.betting.service;

import java.util.List;
import java.util.Map;

import com.lb.betting.model.BettingOrderDetail;
import com.lb.betting.model.BettingOrderInfo;
import com.lb.betting.model.BettingTicketDetails;
import com.lb.betting.model.GameType;
import com.lb.betting.model.UserDetail;

public interface BettingOrderInfoService {
	
	List<BettingOrderInfo> queryBettingOrderInfo(Map<String, Object> map);
	
	UserDetail queryUserInfo(String userName);
	
	List<GameType> queryGameType();
	
	BettingOrderDetail queryBettingOrder(Map<String, Object> map);
	
	Map<String,Object> queryNumbersBettingDetail(Map<String, Object> map);
	
	int cancelTheOrder(Map<String, Object> map);

	List<BettingOrderInfo> queryFailOrderInfo(Map<String, Object> map);
	
	//List<BettingInfoDetail> queryBettingMatchDetail(String actionData);
	List<BettingTicketDetails> getActionDataResult(Map<String, Object> map);
	
	int prizeRollbackOperation(Map<String, Object> map);
	
	int prizeOperation(Map<String, Object> map);
	
	int prizeOperationByBet(Map<String, Object> map);
	
	int numberPrizeOperation(Map<String, Object> map);
	
	Map<String,Object> getRecentlyBetCensus();
	
	Map<String,Object> getRecentlyDepositCensus();
	
	Map<String,Object> getRecentlyWithdrawCensus();
	
	Map<String,Object> getRecentlyCoinUpdateCensus();
}
