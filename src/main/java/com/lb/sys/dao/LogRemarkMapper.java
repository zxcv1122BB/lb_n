package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.log.model.LogRemark;
import com.lb.log.model.LogRemarkExample;

public interface LogRemarkMapper {
    long countByExample(LogRemarkExample example);

    int deleteByExample(LogRemarkExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LogRemark record);

    int insertSelective(LogRemark record);

    List<LogRemark> selectByExample(LogRemarkExample example);

    LogRemark selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LogRemark record, @Param("example") LogRemarkExample example);

    int updateByExample(@Param("record") LogRemark record, @Param("example") LogRemarkExample example);

    int updateByPrimaryKeySelective(LogRemark record);

    int updateByPrimaryKey(LogRemark record);
}