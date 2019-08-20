/**
 * 
 */
package com.lb.openprize.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lb.openprize.service.IDigitalOpenService;
import com.lb.sys.dao.DigitalOpenMapper;

/**
 * @describe 
 * @date 2017年11月15日
 */
@Service
public class DigitalOpenServiceImpl implements IDigitalOpenService {

	@Autowired
	private DigitalOpenMapper openMapper;
	
	@Override
	public int insertDigitalOpenData(Map<String, Object> paramterMap) {
		return openMapper.insertDigitalOpenData(paramterMap);
	}
 
}
