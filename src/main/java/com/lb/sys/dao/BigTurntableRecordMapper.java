package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.activity.model.BigTurntableRecord;
import com.lb.activity.model.BigTurntableRecordExample;

public interface BigTurntableRecordMapper {
    long countByExample(BigTurntableRecordExample example);

    int deleteByExample(BigTurntableRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BigTurntableRecord record);

    int insertSelective(BigTurntableRecord record);

    List<BigTurntableRecord> selectByExample(BigTurntableRecordExample example);

    BigTurntableRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BigTurntableRecord record, @Param("example") BigTurntableRecordExample example);

    int updateByExample(@Param("record") BigTurntableRecord record, @Param("example") BigTurntableRecordExample example);

    int updateByPrimaryKeySelective(BigTurntableRecord record);

    int updateByPrimaryKey(BigTurntableRecord record);

	List<Map<String, Object>> queryBigTurntableRecordList(Map<String, Object> map);
}