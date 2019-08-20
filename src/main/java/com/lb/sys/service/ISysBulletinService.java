package com.lb.sys.service;

import java.util.List;
import java.util.Map;

import com.lb.sys.model.SysBulletin;

/**
 * @author jiangheng
 *公告service
 */
public interface ISysBulletinService {

	int deleteByPrimaryKey(Long id);

	int insert(SysBulletin record);

	int insertSelective(SysBulletin record);

	SysBulletin selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SysBulletin record);

	int updateByPrimaryKey(SysBulletin record);

	List<SysBulletin> selectSysBulletinList();

	int batchSysMessageByid(List<String> ids);
	
	List<SysBulletin> selectByTitle(Map<String, Object> map);
}
