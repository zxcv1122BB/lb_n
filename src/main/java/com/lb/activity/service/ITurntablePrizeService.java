package com.lb.activity.service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.activity.model.TurntablePrize;
import com.lb.sys.tools.model.Message;

public interface ITurntablePrizeService {

	List<TurntablePrize> queryTurntablePrizeList(Map<String, Object> map);

	Message addTurntablePrize(HttpServletRequest request, TurntablePrize turntablePrize);

	TurntablePrize queryTurntablePrizeById(Integer bid);

	Message updateTurntablePrize(HttpServletRequest request, TurntablePrize turntablePrize);

	Message deleteTurntablePrize(HttpServletRequest request, Integer bid);

	Message isStartTurntablePrize(HttpServletRequest request, Integer bid, Byte state);

}
