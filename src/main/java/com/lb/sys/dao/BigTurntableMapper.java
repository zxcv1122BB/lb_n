package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.activity.model.BigTurntable;
import com.lb.activity.model.BigTurntableExample;

public interface BigTurntableMapper {
	
    long countByExample(BigTurntableExample example);

    int deleteByExample(BigTurntableExample example);

    int deleteByPrimaryKey(Integer bid);

    int insert(BigTurntable record);

    int insertSelective(Map<String, Object> map);

    List<BigTurntable> selectByExample(BigTurntableExample example);

    BigTurntable selectByPrimaryKey(Integer bid);

    int updateByExampleSelective(@Param("record") BigTurntable record, @Param("example") BigTurntableExample example);

    int updateByExample(@Param("record") BigTurntable record, @Param("example") BigTurntableExample example);

    int updateByPrimaryKeySelective(BigTurntable record);

    int updateByPrimaryKey(BigTurntable record);

	List<Map<String, Object>> queryBigTurntableList();

	Map<String, Object> queryBigTurntableById(Integer bid);
}