package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface DigitalOpenMapper {
	int insertDigitalOpenData(Map<String, Object> paramterMap);
	
	Map<String,Object> qryLastIssueData(Map<String,Object> one_type_id);
	
	//查询开奖数据
	List<Map<String,Object>> selectLotteryData(Map<String,Object> parm);

	void updateOpenData(String openDataId);
	
}
