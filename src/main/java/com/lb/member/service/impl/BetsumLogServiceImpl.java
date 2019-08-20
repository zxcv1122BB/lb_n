package com.lb.member.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.lb.member.model.BetsumLog;
import com.lb.member.service.IBetsumLogService;
import com.lb.sys.dao.BetsumLogMapper;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserModel;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class BetsumLogServiceImpl implements IBetsumLogService{
	
	private final Log LOGGER = LogFactory.getLog(BetsumLogServiceImpl.class);
	
	@Autowired
	private BetsumLogMapper betsumLogMapper;
	@Autowired
	private UserModelMapper userMapper;
	@Autowired
	private UserModelService userService;
	
	@Override
	public List<Map<String, Object>> queryBetsumLogList(Map<String, Object> map) {
		return betsumLogMapper.queryBetsumLogList(map);
	}
	@Override
	public Message addAndSubtractCode(HttpServletRequest request, Map<String, Object> map) {
		try {
			//加减款判断
			Byte betsumOperateType = Byte.valueOf(String.valueOf(map.get("betsumOperateType")));
			BigDecimal betsum=new BigDecimal(""+map.get("betsum"));
			//查询会员信息
			String userName = String.valueOf(map.get("userName"));
			UserModel userModel=userService.queryUserByUserName(userName);
			//设置资金流动记录表
			BetsumLog betsumLog = new BetsumLog();
			betsumLog.setBetsumBefore(userModel.getWithdrawNeedsum());
			betsumLog.setUid(userModel.getUid());
			betsumLog.setUserName(userModel.getUserName());
			betsumLog.setBetsumOperateType(betsumOperateType);
			betsumLog.setInfo(String.valueOf(map.get("info")));
			//判断加扣款
			if(betsumOperateType.equals(Byte.valueOf("7"))) {
				if(userModel.getWithdrawNeedsum().compareTo(betsum)<0) {
					throw new RuntimeException("所减打码量不能大于会员的提款所需打码量");
				}
				betsumLog.setUserBetsum(userModel.getWithdrawNeedsum().subtract(betsum));
				betsumLog.setBetsumAmount(betsum);
				userModel.setWithdrawNeedsum(new BigDecimal("-"+betsum));
			}else if(betsumOperateType.equals(Byte.valueOf("6"))){
				if((userModel.getWithdrawNeedsum().add(betsum)).compareTo(new BigDecimal("100000000"))>=0) {
					throw new RuntimeException("加打码量过多，该会员打码量将超过上限100000000");
				}
				betsumLog.setUserBetsum(userModel.getWithdrawNeedsum().add(betsum));
				betsumLog.setBetsumAmount(betsum);
				userModel.setWithdrawNeedsum(betsum);
			}
			//插入资金流动记录表
			
			int insert=insertAddAndSubtractMoney(request,betsumLog);
			if(insert<=0) {
				throw new RuntimeException("打码量操作失败");
			}
			//更新会员账户资金
			insert=userMapper.updateBetsum(userModel);
			if(insert<=0) {
				throw new RuntimeException("打码量操作失败");
			}
			return new Message(200,"打码量操作成功");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
			e.printStackTrace();
			LOGGER.error("打码量操作失败");
			return new Message(331,e.getMessage());
		}
	}
	private int insertAddAndSubtractMoney(HttpServletRequest request, BetsumLog betsumLog) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		betsumLog.setOperateTime(new Date());
		if(sysUser!=null) {
			betsumLog.setOperateUid(sysUser.getUserId());
			betsumLog.setOperateUser(sysUser.getUserName());
		}
		betsumLog.setOperateIp(request.getRemoteAddr());
		int insertSelective = betsumLogMapper.insertSelective(betsumLog);
		if(insertSelective>0) {
		}else {
			LOGGER.error("后台添加打码量记录失败");
		}
		return insertSelective;
	}
}
