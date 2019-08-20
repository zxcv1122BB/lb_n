package com.lb.sys.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.SysAdvPictureMapper;
import com.lb.sys.model.SysAdvPicture;
import com.lb.sys.model.SysAdvPictureExample;
import com.lb.sys.service.ISysAdvPictureService;

/**
 * 轮播图片的serviceImpl
 * @author jiangheng
 *
 */
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysAdvPictureServiceImpl implements ISysAdvPictureService {

	private final Log LOGGER = LogFactory.getLog(SysAdvPictureServiceImpl.class);
	
	@Autowired
	private SysAdvPictureMapper sysAdvPictureMapper;
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		SysAdvPicture sysAdvPicture = new SysAdvPicture();
		sysAdvPicture.setId(id);
		sysAdvPicture.setDelStatus(Byte.valueOf("0"));
		sysAdvPicture.setUpdateDate(new Date());
		return sysAdvPictureMapper.updateByPrimaryKeySelective(sysAdvPicture);
	}

	@Override
	public int insert(SysAdvPicture record) {
		record.setCreateDate(new Date());
		return sysAdvPictureMapper.insert(record);
	}

	@Override
	public int insertSelective(SysAdvPicture record) {
		record.setCreateDate(new Date());
		return sysAdvPictureMapper.insertSelective(record);
	}

	@Override
	public SysAdvPicture selectByPrimaryKey(Long id) {
		SysAdvPicture sysAdvPicture = sysAdvPictureMapper.selectByPrimaryKey(id);
		if(sysAdvPicture!=null&&"1".equals(String.valueOf(sysAdvPicture.getDelStatus()))) {
			return sysAdvPicture;
		}else {
			return null;
		}
	}

	@Override
	public int updateByPrimaryKeySelective(SysAdvPicture record) {
		record.setUpdateDate(new Date());
		return sysAdvPictureMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SysAdvPicture record) {
		record.setUpdateDate(new Date());
		return sysAdvPictureMapper.updateByPrimaryKey(record);
	}

	@Override
	public List<SysAdvPicture> selectSysAdvPictureList() {
		SysAdvPictureExample example = new SysAdvPictureExample();
		example.setOrderByClause("priority ASC , create_date desc");
		example.createCriteria().andDelStatusEqualTo(Byte.valueOf("1"));
		return sysAdvPictureMapper.selectByExample(example);
	}

	@Override
	public int batchSysAdvPictureByid(List<String> ids) {
		List<Long> listIds = new ArrayList<>();
		if(ids!=null && ids.size()>0) {
			for (String id : ids) {
				if(id!=null) {
					listIds.add(Long.parseLong(id));
				}
			}
			
			SysAdvPicture record = new SysAdvPicture();
			record.setDelStatus(Byte.valueOf("0"));
			SysAdvPictureExample example = new SysAdvPictureExample();
			example.createCriteria().andIdIn(listIds);
			return sysAdvPictureMapper.updateByExampleSelective(record, example);
		}else {
			LOGGER.error("批量删除失败");
			return -1;
		}
	}

}
