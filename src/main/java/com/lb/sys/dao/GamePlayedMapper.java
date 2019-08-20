package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.game.model.GamePlayed;
import com.lb.game.model.GamePlayedExample;

public interface GamePlayedMapper {
    long countByExample(GamePlayedExample example);

    int deleteByExample(GamePlayedExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GamePlayed record);

    int insertSelective(GamePlayed record);

    List<GamePlayed> selectByExample(GamePlayedExample example);

    GamePlayed selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GamePlayed record, @Param("example") GamePlayedExample example);

    int updateByExample(@Param("record") GamePlayed record, @Param("example") GamePlayedExample example);

    int updateByPrimaryKeySelective(GamePlayed record);

    int updateByPrimaryKey(GamePlayed record);
    
    //查询最后一级玩法
    List<GamePlayed> selectFootballGamePlayedByGroup(@Param("id") int id);
}