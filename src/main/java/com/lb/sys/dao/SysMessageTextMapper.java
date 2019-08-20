package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysMessageText;
import com.lb.sys.model.SysMessageTextExample;

public interface SysMessageTextMapper {
    int countByExample(SysMessageTextExample example);

    int deleteByExample(SysMessageTextExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysMessageText record);

    int insertSelective(SysMessageText record);

    List<SysMessageText> selectByExample(SysMessageTextExample example);

    SysMessageText selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysMessageText record, @Param("example") SysMessageTextExample example);

    int updateByExample(@Param("record") SysMessageText record, @Param("example") SysMessageTextExample example);

    int updateByPrimaryKeySelective(SysMessageText record);

    int updateByPrimaryKey(SysMessageText record);
    
    List<SysMessageText> querySysMessageTextByName(Map<String,Object> map);
    
    //查询所有需要推送的消息
    List<SysMessageText> selectAllContexts(Map<String,Object> map);
}