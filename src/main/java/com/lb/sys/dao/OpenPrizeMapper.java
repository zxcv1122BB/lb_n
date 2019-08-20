package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OpenPrizeMapper {

	List<Map<String, Object>> selectBetInfo(@Param("type") int oneTypeId, @Param("issue") String issue);

	String selectGamePlayedInfo(String id);
	
	Map<String, Object> qryGamePlayedInfo(String id);
	
	int updateGameBet(Map<String, Object> map);

	Map<String, Object> selectBeforeOrder(String beforeOrderId);

	int insertOpenRecord(Map<String, Object> map);

	int selectOpenRecord(@Param("type") int oneTypeId, @Param("issue") String issue,
			@Param("luckNumber") String luckNumber);
	
	int updateOpenRecord(Map<String, Object> map);

	Map<String, Object> selectLuckNum(String oneTypeId);

	int updateSetLuckNum(Map<String, Object> map);

	String sureIssueInDb(String openDataId);
}
