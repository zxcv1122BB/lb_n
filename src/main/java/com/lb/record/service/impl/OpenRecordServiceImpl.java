package com.lb.record.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.record.service.OpenRecordService;
import com.lb.sys.dao.OpenRecordMapper;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class OpenRecordServiceImpl implements OpenRecordService {

	@Autowired
	private OpenRecordMapper openRecordMapper;

	@Override
	public List<Map<String, Object>> queryOpenRecord(Map<String, Object> map) {
		return openRecordMapper.selectOpenRecord(map);
	}

	@Override
	public List<Map<String, Object>> queryMatchType() {
		return openRecordMapper.selectMatchType();
	}

	@Override
	public List<Map<String, Object>> queryDigitRecord(Map<String, Object> map) {
		return openRecordMapper.selectDigitRecord(map);
	}

	@Override
	public List<Map<String, Object>> queryDigitType() {
		return openRecordMapper.selectDigitType();
	}
}
