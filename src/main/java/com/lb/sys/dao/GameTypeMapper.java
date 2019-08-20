package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.game.model.GameType;
import com.lb.game.model.GameTypeExample;

public interface GameTypeMapper {
    long countByExample(GameTypeExample example);

    int deleteByExample(GameTypeExample example);

    int deleteByPrimaryKey(Byte gameid);

    int insert(GameType record);

    int insertSelective(GameType record);

    List<GameType> selectByExample(GameTypeExample example);

    GameType selectByPrimaryKey(Byte gameid);

    int updateByExampleSelective(@Param("record") GameType record, @Param("example") GameTypeExample example);

    int updateByExample(@Param("record") GameType record, @Param("example") GameTypeExample example);

    int updateByPrimaryKeySelective(GameType record);

    int updateByPrimaryKey(GameType record);
    
    String selectGameNameById(String gameID);

	List<Map<String, String>> selectSysColorType();

	List<Map<String, Object>> selectSystemColorNum(Map<String, Object> map);
	//查询全部彩种(有效)
	List<Map<String, Object>> qryAllGameType();
	
	String findGameTypeGameIds();
}