package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

public interface PayTypeMapper {

	List<Map<String, Object>> selectPayList(String payType);

	Integer insertPayType(Map<String, Object> map);

	Integer delPayTypeByid(String id);

	Integer updatePayType(Map<String, Object> map);

}
