package com.lb.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lb.sys.model.UserVipInfo;
import com.lb.sys.model.UserVipInfoExample;
import com.lb.sys.tools.model.Message;

public interface UserVipInfoMapper {
	
	int selectCountForGroup(@Param("group")Integer group);
	
	int selectCount(Map<String,Object> map);
	
    long countByExample(UserVipInfoExample example);

    int deleteByExample(UserVipInfoExample example);

    int deleteByPrimaryKey(Integer vipid);

    int insert(UserVipInfo record);

    int insertSelective(UserVipInfo record);

    List<UserVipInfo> selectByExample(UserVipInfoExample example);

    UserVipInfo selectByPrimaryKey(Integer vipid);

    int updateByExampleSelective(@Param("record") UserVipInfo record, @Param("example") UserVipInfoExample example);

    int updateByExample(@Param("record") UserVipInfo record, @Param("example") UserVipInfoExample example);

    int updateByPrimaryKeySelective(UserVipInfo record);

    int updateByPrimaryKey(UserVipInfo record);
    
    //查询vip详情
    List<Map<String,Object>> selectVips();
    //删除一个vip等级分组
    Message deleteVip(@Param("vipId")Integer vipId);
    //禁用一个vip等级信息  或者开启一个vip等级信息
    int upVip(Map<String,Integer> map);
    //查询某一个状态下的所有vip分组信息  selectAllVipByS0
    List<UserVipInfo> selectAllVipByS0(Integer statu);
    //添加一个vip等级
    int addUserVip(@Param("uv")UserVipInfo uv);
    //修改会员的等级信息      updateUserVip
    int updateUserVip(Map<String,Object> map);
    //根据编号查询
    UserVipInfo selectbyId(@Param("vipId")Integer vipId);

	List<String> queryVIpNameByids(List<Integer> vipids);
	
	//获取分组名和id
	List<Map<String,Object>> queryAllVipNames(Map<String,Object> param);
	
    Integer onOffIsUserRebate(String sys_config1);
}