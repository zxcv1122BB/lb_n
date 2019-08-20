package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.log.model.LogInfo;
import com.lb.log.model.LogInfoExample;
import com.lb.sys.tools.model.Message;

public interface LogInfoMapper {
    long countByExample(LogInfoExample example);

    int deleteByExample(LogInfoExample example);

    int deleteByPrimaryKey(String logid);

    int insert(LogInfo record);

    int insertSelective(LogInfo record);

    List<LogInfo> selectByExample(LogInfoExample example);

    LogInfo selectByPrimaryKey(String logid);

    int updateByExampleSelective(@Param("record") LogInfo record, @Param("example") LogInfoExample example);

    int updateByExample(@Param("record") LogInfo record, @Param("example") LogInfoExample example);

    int updateByPrimaryKeySelective(LogInfo record);

    int updateByPrimaryKey(LogInfo record);
    
    //添加日志
    Message addRiZhi(@Param("log") LogInfo log);
    
    //查询
    List<LogInfo> selectAllLogs(@Param("pam")LogInfo pam);
}