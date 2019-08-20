package com.lb.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lb.sys.dao.SysRegisterOptionPojoMapper;
import com.lb.sys.pojo.SysRegisterOptionPojo;
import com.lb.sys.service.ISysRegisterOptionService;

/**
 * @author wz
 * @describe 
 * @date 2017年9月22日
 */
@Service
public class SysRegisterOptionServiceImpl implements ISysRegisterOptionService {

	@Autowired
	private SysRegisterOptionPojoMapper mapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(SysRegisterOptionPojo record) {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(SysRegisterOptionPojo record) {
		return insertSelective(record);
	}

	@Override
	public SysRegisterOptionPojo selectByPrimaryKey(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(SysRegisterOptionPojo record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SysRegisterOptionPojo record) {
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SysRegisterOptionPojo> querySysRegisterOptionByTypeId(Integer register_type) {
		return mapper.querySysRegisterOptionByTypeId(register_type);
	}

	@Override
	public int isOnlyValidate(String type, String value) {
		return mapper.isOnlyValidate(type, value);
	}

	

}
