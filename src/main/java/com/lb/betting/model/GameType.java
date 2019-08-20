/**
 * 
 */
package com.lb.betting.model;

/**
 * @author wz
 * @describe 彩种
 * @date 2017年9月29日
 */
public class GameType {
	private Integer gameId;
	private String gameName;//名称
	private Byte status;
	private Byte game_type;
	/**
	 * @return the gameId
	 */
	public Integer getGameId() {
		return gameId;
	}
	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
	/**
	 * @return the gameName
	 */
	public String getGameName() {
		return gameName;
	}
	/**
	 * @param gameName the gameName to set
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	/**
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Byte getGame_type() {
		return game_type;
	}
	public void setGame_type(Byte game_type) {
		this.game_type = game_type;
	}
	
	
}
