package com.lb.sys.service;

import java.util.List;

import com.lb.sys.pojo.SysRegisterOptionPojo;

public interface ISysRegisterOptionService {
	int deleteByPrimaryKey(Integer id);

	int insert(SysRegisterOptionPojo record);

	int insertSelective(SysRegisterOptionPojo record);

	SysRegisterOptionPojo selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysRegisterOptionPojo record);

	int updateByPrimaryKey(SysRegisterOptionPojo record);
	
	List<SysRegisterOptionPojo> querySysRegisterOptionByTypeId(Integer register_type);
	    
    int isOnlyValidate(String type,String value);
}
