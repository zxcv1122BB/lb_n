package com.lb.record.service;

import java.util.List;
import java.util.Map;

public interface OpenRecordService {

	/**
	 * 获取开奖记录信息 包括分页和筛选
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryOpenRecord(Map<String, Object> map);

	/**
	 * 获取数字彩开奖记录信息 包括分页和筛选
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryDigitRecord(Map<String, Object> map);

	/**
	 * 查询所有的比赛分类
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryMatchType();

	/**
	 * 查询所有的数字彩彩种
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryDigitType();
}
