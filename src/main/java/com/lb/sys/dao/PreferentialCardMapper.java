package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.download.model.PreferentialCardRecord;


public interface PreferentialCardMapper {
	
	List<Map<String, Object>> queryPreferentialCard(Map<String, Object> map1);

	int addPreferentialCard(Map<String, Object> map);

	int updatePreferentialCard(Map<String, Object> map);

	int deletePreferentialCard(@Param("id")Integer id);

	List<PreferentialCardRecord> exportPreferentialCard(Map<String, Object> map);
}