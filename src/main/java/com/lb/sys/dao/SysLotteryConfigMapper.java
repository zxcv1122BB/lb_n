package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysLotteryConfig;
import com.lb.sys.model.SysLotteryConfigExample;

public interface SysLotteryConfigMapper {
    
    long countByExample(SysLotteryConfigExample example);

    int deleteByExample(SysLotteryConfigExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysLotteryConfig record);

    int insertSelective(SysLotteryConfig record);

    List<SysLotteryConfig> selectByExample(SysLotteryConfigExample example);

    SysLotteryConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysLotteryConfig record, @Param("example") SysLotteryConfigExample example);

    int updateByExample(@Param("record") SysLotteryConfig record, @Param("example") SysLotteryConfigExample example);

    int updateByPrimaryKeySelective(SysLotteryConfig record);

    int updateByPrimaryKey(SysLotteryConfig record);

	List<SysLotteryConfig> querySysLotteryConfigList(Map<String, Object> map);
}