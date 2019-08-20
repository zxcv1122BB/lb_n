package com.lb.member.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.download.model.DepositRecord;
import com.lb.member.model.MemberDeposit;
import com.lb.member.service.ICoinLogService;
import com.lb.member.service.IMemberDepositService;
import com.lb.sys.dao.BetsumLogMapper;
import com.lb.sys.dao.MemberDepositMapper;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserVipModel;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class MemberDepositServiceImpl implements IMemberDepositService{
	
	private final Log LOGGER = LogFactory.getLog(MemberDepositServiceImpl.class);
	
	@Autowired
	private MemberDepositMapper memberDepositMapper;
	@Autowired
	private ICoinLogService coinLogService;
	@Autowired
	private UserModelMapper userMapper;
	@Autowired
	private BetsumLogMapper betsumLogMapper;
	
	@Override
	public List<Map<String, Object>> queryMemberDepositList(Map<String, Object> map) {
		return memberDepositMapper.queryMemberDepositList(map);
	}

	@Override
	public Message depositHandle(HttpServletRequest request,Map<String, Object> map) {
		if(StringUtil.isBlank(map.get("amount"))) {
			LOGGER.error("[充值失败]:"+map.entrySet());
			return new Message(306,"金额参数校验失败");
		}
		Map<String, Object> memberDepositMap= memberDepositMapper.queryById(map);
		//查询原更新人信息
		if(StringUtil.isBlank(memberDepositMap.get("operateUser"))) {
			return new Message(306,"该提款信息还未被锁定");
		}
		String olderOperateUser = String.valueOf(memberDepositMap.get("operateUser"));
		//从session中获取当前操作人
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		String operateUser=sysUser.getUserName();
		//判断是否锁定
//		if((operateUser.equals(olderOperateUser))||"1".equals(sysUser.getRoleId().toString())){
		if(operateUser.equals(olderOperateUser)){
			Byte state = Byte.valueOf(map.get("state").toString());
			//会员充值次数
			Integer recharge_times=Integer.valueOf(memberDepositMap.get("RECHARGE_TIMES").toString());
			//会员充值金额
			BigDecimal amount=new BigDecimal(map.get("amount").toString());
			//会员账户金额
			BigDecimal coin = new BigDecimal(memberDepositMap.get("COIN")+"");
			//赠送金额
			BigDecimal depositGive = coinLogService.queryDepositGiveScheme(amount,recharge_times);
			if(state==3) {
				map.put("depositGive", depositGive);
			}
			int result=memberDepositMapper.updateDepositInfoState(map);
			if(result>0) {
				if(state==3) {
					memberDepositMap.put("userId", sysUser.getUserId());
					memberDepositMap.put("sysUserName", sysUser.getUserName());
					memberDepositMap.put("operateIp", request.getRemoteAddr());
					memberDepositMap.put("info",map.get("info"));
					memberDepositMap.put("userCoin",amount.add(coin) );
					memberDepositMap.put("coinBefore",coin);
					memberDepositMap.put("coin",amount);
					memberDepositMap.put("coinOperateType",1);
					//插入充值帐变
					result = memberDepositMapper.addUserCoinLog(memberDepositMap);		
					if(result <= 0) {
						throw new RuntimeException("操作失败");
					}
					//插入打码量变动记录表
					Map<String, Object> betmap=new HashMap<>();
					betmap.put("uid",memberDepositMap.get("uid"));
					betmap.put("user_name",memberDepositMap.get("userName"));
					betmap.put("betsum_amount",amount);
					betmap.put("betsum_before",memberDepositMap.get("WITHDRAW_NEEDSUM")==null?0:memberDepositMap.get("WITHDRAW_NEEDSUM"));
					betmap.put("operate_ip",request.getRemoteAddr());
					betmap.put("betsum_operate_type",1);
					betmap.put("operate_uid",sysUser.getUserId());
					betmap.put("operate_user",sysUser.getUserName());
					betmap.put("info","人工处理充值增加提现所需打码量");
					result=betsumLogMapper.insertBetsum(betmap);
					if(result <= 0) {
						throw new RuntimeException("操作失败");
					}
					if(depositGive!=null&&depositGive.compareTo(new BigDecimal(0))>0) {
						//插入充值赠送帐变
						memberDepositMap.put("userCoin",amount.add(coin).add(depositGive));
						memberDepositMap.put("coinBefore",amount.add(coin));
						memberDepositMap.put("coin",depositGive);
						memberDepositMap.put("coinOperateType",10);
						memberDepositMap.put("orderId",null);
						result=memberDepositMapper.addUserCoinLog(memberDepositMap);
						if(result <= 0) {
							throw new RuntimeException("操作失败");
						}
						//插入充值赠送打码量变动记录表
						betmap.put("betsum_amount",amount);
						betmap.put("betsum_before",memberDepositMap.get("WITHDRAW_NEEDSUM")==null?0:memberDepositMap.get("WITHDRAW_NEEDSUM"));
						betmap.put("depositGive",depositGive);
						betmap.put("betsum_operate_type",10);
						betmap.put("info","充值赠送增加提现所需打码量");
						result=betsumLogMapper.insertBetsum(betmap);
						if(result<=0) {
							throw new RuntimeException("操作失败");
						}
					}
					//更新会员账号资金
					String uid = memberDepositMap.get("uid").toString();
					memberDepositMap.clear();
					memberDepositMap.put("uid", uid);
					memberDepositMap.put("coin", amount);
					memberDepositMap.put("depositGive",depositGive==null?new BigDecimal(0):depositGive);
					int VIP_ID = coinLogService.getVIPId(uid,amount);
					memberDepositMap.put("VIP_ID",VIP_ID);//——获取充值完成后的VIPID
					/*result = memberDepositMapper.updateUserCoin(memberDepositMap);*/
					result =userMapper.updateCoin(memberDepositMap);
					if(result <= 0) {
						throw new RuntimeException("操作失败");
					}
				}else if(state==4){
					
				}
				return new Message(200,"充值处理成功");
			}else {
				throw new RuntimeException("操作失败");
			}
		}else {
			return new Message(306,"您不能进行此操作");
		}
	}

	@Override
	public Map<String, Object> depositQuery(Map<String, Object> map) {
		Map<String, Object> memberDeposit= memberDepositMapper.queryById(map);
		return memberDeposit;
	}

	@Override
	public Message depositIsLock(HttpServletRequest request,Map<String, Object> map) {
		Integer id= Integer.valueOf(map.get("id").toString());
		Map<String, Object> memberDepositMap= memberDepositMapper.queryById(map);
		//查询状态是否为锁定
		String olderState = String.valueOf(memberDepositMap.get("state"));
		String olderOperateUser = String.valueOf(memberDepositMap.get("operateUser"));
		//从session中获取当前操作人
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		String operateUser=sysUser.getUserName();
		//创建对象
		MemberDeposit memberDeposit = new MemberDeposit();
		//判断是否锁定
		if("1".equals(olderState)||("2".equals(olderState)&&operateUser.equals(olderOperateUser))||"1".equals(sysUser.getRoleId().toString())) {
			memberDeposit.setOperateUser(operateUser);
		}else {
			return new Message(306,"您不能进行此操作");
		}
		//设置更新属性
		Byte state =Byte.parseByte(String.valueOf(map.get("state")));
		memberDeposit.setId(id);
		memberDeposit.setState(state);
		memberDeposit.setOperateIp(request.getRemoteAddr());
		memberDeposit.setOperateUid(sysUser.getUserId());
		memberDeposit.setOperateTime(new Date());
		int update = memberDepositMapper.updateByPrimaryKeySelective(memberDeposit);
		if(update>0) {
			return new Message(200,"充值锁定或取消成功");
		}else {
			LOGGER.info("充值锁定或取消失败");
			return new Message(305,"充值锁定或取消失败");
		}
	}

	@Override
	public List<DepositRecord> exportDepositRecord(Map<String, Object> map) {
		return memberDepositMapper.exportDepositRecord(map);
	}

	@Override
	public Map<String, Object> depositUserInfo(Map<String, Object> map) {
		Map<String,Object> result = memberDepositMapper.depositUserInfo(map);
		if(result!=null) {
			String proxyName = result.get("proxyName").toString();
			String proxyNames = getProxyNames(proxyName,""); 
			result.put("proxyName",proxyNames);
		}
		return result;
	}
	
	
	public String getProxyNames(String proxyName,String proxyNames) {
		Map<String, Object> map = new HashMap<>();
		map.put("userName", proxyName);
		UserVipModel user = userMapper.getUserByUserName(map);
		String proxys = "".equals(proxyNames)?proxyName:proxyName+">"+proxyNames;
		if(user!=null && !"0".equals(user.getProxyId())) {
			proxys=getProxyNames(user.getProxyName(),proxys);
		}
		return proxys;
	}
}
