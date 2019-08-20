package com.lb.activity.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.activity.service.IBigTurntableRecordService;
import com.lb.sys.dao.BigTurntableRecordMapper;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class BigTurntableRecordServiceImpl implements IBigTurntableRecordService{
	
	
	@Autowired
	private BigTurntableRecordMapper bigTurntableRecordMapper;

	@Override
	public List<Map<String, Object>> queryBigTurntableRecordList(Map<String, Object> map) {
		return bigTurntableRecordMapper.queryBigTurntableRecordList(map);
	}


}
