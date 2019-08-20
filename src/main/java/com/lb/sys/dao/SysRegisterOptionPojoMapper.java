package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.pojo.SysRegisterOptionPojo;

public interface SysRegisterOptionPojoMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(SysRegisterOptionPojo record);

	int insertSelective(SysRegisterOptionPojo record);

	SysRegisterOptionPojo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysRegisterOptionPojo record);

	int updateByPrimaryKey(SysRegisterOptionPojo record);
	
	List<SysRegisterOptionPojo> querySysRegisterOptionByTypeId(@Param("register_type") Integer register_type);
	    
    int isOnlyValidate(@Param("type") String type,@Param("value") String value);
}