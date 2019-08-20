package com.lb.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lb.report.model.GameBetsPOJO;
import com.lb.report.service.GameBetsService;
import com.lb.sys.dao.GameBetsPOJOMapper;

@Service
@Transactional
public class GameBetsServiceImpl implements GameBetsService{
	
	@Autowired
	private GameBetsPOJOMapper gameBetsPOJOMapper;
	
	@Override
	public GameBetsPOJO queryTodayAndYesterDayMoney() {
		return this.gameBetsPOJOMapper.queryTodayAndYesterDayMoney();
	}

	@Override
	public List<Map<String, Object>> getLotteryList(Map<String, Object> map) {
		return gameBetsPOJOMapper.getLotteryList(map);
	}

	@Override
	public Map<String, Object> getLotteryListTotal(Map<String, Object> map) {
		return gameBetsPOJOMapper.getLotteryListTotal(map);
	}
	@Override
	public List<Map<String, Object>> qryGamebetsBetSum(Integer uid) {
		return gameBetsPOJOMapper.qryGamebetsBetSum(uid);
	}
}
