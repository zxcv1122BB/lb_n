package com.lb.log.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.log.model.LogInfo;
import com.lb.log.model.LogInfoExample;
import com.lb.sys.tools.model.Message;

/***
 * 日志模块业务逻辑接口层
 * @author tjr
 *
 */
public interface LogInfoService {
	   //添加一条日志记录
    int insert(LogInfo record);

    int insertSelective(LogInfo record);

    //查询所有的日志信息
    List<LogInfo> selectByExample(LogInfoExample example);
//    //根据某一个日志的编号  查询某一条日志记录的详情
//    LogInfo selectByPrimaryKey(String logid);
    //更新日志记录信息
    int updateByPrimaryKey(LogInfo record);
    
    
    //添加日志
    Message addRiZhi(@Param("log") LogInfo log);
    /**
     * 查询所有的日志
     * @param pam
     * @return
     */
    List<LogInfo> selectAllLogs(LogInfo pam);
}
