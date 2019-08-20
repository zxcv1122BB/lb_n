package com.lb.sys.service;


import java.util.List;

import com.lb.sys.model.SysAdvPicture;

/**
 * 
 * @author jiangheng
 *轮播图片service
 */
public interface ISysAdvPictureService {


	int deleteByPrimaryKey(Long id);

	int insert(SysAdvPicture record);

	int insertSelective(SysAdvPicture record);

	SysAdvPicture selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SysAdvPicture record);

	int updateByPrimaryKey(SysAdvPicture record);

	List<SysAdvPicture> selectSysAdvPictureList();

	int batchSysAdvPictureByid(List<String> ids);
}
