package com.lb.rebate.service;

import java.util.List;
import java.util.Map;

public interface UserRebateService {
	/**
	 * 查询批量用户返利信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String, Object>> qryRebateLog();
	
	/**
	 * 批量用户返利回滚
	 * @param paramMap
	 * @return
	 */
	boolean batchRollBack(String batch_no);
	
	/**
	 * 批量投注返利
	 * 	——仅用于[系统自动跑批]->[手动回滚]->后的[手动跑批]
	 * @param batch_no
	 * @return
	 */
	boolean batchBetRebate(String batch_no);
}
