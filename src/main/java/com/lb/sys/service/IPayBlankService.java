package com.lb.sys.service;

import java.util.List;
import java.util.Map;

public interface IPayBlankService {

	List<Map<String, Object>> queryPayList();

	Integer addPayInfo(Map<String, Object> map);

	Integer delPayInfoByid(Integer integer);

	Integer updatePayInfo(Map<String, Object> map);

}
