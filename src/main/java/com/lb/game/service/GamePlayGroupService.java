package com.lb.game.service;

import java.util.List;

import com.lb.game.model.GamePlayGroup;

/****
 * 
 * @author ASUS
 *
 */
public interface GamePlayGroupService {
	 List<GamePlayGroup>  selectAllFootballGamePlayGroup(Integer gameID);
}
