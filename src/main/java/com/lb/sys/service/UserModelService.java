package com.lb.sys.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lb.download.model.UserDownload;
import com.lb.sys.model.MemeberJsonResult;
import com.lb.sys.model.UserModel;
import com.lb.sys.model.UserVipModel;

/********
 * 会员管理业务逻辑接口层
 * 
 * @author ASUS
 *
 */
public interface UserModelService {
	/**
	 * 查询所有的会员信息
	 * 
	 * @param example
	 * @return
	 */
	
    List<UserDownload> queryUserListDownl(Map<String, Object> map) ;
	
	List<UserModel> selectAllUser();

	List<UserModel> exportUser_Info(Integer parm);

	// 模糊查询
	List<UserModel> selectLike( UserModel um);

	int updateStatc( UserModel user);

	UserModel queryUserByUserName(String userName);

	// 查询某一个vip类别下的所有会员 selectVipByUser
	List<UserModel> selectVipByUser(Map<String, Object> map);
	
	List<Map<String,Object>> selectBankType();
	
	//站内信时查询所有用户名
	List<UserModel> queryUsername();
	
	/**
	 * 修改用户信息
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(UserModel record);

	List<Map<String,Object>> queryAllUsername();
	
	UserModel selectCiDbYuID(Integer uid);

	List<UserVipModel> queryUserList(Map<String, Object> map);
	
	List<MemeberJsonResult> getMemberList();
	
	List<MemeberJsonResult> getKeyList();
	
	int updateVipUser(Map<String, Object> map);
	
	int updateVipUser1(Map<String, Object> map);

	UserVipModel getUserByUserName(Map<String, Object> map);
	
	List<MemeberJsonResult> getProxyList(String proxyName);
	
	int insertVipUser(HttpServletRequest request, UserVipModel model);
	
	int checkUsername(String username);

	List<Map<String, Object>> getOperationUserList(Map<String, Object> param);
	
	List<MemeberJsonResult> getAllProxyList();

	Map<String, Object> queryUserBetsum(String userName);
	
	List<Map<String,Object>> getRebateConfigList();
	
	int addInvitateInfo(Map<String, Object> param);
	
	int removeInvitateInfo(Map<String, Object> param);
	
	List<Map<String, Object>> queryInvitateInfoList(Map<String, Object> param);
	
	
}
