package com.lb.sys.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.ProxyInfo;
import com.lb.sys.model.ProxyInfoExample;
import com.lb.sys.model.UserVipModel;

public interface ProxyInfoMapper {
	 
    long countByExample(ProxyInfoExample example);

    int deleteByExample(ProxyInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProxyInfo record);
   
    int insertSelective(ProxyInfo record);

    List<ProxyInfo> selectByExample(ProxyInfoExample example);
   
    ProxyInfo selectByPrimaryKey(Integer id);
    
    int updateByExampleSelective(@Param("record") ProxyInfo record, @Param("example") ProxyInfoExample example);
   
    int updateByExample(@Param("record") ProxyInfo record, @Param("example") ProxyInfoExample example);

    int updateByPrimaryKeySelective(ProxyInfo record);

    int updateByPrimaryKey(ProxyInfo record);
    
    int deleteByProxyInfo(ProxyInfo proxy);

    int updateByStatus(ProxyInfo proxy);

    List<UserVipModel> selectByProxyInfo(Map<String, Object> map);
	
	/**
	 * 根据时间区间查询代理商所发展的会员总投注流水金额、总存款金额、代理商id以及用户id
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> queryProxyInfoUserList(Map<String,Object> paramMap);
	
	/**
	 * 根据当前代理查询当前代理以及上一级代理信息
	 * @param ids
	 * @return
	 */
	Map<String,Object> queryProxyInfoById(@Param("id")Integer id);
	
	/**
	 * 更新代理商个人财产金额信息
	 * @param paramMap 参数：代理商id、金额
	 * @return
	 */
	int updateProxyInfoCoin(@Param("id")Integer id,@Param("coin")BigDecimal coin);
	
	List<Map<String,Object>> getProxyUserList(Map<String,Object> map);
	
	/**
	 * 回滚时操作proxyInfo表
	 */
	Integer rollbackProxyInfo(Map<String,Object> map);
	
	//查询个人的账号余额
	Map<String,Object> queryProxyInfoCoinkByid(Integer id);
	//查询代理信息
	Map<String,Object> queryProxyInfoByUserName(@Param("userName") String userName);

	List<Map<String, Object>> selectProxyInfoAll();

	int checkAccount(Map<String, Object> map);

	int updatePassword(Map<String, Object> map);
	
	String queryProxyIds(@Param("type")Integer type,@Param("uid") Integer uid);
}