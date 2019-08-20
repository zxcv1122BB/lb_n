package com.lb.activity.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.activity.model.BigTurntable;
import com.lb.activity.model.TurntablePrize;
import com.lb.sys.tools.model.Message;

public interface IBigTurntableService {

	Message isStartBigTurntable(HttpServletRequest request, Integer bid, Byte state);

	Message deleteBigTurntable(HttpServletRequest request, Integer bid);

	Message updateBigTurntable(HttpServletRequest request, BigTurntable bigTurntable);

	Message addBigTurntable(HttpServletRequest request, Map<String, Object> map);

	List<Map<String, Object>> queryBigTurntableList();

	Map<String, Object> queryBigTurntableById(Integer bid);

	List<TurntablePrize> isExistBigTurntable(Map<String, Object> map);

	Message getTurntablePrize(HttpServletRequest request, Map<String, Object> map);

	long queryBigTurntableVip(Integer integer);

}
