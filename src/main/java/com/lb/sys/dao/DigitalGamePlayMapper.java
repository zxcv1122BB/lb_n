package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DigitalGamePlayMapper {
	
	List<Map<String,Object>> qryGameType();
	List<Map<String,Object>> qryGameGroup(@Param("id") Integer id);
	List<Map<String,Object>> qryGamePlayed(@Param("id") Integer id);
	
	
	
	int updateGameType(Map<String,Object> paramMap);
	int updateGameGroup(Map<String,Object> paramMap);
	int updateGamePlayed(Map<String,Object> paramMap);
	
	
}
