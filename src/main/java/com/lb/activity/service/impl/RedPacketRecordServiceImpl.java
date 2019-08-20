package com.lb.activity.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.activity.service.IRedPacketRecordService;
import com.lb.sys.dao.RedPacketRecordMapper;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class RedPacketRecordServiceImpl implements IRedPacketRecordService{
	
	@Autowired
	private RedPacketRecordMapper redPacketRecordMapper;

	@Override
	public List<Map<String, Object>> queryRedPacketRecordList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return redPacketRecordMapper.queryRedPacketRecordList(map);
	}

	
	
}
