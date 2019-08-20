package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.report.model.GlobalCount;
import com.lb.report.model.TeamCount;

public interface ProxyDistributionMapper {

	/**
	 * 新增代理商分销信息
	 * @param paramMap 参数
	 * @return
	 */
    int insert(Map<String,Object> paramMap);
    
    /**
     * 查询代理商分销情况信息
     * @param paramMap 参数
     * @return
     */
    List<Map<String,Object>> findProxyDistributionInfo(Map<String,Object> paramMap);
    
    /**
     * 多级代理返点记录
     * @return
     */
    List<Map<String,Object>> getAgentRebateList(Map<String,Object> map);
    
    /**
     * 更新代理商分销状态或时间信息
     * @param paramMap 参数  主键id、状态、时间
     * @return
     */
    int updateProxyStatus(@Param("id")Integer id,@Param("status")String status,@Param("rebatesTime") String rebatesTime);

	/**
	 * rollBackProxyDistribution
	 * 回滚时表更改ProxyDistribution
	 */
	Integer rollBackProxyDistribution(Map<String, Object> map);

	Map<String, Object> queryCountGK();

	List<Map<String,Object>> queryTeamCount(Map<String, Object> map);

	int isExsitAgency(@Param("agentCount")String agentCount);

	Map<String, Object> getCoinCount(Map<String, Object> map);

	Map<String, Object> getBetsCount(Map<String, Object> map);

	Map<String, Object> getWithDrawCount(Map<String, Object> map);

	TeamCount queryTotalTeamCount(Map<String, Object> map);

	GlobalCount queryGlobalCount(Map<String, Object> map);
	GlobalCount queryLsHistoryGlobalCount(Map<String, Object> map);
	
	Map<String, Object> queryHomeTodayReports();
	
	void queryTodayTeamReports();

}