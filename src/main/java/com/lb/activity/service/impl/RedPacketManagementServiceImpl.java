package com.lb.activity.service.impl;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.lb.activity.model.RedPacketManagement;
import com.lb.activity.model.RedPacketRecord;
import com.lb.activity.service.IRedPacketManagementService;
import com.lb.redis.JedisClient;
import com.lb.sys.dao.RedPacketManagementMapper;
import com.lb.sys.dao.RedPacketRecordMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserModel;
import com.lb.sys.pojo.RandomRedPacketInfo;
import com.lb.sys.pojo.RedPacketPojo;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.RedPacketUtil;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class RedPacketManagementServiceImpl implements IRedPacketManagementService{
	
	private final Log LOGGER = LogFactory.getLog(RedPacketManagementServiceImpl.class);
	
	@Autowired
	private RedPacketManagementMapper redPacketManagementMapper;
	@Autowired
	private RedPacketRecordMapper redPacketRecordMapper;
	
	@Autowired
	private JedisClient jedisClient;//注入Redis
	
	@Autowired
	private UserModelService userService;
	
	@Override
	public List<Map<String, Object>> queryAllRedPacketManagement() {
		List<Map<String, Object>> list = redPacketManagementMapper.queryAllRedPacketManagement();
		for (Map<String, Object> map : list) {
			List<Integer> vipIdList = new ArrayList<>();	
			String vipIds = String.valueOf(map.get("vipId"));
			if(!StringUtils.isEmpty(vipIds)) {
				String[] splits = vipIds.split(",");
				for (String split : splits) {
					try {
						vipIdList.add(Integer.valueOf(split));
					} catch (Exception e) {
					}
				}
				String vipName=redPacketManagementMapper.getVipName(vipIdList);
				map.put("vipId", vipName);
			}
		}
		return list;
	}

	@Override
	public Message addRedPacketManagement(HttpServletRequest request,Map<String, Object> map) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		map.put("createdUser",sysUser.getUserName());
		int insertSelective = redPacketManagementMapper.insertSelective(map);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(310);
			message.setMsg("红包添加失败");
			LOGGER.error("红包添加失败");
		}
		return message;
	}

	@Override
	public RedPacketManagement queryRedPacketManagementById(Integer redPacketId) {
		return redPacketManagementMapper.selectByPrimaryKey(redPacketId);
	}

	@Override
	public Message updateRedPacketManagement(HttpServletRequest request, RedPacketManagement redPacket) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		//设置更新属性
		redPacket.setUpdateDate(new Date());
		redPacket.setUpdateUser(sysUser.getUserName());
		int update= redPacketManagementMapper.updateByPrimaryKeySelective(redPacket);
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(311);
			message.setMsg("红包编辑失败");
			LOGGER.error("红包编辑失败");
		}
		return message;
	}

	@Override
	public Message deleteRedPacketManagement(HttpServletRequest request, Integer redPacketId) {
		Message message =new Message();
		if(jedisClient.hash_exists("redPacket", "redPacket_"+redPacketId)) {
			jedisClient.hdel("redPacket","redPacket_"+redPacketId);
		}
		int delete = redPacketManagementMapper.deleteByPrimaryKey(redPacketId);
		if(delete>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(312);
			message.setMsg("红包删除失败");
			LOGGER.error("红包删除失败");
		}
		return message;
	}

	@Override
	public Message isStartRedPacketManagement(HttpServletRequest request, Integer redPacketId, Byte state) {
		try {
			/*先判断是否启用*/
			if(state!=null&&"0".equals(state.toString())) {
				return new Message(312,"红包已启用,请不要重复启用");
			}
			SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
			//设置更新属性
			RedPacketManagement selectByPrimaryKey = redPacketManagementMapper.selectByPrimaryKey(redPacketId);
			selectByPrimaryKey.setState(state);
			selectByPrimaryKey.setUpdateDate(new Date());
			selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
			redPacketManagementMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
			//生成随机红包
			RedPacketUtil util = new RedPacketUtil(selectByPrimaryKey.getMoneyMin(),selectByPrimaryKey.getRedPacketMoney()); 
			List<Float> redPacketList = util.splitRedPackets(selectByPrimaryKey.getRedPacketMoney(),selectByPrimaryKey.getRedPacketNum());
			//将随机红包和红包结束时间封装到randomRedPacketInfo中
			selectByPrimaryKey.setList(redPacketList);
			//将红包集合转成json字符串
			Gson gson = new Gson();
			//将selectByPrimaryKey转成json字符串
			String redPacketInfo = gson.toJson(selectByPrimaryKey);
			//将红包管理信息存进Redis
			jedisClient.hset("redPacket","redPacket_"+selectByPrimaryKey.getId(),redPacketInfo);
			//设置随机红包过期时间
			return new Message(200,"红包启用操作成功");
		} catch (Exception e) {
			LOGGER.info("红包启用操作失败");
			return new Message(312,"红包启用操作失败");
		}
	}

	@Override
	public List<RedPacketPojo> isExistRedPacket(Map<String, Object> map) {
		String userName = String.valueOf(map.get("userName"));
		LOGGER.error("zhanghao"+userName);
		if(userName!=null&&!"".equals(userName)) {
			//通过会员账号得到会员信息
			UserModel userModel = userService.queryUserByUserName(userName);
			if(userModel!=null&&!"".equals(userModel)) {
				//获取会员等级id
				Integer vipId = userModel.getVipId();
				//从缓存中获取启用的红包信息
				Map<String, String> hgetAll = jedisClient.hgetAll("redPacket");
				if (!hgetAll.isEmpty()) {
					List<RedPacketPojo> list = new ArrayList<>();
					Gson gson = new Gson();
					for (String value : hgetAll.values()) {
						RedPacketManagement redPacket = gson.fromJson(value, RedPacketManagement.class);
						//会员等级与红包管理中的会员等级是否匹配的标记：0表示不匹配，1表示匹配
						int vipIdBZ=0;
						//切割红包管理中的会员等级
						String vipIds = redPacket.getVipId();
						if(vipIds.contains(",")) {
							String[] splits = vipIds.split(",");
							for (String split : splits) {
								if((vipId+"").equals(split)) {
									vipIdBZ=1;
									break;
								}
							}
						}else {
							if((vipId+"").equals(vipIds)) {
								vipIdBZ=1;
							}
						}
						//判断缓存里面红包信息的会员等级是否匹配
						if(vipIdBZ==1&&new Date().getTime()<=redPacket.getEndTime().getTime()) {
							//得到红包RedPacketPojo，用于抢红包
							RedPacketPojo pojo = new RedPacketPojo("redPacket_"+redPacket.getId(),redPacket.getRedPacketTitle(),redPacket.getRedPacketMoney(),redPacket.getStartTime(),redPacket.getEndTime());
							list.add(pojo);
						}
					}
					return list;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	@Override
	public synchronized Message fetchRedPacket(HttpServletRequest request,Map<String, Object> map) {
		//得到前端传来的红包钥匙
		 String redPacketKey = String.valueOf(map.get("redPacketKey"));
		 String userName = String.valueOf(map.get("userName"));
		//通过会员账号得到会员信息
		 UserModel userModel = userService.queryUserByUserName(userName);
		 if(!redPacketKey.isEmpty()) {
			 //取出Redis里面的红包信息
			 String string =jedisClient.get(redPacketKey);
			 if(!string.isEmpty()) {
				 Gson gson = new Gson();
				 //得到红包信息对象
				 RandomRedPacketInfo randomRedPacketInfo = gson.fromJson(string, RandomRedPacketInfo.class);
				 Date endTime = randomRedPacketInfo.getEndTime();
				 long time = endTime.getTime();
				 long currentTime = new Date().getTime();
				 //判断红包是否结束
				 if(currentTime<=time) {
					 HashedMap recordMap = new HashedMap();
					 recordMap.put("uid", userModel.getUid());
					 recordMap.put("redPacketId", randomRedPacketInfo.getRedPacketManagement().getId());
					 @SuppressWarnings("unchecked")
					 //查询用户领取当前红包的记录
					 List<Map<String, Object>> recordList = redPacketRecordMapper.queryRedPacketRecordList(recordMap);
					 //判断用户是否首次领当前红包
					 if(recordList.size()==0) {
						 //获取限制IP数
						 Integer limitIpNum = randomRedPacketInfo.getRedPacketManagement().getLimitIpNum();
						 HashedMap hashedMap = new HashedMap();
						 hashedMap.put("ip",request.getRemoteAddr());
						 hashedMap.put("endTime", new Date().toString());
						 //获取当天0点的时间
						 Calendar cal = Calendar.getInstance();  
						 cal.set(Calendar.HOUR_OF_DAY, 0);  
						 cal.set(Calendar.SECOND, 0);  
						 cal.set(Calendar.MINUTE, 0);  
						 cal.set(Calendar.MILLISECOND, 0); 
						 //设置开始时间
						 hashedMap.put("startTime", cal.getTime().toString());
						 //查找当前IP当天领取到的红包记录
						 @SuppressWarnings("unchecked")
						 List<Map<String, Object>> queryList = redPacketRecordMapper.queryRedPacketRecordList(hashedMap);
						 Integer ipNum=0;
						 if(!queryList.isEmpty()&&queryList!=null) {
							 ipNum=queryList.size();
						 }
						 if(limitIpNum>ipNum) {
							 List<Float> list =randomRedPacketInfo.getList();
							 //判断红包是否被领完
							 if(list!=null) {
								 //取出红包并从list中删除
								 Float money = list.get(0);
								 list.remove(0);
								 //获取红包的会员等级限制
								 //Byte memberGrade = randomRedPacketInfo.getRedPacketManagement().getMemberGrade();
								 if(list!=null) {
									 //重新设置list
									 randomRedPacketInfo.setList(list);
									 //将红包信息重新转成json字符串
									 String redPacketInfo = gson.toJson(randomRedPacketInfo);
									 //将随机红包存进Redis
									 jedisClient.set(redPacketKey,redPacketInfo);
									 //设置过期时间
									 long expireTime=randomRedPacketInfo.getRedPacketManagement().getEndTime().getTime()-new Date().getTime();
									 if(expireTime%1000==0) {
										 expireTime=expireTime/1000;
									 }else {
										 expireTime=expireTime/1000+1;
									 }
									 jedisClient.expire(redPacketKey,Integer.valueOf(expireTime+""));
								 }else {
									 //删除随机红包信息
									 jedisClient.del(redPacketKey);
									 //得到红包管理id，并删除启用的红包管理
									 String redPacketId = redPacketKey.substring(redPacketKey.indexOf("_")+1);
									 jedisClient.hdel(redPacketKey.substring(0,redPacketKey.indexOf("_")), redPacketId);
								 }
								 
								 setRedPacketRecord(request, randomRedPacketInfo, money, userModel);
								 return new Message(200,"success",money);
							 }
							 return new Message(313,"红包已抢完");
						 }
						 return new Message(313,"当前IP所抢的红包已达上限");
					 }
					 return new Message(313,"请不要重复领取");
				 }
				 return new Message(313,"红包已过时");
			 }
		 }
		return null;
	}

	private void setRedPacketRecord(HttpServletRequest request, RandomRedPacketInfo randomRedPacketInfo, Float money,
			UserModel userModel) {
		RedPacketRecord redPacketRecord = new RedPacketRecord();
		redPacketRecord.setRedPacketId(randomRedPacketInfo.getRedPacketManagement().getId());
		redPacketRecord.setCreateTime(new Date());
		redPacketRecord.setUid(userModel.getUid());
		redPacketRecord.setUserName(userModel.getName());
		redPacketRecord.setRedPacketMoney(money);
		redPacketRecord.setRedPacketTitle(randomRedPacketInfo.getRedPacketManagement().getRedPacketTitle());
		redPacketRecord.setIp(request.getRemoteAddr());
		redPacketRecord.setState(Byte.valueOf("0"));
		redPacketRecordMapper.insertSelective(redPacketRecord);
	}
}
