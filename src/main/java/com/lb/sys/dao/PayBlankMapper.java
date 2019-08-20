package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface PayBlankMapper {

	List<Map<String, Object>> queryPayList();

	Integer insertBlankInfoSelective(Map<String, Object> map);

	Integer delPayBlankInfo(Integer id);

	Integer updateBlankInfoByid(Map<String, Object> map);

}
