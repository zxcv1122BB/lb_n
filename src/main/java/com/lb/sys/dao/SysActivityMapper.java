package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysActivity;
import com.lb.sys.model.SysActivityExample;

public interface SysActivityMapper {
    int countByExample(SysActivityExample example);

    int deleteByExample(SysActivityExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysActivity record);

    int insertSelective(SysActivity record);

    List<SysActivity> selectByExample(SysActivityExample example);

    SysActivity selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysActivity record, @Param("example") SysActivityExample example);

    int updateByExample(@Param("record") SysActivity record, @Param("example") SysActivityExample example);

    int updateByPrimaryKeySelective(SysActivity record);

    int updateByPrimaryKey(SysActivity record);

	List<SysActivity> querSysActivity(Map<String, Object> map);
}