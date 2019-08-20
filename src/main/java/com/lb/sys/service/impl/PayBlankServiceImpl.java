package com.lb.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.PayBlankMapper;
import com.lb.sys.service.IPayBlankService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class PayBlankServiceImpl implements IPayBlankService {
	
	@Autowired
	private PayBlankMapper payBlankMapper;

	@Override
	public List<Map<String, Object>> queryPayList() {
		
		return payBlankMapper.queryPayList();
	}

	@Override
	public Integer addPayInfo(Map<String, Object> map) {
		
		return payBlankMapper.insertBlankInfoSelective(map);
	}

	@Override
	public Integer delPayInfoByid(Integer id) {
		
		return payBlankMapper.delPayBlankInfo(id);
	}

	@Override
	public Integer updatePayInfo(Map<String, Object> map) {
		
		return payBlankMapper.updateBlankInfoByid(map);
	}
}
