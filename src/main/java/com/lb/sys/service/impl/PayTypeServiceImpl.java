package com.lb.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.PayTypeMapper;
import com.lb.sys.service.IPayTypeService;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class PayTypeServiceImpl implements IPayTypeService {
	@Autowired
	private PayTypeMapper payTypeMapper;
	
	@Override
	public List<Map<String, Object>> queryPayList(String payType) {
		return payTypeMapper.selectPayList(payType);
	}

	@Override
	public Integer addPayType(Map<String, Object> map) {
		return payTypeMapper.insertPayType(map);
	}

	@Override
	public Integer delPayTypeByid(String id) {
		return payTypeMapper.delPayTypeByid(id);
	}

	@Override
	public Integer updatePayType(Map<String, Object> map) {
		return payTypeMapper.updatePayType(map);
	}

}
