package com.lb.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.SysMessageMapper;
import com.lb.sys.model.SysMessage;
import com.lb.sys.service.SysMessageService;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysMessageServiceImpl implements SysMessageService {
    @Autowired
    private SysMessageMapper sysMessageMapper; 
	@Override
	public int tuiOverAdd(SysMessage sysMessage) {
		return sysMessageMapper.tuiOverAdd(sysMessage);
	}

}
