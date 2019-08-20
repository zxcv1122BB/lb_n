package com.lb.sys.service;

import java.util.List;
import java.util.Map;

import com.lb.sys.tools.model.Message;

public interface IDigitalLotteryMaintService {

	List<Map<String,Object>> getNewDigitalLotteryList(Map<String,Object> param);
	
	Map<String,Object> getDigitalLotteryInfoList(Map<String,Object> param);
	
	List<Map<String,Object>> getDigitalLotteryIssueList(Map<String,Object> param);

	int updateDigitalLotteryInfo(Map<String,Object> param);
	
	Message updateOpenDataInfo(Map<String,Object> param);
	
	Message saveOpenDataInfo(Map<String,Object> param);

	Map<String, Object> qryOpenDataById(String id);
}
