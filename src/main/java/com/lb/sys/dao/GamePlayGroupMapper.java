package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.game.model.GamePlayGroup;
import com.lb.game.model.GamePlayGroupExample;

public interface GamePlayGroupMapper {
    long countByExample(GamePlayGroupExample example);

    int deleteByExample(GamePlayGroupExample example);

    int deleteByPrimaryKey(Short id);

    int insert(GamePlayGroup record);

    int insertSelective(GamePlayGroup record);

    List<GamePlayGroup> selectByExample(GamePlayGroupExample example);

    GamePlayGroup selectByPrimaryKey(Short id);

    int updateByExampleSelective(@Param("record") GamePlayGroup record, @Param("example") GamePlayGroupExample example);

    int updateByExample(@Param("record") GamePlayGroup record, @Param("example") GamePlayGroupExample example);

    int updateByPrimaryKeySelective(GamePlayGroup record);

    int updateByPrimaryKey(GamePlayGroup record);
    
    List<GamePlayGroup>  selectAllFootballGamePlayGroup(@Param("gameID")Integer gameID);
}