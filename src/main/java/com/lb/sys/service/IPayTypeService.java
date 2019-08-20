package com.lb.sys.service;

import java.util.List;
import java.util.Map;

public interface IPayTypeService {

	List<Map<String, Object>> queryPayList(String payType);

	Integer addPayType(Map<String, Object> map);

	Integer delPayTypeByid(String id);

	Integer updatePayType(Map<String, Object> map);

}
