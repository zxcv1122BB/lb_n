package com.lb.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.SysAdvPicture;
import com.lb.sys.model.SysAdvPictureExample;

public interface SysAdvPictureMapper {
    int countByExample(SysAdvPictureExample example);

    int deleteByExample(SysAdvPictureExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SysAdvPicture record);

    int insertSelective(SysAdvPicture record);

    List<SysAdvPicture> selectByExample(SysAdvPictureExample example);

    SysAdvPicture selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SysAdvPicture record, @Param("example") SysAdvPictureExample example);

    int updateByExample(@Param("record") SysAdvPicture record, @Param("example") SysAdvPictureExample example);

    int updateByPrimaryKeySelective(SysAdvPicture record);

    int updateByPrimaryKey(SysAdvPicture record);
}