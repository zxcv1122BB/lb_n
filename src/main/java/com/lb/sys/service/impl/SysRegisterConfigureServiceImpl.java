/**
 * 
 */
package com.lb.sys.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.activity.service.impl.RedPacketRecordServiceImpl;
import com.lb.sys.dao.SysRegisterConfigureMapper;
import com.lb.sys.model.SysRegisterConfigure;
import com.lb.sys.service.ISysRegisterConfigureService;

/**
 * @author wz
 * @describe 
 * @date 2017年9月22日
 */
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysRegisterConfigureServiceImpl implements ISysRegisterConfigureService {

	private final Log LOGGER = LogFactory.getLog(RedPacketRecordServiceImpl.class);
	
	@Autowired
	private SysRegisterConfigureMapper sysRegisterConfigureMapper;
	/* (non-Javadoc)
	 * @see com.lb.sys.service.ISysRegisterConfigureService#queryRegisterConfigure(java.lang.Integer)
	 */
	@Override
	public List<SysRegisterConfigure> queryRegisterConfigure(Integer type) {
		if(type == null || type <1 || type>2) {
			LOGGER.info("注册类型错误！默认使用会员注册");
			type = 1;
		}
		return sysRegisterConfigureMapper.queryRegisterConfigure(type);
	}

	/* (non-Javadoc)
	 * @see com.lb.sys.service.ISysRegisterConfigureService#updateRegisterConfigure(com.lb.sys.model.SysRegisterConfigure)
	 */
	@Override
	public int updateRegisterConfigure(SysRegisterConfigure configure) {
		return sysRegisterConfigureMapper.updateRegisterConfigure(configure);
	}

}
