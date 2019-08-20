package com.lb.ip.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lb.ip.model.IpWhiteList;
import com.lb.ip.model.IpWhiteListExample;

/***
 * ip配置业务逻辑接口层
 * @author tjr
 */
public interface IpWhiteListService {
	 long countByExample(IpWhiteListExample example);

	    int deleteByExample(IpWhiteListExample example);

	    int deleteByPrimaryKey(Integer id);

	    int insert(IpWhiteList record);

	    int insertSelective(IpWhiteList record);
//	    List<IpWhiteList> selectByExample(IpWhiteListExample example);
	    List<IpWhiteList> selectByExample(Integer stuas);
	    IpWhiteList selectByPrimaryKey(Integer id);
	    
	    Integer selectIpYorN(@Param("ip")String ip);

	    int updateByExampleSelective(IpWhiteList record, @Param("example") IpWhiteListExample example);

	    int updateByExample( IpWhiteList record, @Param("example") IpWhiteListExample example);

	    int updateByPrimaryKeySelective(IpWhiteList record);

	    int updateByPrimaryKey(IpWhiteList record);
	    
	    //逻辑删除，也就是更改Ip配置记录的状态
	    int deleteIp (IpWhiteList ipWhiteList);
	    
	    
	    //验证密码是否正确，是否锁定
	    boolean isLocking(String Uid);
	    
	    public String byDeadline_bet(String dateStr);
	    
}
