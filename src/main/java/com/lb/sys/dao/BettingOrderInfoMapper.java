/**
 * 
 */
package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.betting.model.BettingInfoDetail;
import com.lb.betting.model.BettingOrderDetail;
import com.lb.betting.model.BettingOrderInfo;
import com.lb.betting.model.GameType;
import com.lb.betting.model.ItemModel;
import com.lb.betting.model.UserDetail;

/**
 * @author wz
 * @describe 投注订单信息dao层接口
 * @date 2017年9月20日
 */
public interface BettingOrderInfoMapper {
	
	List<BettingOrderInfo> queryBettingOrderInfo(Map<String, Object> map);
	//按照批次查询
	List<BettingOrderInfo> queryBettingOrderInfoByCal_no(Map<String, Object> map);
	
	List<BettingOrderInfo> queryFailOrderInfo(Map<String, Object> map);
	
	UserDetail queryUserInfo(@Param("userName")String userName);
	
	List<GameType> queryGameType();
	
	BettingOrderDetail queryBettingOrder(Map<String, Object> map);
	
	int cancelTheOrder(Map<String, Object> map);
	
	int userAccountChange(Map<String, Object> map);
	
	Map<String,Object> queryNumbersBettingDetail(Map<String, Object> map);
	
	int prizeOperation(Map<String, Object> map);
	
	int numberPrizeOperation(Map<String, Object> map);
	
	Map<String, Object> getOpenTaskDigitInfo(Map<String, Object> map);
	
	/*BettingInfoDetail queryBettingDetailList(@Param("quizName") String quizName,
			@Param("matchId") String matchId);
	
	BettingInfoDetail queryBasketballDetailList(@Param("quizName") String quizName,
			@Param("matchId") String matchId);
	*/
	List<BettingInfoDetail> queryBettingDetailList(Map<String,Object> map);
	
	List<BettingInfoDetail> queryBasketballDetailList(Map<String,Object> map);
	
	int insertCancelCoinLog(Map<String,Object> map);
	
	Map<String,Object> getActionDataResult(Map<String,Object> map);
	
	List<ItemModel> getCodeLibrary(@Param("quizType") String quizType);
	
	Map<String,Object> getUserBetsumInfo(Map<String,Object> map);
	
	List<Map<String,Object>> getCorrespondingGameBets(Map<String,Object> map);
	
	int correspondingGameBetsInfo(Map<String,Object> map);
	
	int correspondingUserInfo(Map<String,Object> map);
	
	/**--------数字彩手动开奖方法 start---------*/
	Map<String,Object> selectBetInfo(Map<String,Object> map);
	String selectGamePlayedInfo(@Param("id") String id);
	int updateGameBet(Map<String, Object> map);
	/**--------数字彩手动开奖方法 end---------*/
	List<Map<String,Object>> selectCoinLogByOrderId(Map<String, Object> map);
	int correspondingProxy(List<Map<String,Object>> map);
	int insertProxyCancelCoinLog(List<Map<String,Object>> map);
	
	Map<String,Object> getRecentlyBetCensus();
	
	Map<String,Object> getRecentlyDepositCensus();
	
	Map<String,Object> getRecentlyWithdrawCensus();
	
	Map<String,Object> getRecentlyCoinUpdateCensus();
}