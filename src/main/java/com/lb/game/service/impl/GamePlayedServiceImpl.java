package com.lb.game.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.game.model.GamePlayed;
import com.lb.game.service.GamePlayedService;
import com.lb.sys.dao.GamePlayedMapper;
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class GamePlayedServiceImpl implements GamePlayedService {
	@Autowired
	private GamePlayedMapper gpm;
	@Override
	public List<GamePlayed> selectFootballGamePlayedByGroup(int id) {
		return gpm.selectFootballGamePlayedByGroup(id);
	}

}
