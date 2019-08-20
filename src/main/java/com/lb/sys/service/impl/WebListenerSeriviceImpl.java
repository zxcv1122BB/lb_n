package com.lb.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.WebListenerMapper;
import com.lb.sys.service.IWebListenerSerivice;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class WebListenerSeriviceImpl implements IWebListenerSerivice{

	@Autowired
	private WebListenerMapper webListenerMapper;
	
	@Override
	public Map<String,Object> selectWebList(String webName) {
		
		return webListenerMapper.selectWebList(webName);
	}

	@Override
	public List<Map<String, Object>> selectWebTitleList() {
		
		return webListenerMapper.selectWebTitleList();
	}

}
