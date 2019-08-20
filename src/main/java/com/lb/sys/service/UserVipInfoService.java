package com.lb.sys.service;

import java.util.List;
import java.util.Map;

import com.lb.sys.model.UserVipInfo;
import com.lb.sys.tools.model.Message;


public interface UserVipInfoService {
	/***
	 * 查询vip详情
	 * @return vip分类信息集合
	 */
    List<Map<String,Object>> selectVips();
    
    /****
     * 删除一个vip等级分组
     * @param vipId
     * @return 操作结果对象
     */
    Message deleteVip(Integer vipId);
    
    /***
     * 禁用一个vip等级信息  或者开启一个vip等级信息
     * @param map  参数集合
     * @return  操作结果对象
     */
    Message upVip(Map<String,Integer> map);
    
    /***
     * 修改vip的基本信息
     * @param record  要修改的信息
     * @return  操作结果对象
     */
    Message updateVIP(UserVipInfo record);
    
    /***
     * 查询某一个状态下的所有vip分组信息
     * @param statu  状态码
     * @return   查询到的结果集
     */
    List<UserVipInfo> selectAllVipByS0(Integer statu);
    /***
     * 添加一个会员等级信息
     * @param map参数集合
     * @return
     */
    Message addUserVip(UserVipInfo map);
    /***
     * 修改会员的等级信息 
     * @param map 参数集合
     * @return 
     */
    Message updateUserVip(Map<String,Object> map);
    /***
     * 根据编号查询
     * @param vipId  vip编号
     * @return
     */
    UserVipInfo selectbyId(Integer vipId);
    
}
