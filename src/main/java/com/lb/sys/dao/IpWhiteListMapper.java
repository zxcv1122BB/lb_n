package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.ip.model.IpWhiteList;
import com.lb.ip.model.IpWhiteListExample;

public interface IpWhiteListMapper {
    long countByExample(IpWhiteListExample example);

    int deleteByExample(IpWhiteListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(IpWhiteList record);

    int insertSelective(IpWhiteList record);

//    List<IpWhiteList> selectByExample(IpWhiteListExample example);

    List<IpWhiteList> selectByExample(@Param("status")Integer status);
    
    //查询某一个Ip是否已经存在   selectIpYorN
    
    Integer selectIpYorN(@Param("ip")String ip);
    
    IpWhiteList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IpWhiteList record, @Param("example") IpWhiteListExample example);

    int updateByExample(@Param("record") IpWhiteList record, @Param("example") IpWhiteListExample example);

    int updateByPrimaryKeySelective(@Param("record")IpWhiteList record);

    int updateByPrimaryKey(IpWhiteList record);
    
    //逻辑删除，也就是更改Ip配置记录的状态
    int deleteIp (@Param("ipWhiteList")IpWhiteList ipWhiteList);
    
    //查询是否为开启状态
    int on_off(Map<String,Object> parm);
    
    Map<String,Object> byConfigure(Map<String,Object> parm);
    
}