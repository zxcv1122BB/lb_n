/**
 * 
 */
package com.lb.sign.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sign.service.SignInfoManageService;
import com.lb.sys.dao.SignInfoManageMapper;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SignInfoManageServiceImpl implements SignInfoManageService {

	@Autowired private SignInfoManageMapper signInfoManageMapper;
	
	@Override
	public List<Map<String, Object>> querySignInfoList(Map<String, Object> param) {
		return signInfoManageMapper.querySignInfoList(param);
	}

	@Override
	public int addSignInfo(Map<String, Object> param) {
		return signInfoManageMapper.addSignInfo(param);
	}

	@Override
	public int updateSignInfo(Map<String, Object> param) {
		return signInfoManageMapper.updateSignInfo(param);
	}

	@Override
	public int removeSignInfo(Map<String, Object> param) {
		return signInfoManageMapper.removeSignInfo(param);
	}

	@Override
	public int realyRemoveSignInfo(Map<String, Object> param) {
		return signInfoManageMapper.realyRemoveSignInfo(param);
	}

	@Override
	public Map<String, Object> getUserSignList(Map<String, Object> param) {
		Map<String,Object> resultMap = signInfoManageMapper.getUserSignCount(param);
		if(resultMap != null) {
			List<Map<String,Object>> resultList = signInfoManageMapper.getUserSignList(param);
			resultMap.put("list", resultList);
		}
		return resultMap;
	}

	@Override
	public int getStartedSignCount(Map<String, Object> param) {
		return signInfoManageMapper.getStartedSignCount(param);
	}

}
