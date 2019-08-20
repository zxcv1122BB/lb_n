package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysMessage;
import com.lb.sys.model.SysMessageExample;

public interface SysMessageMapper {
    int countByExample(SysMessageExample example);

    int deleteByExample(SysMessageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysMessage record);

    int insertSelective(SysMessage record);

    List<SysMessage> selectByExample(SysMessageExample example);

    SysMessage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysMessage record, @Param("example") SysMessageExample example);

    int updateByExample(@Param("record") SysMessage record, @Param("example") SysMessageExample example);

    int updateByPrimaryKeySelective(SysMessage record);

    int updateByPrimaryKey(SysMessage record);

    //删除记录表中的内容
	int updateSysMessageDelStatus(Map<String,Object> map);
	
	//单发时根据messageid更新记录表中的内容
	int updateByMessageId(SysMessage sysMessage);
	
	//单发时根据messageid更新记录表中的内容
	SysMessage selectByMessageId(Long messageId);
	
	
	//添加一条推送记录
	int tuiOverAdd(@Param("message")SysMessage sysMessage);
	
}