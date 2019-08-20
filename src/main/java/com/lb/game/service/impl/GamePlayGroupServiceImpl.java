package com.lb.game.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.game.model.GamePlayGroup;
import com.lb.game.service.GamePlayGroupService;
import com.lb.sys.dao.GamePlayGroupMapper;
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class GamePlayGroupServiceImpl implements GamePlayGroupService {
	@Autowired
	private GamePlayGroupMapper dpdm;
	@Override
	public List<GamePlayGroup> selectAllFootballGamePlayGroup(Integer gameID) {
		return dpdm.selectAllFootballGamePlayGroup(gameID);
	}

}
