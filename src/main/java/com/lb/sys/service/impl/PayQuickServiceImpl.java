package com.lb.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.PayQuickMapper;
import com.lb.sys.service.IPayQuickService;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class PayQuickServiceImpl implements IPayQuickService {

	@Autowired
	private PayQuickMapper payQuickMapper;
	
	@Override
	public List<Map<String, Object>> queryPayList() {
		
		return payQuickMapper.queryPayList();
	}

	@Override
	public Integer addPayInfo(Map<String, Object> map) {
		
		return payQuickMapper.insertPayQuickType(map);
	}

	@Override
	public Integer delPayInfoByid(Integer id) {
		
		return payQuickMapper.delPayQuickType(id);
	}

	@Override
	public Integer updatePayInfo(Map<String, Object> map) {
		
		return payQuickMapper.updatePayQuickTypeByid(map);
	}

	
}
