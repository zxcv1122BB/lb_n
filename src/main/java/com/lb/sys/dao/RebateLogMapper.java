package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface RebateLogMapper {
	
	Map<String,Object> qryRebateLogByBatchNo(String batch_no);

	List<Map<String,Object>> qryRebateLog();
	
	int updateStatus(Map<String,Object> paramMap);
	
}
