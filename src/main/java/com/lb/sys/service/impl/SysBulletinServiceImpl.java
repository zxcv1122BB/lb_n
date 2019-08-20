package com.lb.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.SysBulletinMapper;
import com.lb.sys.model.SysBulletin;
import com.lb.sys.model.SysBulletinExample;
import com.lb.sys.service.ISysBulletinService;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysBulletinServiceImpl implements ISysBulletinService {
	
	private final Log LOGGER = LogFactory.getLog(SysBulletinServiceImpl.class);

	@Autowired
	private SysBulletinMapper sysBulletinMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		SysBulletin sysBulletin = new SysBulletin();
		sysBulletin.setId(id);
		sysBulletin.setDelStatus(Byte.valueOf("0"));
		sysBulletin.setUpdateDate(new Date());
		return sysBulletinMapper.updateByPrimaryKeySelective(sysBulletin);
	}

	@Override
	public int insert(SysBulletin record) {
		record.setCreateDate(new Date());
		return sysBulletinMapper.insert(record);
	}

	@Override
	public int insertSelective(SysBulletin record) {
		record.setCreateDate(new Date());
		
		return sysBulletinMapper.insertSelective(record);
	}

	@Override
	public SysBulletin selectByPrimaryKey(Long id) {
		SysBulletin sysBulletin = sysBulletinMapper.selectByPrimaryKey(id);
		if(sysBulletin!=null && "1".equals(String.valueOf(sysBulletin.getDelStatus()))) {
			return sysBulletin;
		}else {
			return null;
		}
	}

	@Override
	public int updateByPrimaryKeySelective(SysBulletin record) {
		record.setUpdateDate(new Date());
		return sysBulletinMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SysBulletin record) {
		record.setUpdateDate(new Date());
		return sysBulletinMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SysBulletin> selectSysBulletinList() {
		SysBulletinExample example = new SysBulletinExample();
		example.setOrderByClause("create_date DESC");
		example.createCriteria().andDelStatusEqualTo(Byte.valueOf("1"));
		return sysBulletinMapper.selectByExample(example);
	}

	//批量删除
	@Override
	public int batchSysMessageByid(List<String> ids) {
		List<Long> listIds = new ArrayList<>();
		if(ids!=null && ids.size()>0) {
			for (String id : ids) {
				if(id!=null) {
					listIds.add(Long.parseLong(id));
				}
			}
			SysBulletin record = new SysBulletin();
			record.setDelStatus(Byte.valueOf("0"));
			SysBulletinExample example = new SysBulletinExample();
			example.createCriteria().andIdIn(listIds);
			return sysBulletinMapper.updateByExampleSelective(record, example);
		}else {
			LOGGER.error("批量删除失败");
			return -1;
		}
	}
	
	@Override
	public List<SysBulletin> selectByTitle(Map<String, Object> map) {
		List<SysBulletin> sysBulletList = sysBulletinMapper.querySysBulletinByName(map);
		return sysBulletList;
	}

}
