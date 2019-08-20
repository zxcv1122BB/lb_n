package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lb.download.model.UserDownload;
import com.lb.report.model.UserCount;
import com.lb.sys.model.MemeberJsonResult;
import com.lb.sys.model.UserModel;
import com.lb.sys.model.UserVipModel;
@Mapper
public interface UserModelMapper {
	/**
	 * 查询会员数据概况
	 * @param parmMap   USER_NAME会员名称
	 * @return   查询到的会员概况信息
	 */
	UserCount selectUserDate(Map<String,Object> parmMap);
	UserCount selectLsHistoryUserDate(Map<String,Object> parmMap);
	/**
	 * 查询会员数据概况
	 * @param parmMap   USER_NAME会员名称
	 * @return  统计条数
	 */
	int selectUserCountByUserName (Map<String,Object> parmMap);
	
	
    //查询所有的会员信息
    List<UserModel> selectAllUser();
    List<Map<String,Object>> selectBankType();
    //导出excel表  
    //List<UserModel> exportUser_Info(String arrValue);
    List<UserModel> exportUser_Info(@Param("VIP_Id") Integer VIP_Id);
    //模糊查询
    List<UserModel> selectLike(@Param("userModel") UserModel userModel);
    
    int updateStatc(@Param("user") UserModel user);
    
	List<UserModel> queryUserByUserName(@Param("userName") String userName);
	
	int updateCoin(Map<String, Object> usermap);
	
	int updateBetsum(@Param("user") UserModel userModel); 
	
	//查询某一个vip类别下的所有会员  selectVipByUser
	List<UserModel> selectVipByUser(Map<String, Object> map);
	
	List<String> queryUserNameByids(List<Long> ids);
	
	List<UserModel> queryUsername(Map<String,Object> parame);
	
	/**
	 * 修改用户信息
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(UserModel record);
	
	
	UserModel selectCiDbYuID(Integer uid);
	
	List<UserVipModel> queryUserList(Map<String, Object> map);
	
	List<MemeberJsonResult> getMemberList();
	
	List<MemeberJsonResult> getKeyList();
	
	int updateVipUser(Map<String, Object> bean);
	
	int updateVipUser1(Map<String, Object> bean);
	
	UserVipModel getUserByUserName(Map<String, Object> map);
	
	List<MemeberJsonResult> getProxyList(@Param("proxyName") String proxyName);
	
	int insertVipUser(UserVipModel model);
	
	int checkUsername(@Param("username") String username);
	
	List<Map<String, Object>> getOperationUserList(Map<String, Object> param);
	
	List<MemeberJsonResult> getAllProxyList();
	Map<String, Object> queryUserBetsum(String userName);
	
	//查询导出的时候需要返回的值exportUserList
	List<UserDownload> exportUserList(Map<String,Object> parame);
	
	int registerUserAfter(Map<String, Object> map);
	
	Map<String,Object> getRegisterId(Map<String, Object> map);
	
	/**查询VIP级别和VIP方案、当月充值总额**/
	List<Map<String,Object>> queryVIPGroup();	   
    String queryDepositSUM(String uid);
    List<Map<String, Object>> queryDepositGiveScheme();
    
    List<Map<String, Object>> getRebateConfigList();
    
    Map<String, Object> getProxyRebate(Map<String,Object> map);
    
    int addInvitateInfo(Map<String,Object> map);

    int removeInvitateInfo(Map<String,Object> map);
    
    List<Map<String, Object>> queryInvitateInfoList(Map<String, Object> map);
    /**
     * [批量投注返利回滚]——修改用户余额
     * @param paramList
     * @return
     */
    int updateRollBackCoin(List<Map<String, Object>> paramList);
    
    /**
     * [批量投注返利回滚]——批量查询用户信息
     * @param map
     * @return
     
    List<Map<String, Object>> batchQryUserInfo(List<Map<String, Object>> list);
    
    */
    
    /**
     * 根据代理id查询下级(同级)最大的代理编号
     * @return
     */
    String qryMaxAutoSortUidByAgentId(@Param("proxyId")String proxyId);
}