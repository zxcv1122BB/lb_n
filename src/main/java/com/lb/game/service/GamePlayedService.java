package com.lb.game.service;

import java.util.List;

import com.lb.game.model.GamePlayed;

public interface GamePlayedService {

	//查询最后一级玩法
    List<GamePlayed> selectFootballGamePlayedByGroup(int id);
}
