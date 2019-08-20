package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysBulletin;
import com.lb.sys.model.SysBulletinExample;

public interface SysBulletinMapper {
    int countByExample(SysBulletinExample example);

    int deleteByExample(SysBulletinExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysBulletin record);

    int insertSelective(SysBulletin record);

    List<SysBulletin> selectByExample(SysBulletinExample example);

    SysBulletin selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysBulletin record, @Param("example") SysBulletinExample example);

    int updateByExample(@Param("record") SysBulletin record, @Param("example") SysBulletinExample example);

    int updateByPrimaryKeySelective(SysBulletin record);

    int updateByPrimaryKey(SysBulletin record);
    
    List<SysBulletin> querySysBulletinByName(Map<String,Object> title);
}