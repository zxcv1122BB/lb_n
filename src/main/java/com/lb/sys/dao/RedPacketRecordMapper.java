package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.activity.model.RedPacketRecord;
import com.lb.activity.model.RedPacketRecordExample;

public interface RedPacketRecordMapper {
    long countByExample(RedPacketRecordExample example);

    int deleteByExample(RedPacketRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RedPacketRecord record);

    int insertSelective(RedPacketRecord record);
    
    List<RedPacketRecord> selectByExample(RedPacketRecordExample example);

    RedPacketRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RedPacketRecord record, @Param("example") RedPacketRecordExample example);

    int updateByExample(@Param("record") RedPacketRecord record, @Param("example") RedPacketRecordExample example);

    int updateByPrimaryKeySelective(RedPacketRecord record);

    int updateByPrimaryKey(RedPacketRecord record);

	List<Map<String, Object>> queryRedPacketRecordList(Map<String, Object> map);
}