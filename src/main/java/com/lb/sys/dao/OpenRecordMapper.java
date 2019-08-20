package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface OpenRecordMapper {

	/**
	 * 查询开奖记录 包含分页和筛选
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectOpenRecord(Map<String, Object> map);
	/**
	 * 查询开奖记录 包含分页和筛选
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> selectDigitRecord(Map<String, Object> map);

	/**
	 * 查询所有的比赛分类
	 * @return
	 */
	List<Map<String, Object>> selectMatchType();
	
	/**
	 * 查询所有的数字彩彩种
	 * @return
	 */
	List<Map<String, Object>> selectDigitType();
}
