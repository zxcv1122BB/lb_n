package com.lb.game.service;

import java.util.List;
import java.util.Map;

import com.lb.game.model.GameType;
import com.lb.game.model.GameTypeExample;

public interface GameTypeService {
	List<GameType> selectByExample(GameTypeExample example);
	
	List<Map<String,Object>> qryAllGameType();
	
	boolean batchCanada28Issue(List<Map<String, Object>> gameTypeList,Map<String, Object> paramMap);
}
