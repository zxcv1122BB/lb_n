package com.lb.activity.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.activity.model.BigTurntable;
import com.lb.activity.model.BigTurntableExample;
import com.lb.activity.model.BigTurntableExample.Criteria;
import com.lb.activity.model.BigTurntableRecord;
import com.lb.activity.model.BigTurntableRecordExample;
import com.lb.activity.model.TurntablePrize;
import com.lb.activity.model.TurntablePrizeExample;
import com.lb.activity.service.IBigTurntableService;
import com.lb.sys.dao.BigTurntableMapper;
import com.lb.sys.dao.BigTurntableRecordMapper;
import com.lb.sys.dao.TurntablePrizeMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserModel;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.model.Message;
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class BigTurntableServiceImpl implements IBigTurntableService{
	
	private final Log LOGGER = LogFactory.getLog(BigTurntableServiceImpl.class);
	
	@Autowired
	private BigTurntableMapper bigTurntableMapper;
	@Autowired
	private UserModelService userService;
	@Autowired
	private TurntablePrizeMapper turntablePrizeMapper;
	@Autowired
	private BigTurntableRecordMapper bigTurntableRecordMapper;

	
	@Override
	public Message isStartBigTurntable(HttpServletRequest request, Integer bid, Byte state) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		Message message =new Message();
		//设置更新属性
		BigTurntable selectByPrimaryKey = bigTurntableMapper.selectByPrimaryKey(bid);
		selectByPrimaryKey.setState(state);
		selectByPrimaryKey.setUpdateDate(new Date());
		if(sysUser!=null) {
			selectByPrimaryKey.setUpdateUser(sysUser.getUserName());
		}
		int update= bigTurntableMapper.updateByPrimaryKeySelective(selectByPrimaryKey);
		if(update>0) {
			message.setCode(200);
			message.setMsg("启用成功");
		}else {
			message.setCode(322);
			message.setMsg("大转盘启用操作失败");
			LOGGER.error("大转盘启用操作失败");
		}
		return message;
	}

	@Override
	public Message deleteBigTurntable(HttpServletRequest request, Integer bid) {
		Message message =new Message();
		int delete = bigTurntableMapper.deleteByPrimaryKey(bid);
		if(delete>0) {
			message.setCode(200);
			message.setMsg("删除成功");
		}else {
			message.setCode(322);
			message.setMsg("大转盘删除失败");
			LOGGER.info("大转盘删除失败");
		}
		return message;
	}

	@Override
	public Message updateBigTurntable(HttpServletRequest request, BigTurntable bigTurntable) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null) {
			bigTurntable.setUpdateUser(sysUser.getUserName());
		}
		//设置更新属性
		bigTurntable.setUpdateDate(new Date());
		int update= bigTurntableMapper.updateByPrimaryKeySelective(bigTurntable);
		if(update>0) {
			message.setCode(200);
			message.setMsg("更新成功");
		}else {
			message.setCode(321);
			message.setMsg("大转盘编辑失败");
			LOGGER.info("大转盘编辑失败");
		}
		return message;
	}

	@Override
	public Map<String, Object> queryBigTurntableById(Integer bid) {
		return bigTurntableMapper.queryBigTurntableById(bid);
	}

	@Override
	public Message addBigTurntable(HttpServletRequest request,Map<String, Object> map) {
		Message message =new Message();
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		if(sysUser!=null) {
			map.put("createdUser", sysUser.getUserName());
		}
		int insertSelective = bigTurntableMapper.insertSelective(map);
		if(insertSelective>0) {
			message.setCode(200);
			message.setMsg("添加成功");
		}else {
			message.setCode(320);
			message.setMsg("大转盘添加失败");
			LOGGER.info("大转盘添加失败");
		}
		return message;
	}

	@Override
	public List<Map<String, Object>> queryBigTurntableList() {
		return bigTurntableMapper.queryBigTurntableList();
	}

	@Override
	public List<TurntablePrize> isExistBigTurntable(Map<String, Object> map) {
		String userName = String.valueOf(map.get("userName"));
		if(userName!=null&&!"".equals(userName)) {
			//通过会员账号得到会员信息
			UserModel userModel = userService.queryUserByUserName(userName);
			if(userModel!=null) {
				//获取会员等级id
				Integer vipId = userModel.getVipId();
				//根据会员等级查询大转盘
				BigTurntable bigTurntable= getBigTurntableByVipId(vipId);
				//判断该会员是否能玩大转盘抽奖
				if(bigTurntable!=null) {
					List<TurntablePrize> list =getTurntablePrizeByBigTurntableId(bigTurntable.getBid());
					return list;
				}
			}
		}
		return null;
	}
	//根据大转盘id查询转盘奖励
	private List<TurntablePrize> getTurntablePrizeByBigTurntableId(Integer bid) {
		TurntablePrizeExample example = new TurntablePrizeExample();
		com.lb.activity.model.TurntablePrizeExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andTurntableIdEqualTo(bid);
		createCriteria.andStateEqualTo(Byte.valueOf("1"));
		List<TurntablePrize> TurntablePrizeList = turntablePrizeMapper.selectByExample(example);
		return TurntablePrizeList;
	}

	//根据会员等级查询大转盘
	private BigTurntable getBigTurntableByVipId(Integer vipId) {
		BigTurntableExample example = new BigTurntableExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andStateEqualTo(Byte.valueOf("1"));
		createCriteria.andVipIdEqualTo(vipId);
		List<BigTurntable> list = bigTurntableMapper.selectByExample(example);
		if(list!=null&&!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public synchronized Message getTurntablePrize(HttpServletRequest request, Map<String, Object> map) {
		//得到前端传来的大转盘id，账号
		Integer bid = Integer.valueOf(map.get("bid").toString());
		 String userName = String.valueOf(map.get("userName").toString());
		 if(userName!=null&&!"".equals(userName)&&bid!=null&&!"".equals(bid)) {
			 //通过会员账号得到会员信息
			 UserModel userModel = userService.queryUserByUserName(userName);
			 //根据大转盘id查询转盘奖励
			 List<TurntablePrize> list =getTurntablePrizeByBigTurntableId(bid);
			 //抽奖，获得奖品信息
			 TurntablePrize turntablePrize = getPrize(list); 
			 //添加大转盘记录
			 addBigTurntableRecord(request, userModel, turntablePrize);
			 
			 return new Message(200,"success",turntablePrize);
		}
		return null;
	}

	private void addBigTurntableRecord(HttpServletRequest request, UserModel userModel, TurntablePrize turntablePrize) {
		 BigTurntableRecord bigTurntableRecord = new BigTurntableRecord();
		 bigTurntableRecord.setCreateTime(new Date());
		 bigTurntableRecord.setIp(request.getRemoteAddr());
		 bigTurntableRecord.setPrizeContent(turntablePrize.getPrizeContent());
		 bigTurntableRecord.setPrizeId(turntablePrize.getId());
		 bigTurntableRecord.setTurntableId(turntablePrize.getTurntableId());
		 bigTurntableRecord.setUserName(userModel.getName());
		 bigTurntableRecord.setUid(userModel.getUid());
		 bigTurntableRecord.setState(Byte.valueOf("1"));
		 //通过转盘奖励里面的大转盘id得到当前大转盘信息
		 BigTurntable bigTurntable = bigTurntableMapper.selectByPrimaryKey(turntablePrize.getTurntableId());
		 bigTurntableRecord.setBigTurntableTitle(bigTurntable.getBigTurntableTitle());
		//添加大转盘记录
		 bigTurntableRecordMapper.insert(bigTurntableRecord);
	}

	private List<BigTurntableRecord> getBigTurntableRecordByPrizeId(Integer id) {
		BigTurntableRecordExample example = new BigTurntableRecordExample();
		com.lb.activity.model.BigTurntableRecordExample.Criteria createCriteria = example.createCriteria();
		createCriteria.andPrizeIdEqualTo(id);
		List<BigTurntableRecord> list = bigTurntableRecordMapper.selectByExample(example);
		return list;
	}

	private TurntablePrize getPrize(List<TurntablePrize> prizes) {
		  TurntablePrize result = null;
		  try {
			  int sumWeight = 0;
			  for(TurntablePrize p : prizes){
				  sumWeight += p.getPrizeWeight();
			  }
			  for(int i=0;i<prizes.size();i++){//概率数组循环 
				  int randomNum = new Random().nextInt(sumWeight);//随机生成0到sumWeight-1的整数
				  if(randomNum<prizes.get(i).getPrizeWeight()){//中奖
					  result =prizes.get(i);
					  break;
				  }else{
					  sumWeight -=prizes.get(i).getPrizeWeight();
				  }
			  }
			  //判断得到的result是否为空，若为空递归调用
			  if(result!=null) {
				//判断奖励次数是否有限制
				 if(result.getPrizeNum()!=null&&!"".equals(result.getPrizeNum())) {
					 List<BigTurntableRecord> recordlist = getBigTurntableRecordByPrizeId(result.getId());
					 int recordNum=0;
					 if(recordlist!=null) {
						 recordNum=recordlist.size();
					 }
					 //判断库存，recordNum为已经抽取的数量
					 if(result.getPrizeNum()==recordNum) {
						 getPrize(prizes);
					 }
				 }
			  }else {
				  getPrize(prizes);
			  }
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	      return result;
	}

	@Override
	public long queryBigTurntableVip(Integer vipId) {
		BigTurntableExample example = new BigTurntableExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andVipIdEqualTo(vipId);
		return bigTurntableMapper.countByExample(example);
	}
}
