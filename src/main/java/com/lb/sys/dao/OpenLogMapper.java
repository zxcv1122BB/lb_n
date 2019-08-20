package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface OpenLogMapper {
	
	/***
	 * 查询所有的开奖日志
	 * @return
	 */
	 public List<Map<String, Object>> getAll(Map<String,Object> parmMap);
	 /***
	  * 根据主键查询详情
	  * @param parmMap 参数map
	  * @return
	  */
	 public Map<String, Object> selectByOpenno(Map<String,Object> parmMap);
	 
	 /**
	  * 查询指定赛事
	  * @param parmMap
	  * @return
	  */
	 public Map<String, Object>  selectMatchById(@Param("match_id") String match_id);
}
