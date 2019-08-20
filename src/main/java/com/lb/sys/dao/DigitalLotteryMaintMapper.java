/**
 * 
 */
package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DigitalLotteryMaintMapper {

	List<Map<String,Object>> getNewDigitalLotteryList(Map<String,Object> param);
	
	List<Map<String,Object>> getDigitalLotteryTypeList();
	
	List<Map<String,Object>> getDigitalLotteryIssueList(Map<String,Object> param);
	
	Map<String,Object> selectLotteryData(Map<String,Object> param);
	
	Map<String,Object> getDigitalLotteryIssue(Map<String,Object> param);
	
	Map<String, Object> qryOpenDataById(@Param("id") String id);
}
