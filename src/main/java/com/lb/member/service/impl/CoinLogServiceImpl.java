package com.lb.member.service.impl;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.lb.member.model.CoinLog;
import com.lb.member.service.ICoinLogService;
import com.lb.report.model.OperationAnalysis;
import com.lb.report.model.TeamCount;
import com.lb.sys.dao.BetsumLogMapper;
import com.lb.sys.dao.CoinLogMapper;
import com.lb.sys.dao.GameBetsPOJOMapper;
import com.lb.sys.dao.MemberDepositMapper;
import com.lb.sys.dao.MemberWithdrawMapper;
import com.lb.sys.dao.SysConfigureMapper;
import com.lb.sys.dao.UserModelMapper;
import com.lb.sys.model.SysConfigure;
import com.lb.sys.model.SysUser;
import com.lb.sys.model.UserModel;
import com.lb.sys.service.UserModelService;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.SnowFlakeIdWorker;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

@Service
public class CoinLogServiceImpl implements ICoinLogService{
	
	private final Log LOGGER = LogFactory.getLog(CoinLogServiceImpl.class);
	
	@Autowired
	private CoinLogMapper coinLogMapper;
	@Autowired
	private UserModelMapper userMapper;
	@Autowired
	private UserModelService userService;
	@Autowired
	private SysConfigureMapper configureMapper;
	@Autowired
	private GameBetsPOJOMapper gameBetsMapper;
	@Autowired
	private PlatformTransactionManager txManager;
	@Autowired
	private MemberDepositMapper depositMapper;
	@Autowired
	private MemberWithdrawMapper withDrawMapper;
	@Autowired
	private BetsumLogMapper betsumLogMapper;
	
	@Override
	public List<Map<String, Object>> queryCoinLogList(Map<String, Object> map) {
		return coinLogMapper.queryCoinLogList(map);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
	public int insertAddAndSubtractMoney(HttpServletRequest request, CoinLog coinLog) {
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		coinLog.setOperateTime(new Date());
		if(sysUser!=null) {
			coinLog.setOperateUid(sysUser.getUserId());
			coinLog.setOperateUser(sysUser.getUserName());
		}
		coinLog.setOperateIp(request.getRemoteAddr());
		int insertSelective = coinLogMapper.insertSelective(coinLog);
		if(insertSelective>0) {
			return insertSelective;
		}else {
			LOGGER.error("后台添加帐变记录失败");
			return 0;
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
	public Message addAndSubtractMoney(HttpServletRequest request, Map<String, Object> map) {
		try {	
			SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
			String operate_uid = sysUser.getUserId().toString();
			String operate_user = sysUser.getUserName();
			String operate_ip = request.getRemoteAddr();
			Date operate_time = new Date();
			//加减款判断
			Byte coinOperateType = Byte.valueOf(String.valueOf(map.get("coinOperateType")));
			BigDecimal coin=new BigDecimal(""+map.get("coin"));
			//查询会员信息
			String userName = String.valueOf(map.get("userName"));
			UserModel userModel=userService.queryUserByUserName(userName);
			//判断加扣款
			if(coinOperateType.equals(Byte.valueOf("7"))) {
				if((userModel.getCoin().subtract(new BigDecimal(userModel.getFcion()))).compareTo(coin)<0) {
					throw new RuntimeException("所减款不能大于会员的余额与冻结资金之差");
				}
				String order_id = SnowFlakeIdWorker.getOrderIdBySnowFlake("W");
				//插入提现记录表
				Map<String,Object> withdraw = new HashMap<>();
				withdraw.put("order_id", order_id);
				withdraw.put("uid", userModel.getUid());
				withdraw.put("user_type", 2);//会员
				withdraw.put("user_name", userModel.getUserName());
				withdraw.put("name", userModel.getName());
				withdraw.put("amount", coin);
				withdraw.put("state", 3);//成功到账
				withdraw.put("operate_ip", operate_ip);
				withdraw.put("operate_user", operate_uid);
				withdraw.put("operate_user", operate_user);
				withdraw.put("bankAccount", userModel.getBankAccount());
				withdraw.put("bankAddress", userModel.getName());
				withdraw.put("bankName", userModel.getBankName());
				withdraw.put("operate_time", operate_time);
				withdraw.put("info", "人工扣款");	
				int set=withDrawMapper.manualWithDraw(withdraw);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//插入资金流动记录表
				CoinLog coinLog = new CoinLog();
				coinLog.setOrderId(order_id);
				coinLog.setCoinBefore(userModel.getCoin());
				coinLog.setUid(userModel.getUid());
				coinLog.setUserName(userModel.getUserName());
				coinLog.setCoinOperateType(coinOperateType);
				coinLog.setInfo(String.valueOf(map.get("info")));
				coinLog.setInputtime(new Date());
				coinLog.setCoin(coin);
				coinLog.setUsercoin(userModel.getCoin().subtract(coin));
				set=insertAddAndSubtractMoney(request,coinLog);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				/*//插入打码量变动记录表
				Map<String, Object> betmap=new HashMap<>();
				betmap.put("isWithdraw",1);
				betmap.put("uid",userModel.getUid());
				betmap.put("user_name",userModel.getUserName());
				//betmap.put("betsum_amount",userModel.getBetsum());
				//betmap.put("user_betsum", "-"+coin);
				betmap.put("betsum_before",userModel.getWithdrawNeedsum());
				betmap.put("betsum_operate_type",7);
				betmap.put("operate_ip",operate_ip);
				betmap.put("operate_uid",operate_uid);
				betmap.put("operate_user",operate_user);
				betmap.put("info","人工减款清除提现所需打码量");
				set=betsumLogMapper.insertBetsum(betmap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}*/
				//更新会员账户资金
				Map<String, Object> usermap=new HashMap<>();
				usermap.put("coin", "-"+coin);
				usermap.put("uid", userModel.getUid());
				usermap.put("isWithdraw",1);
				usermap.put("deductMoney",1);
				set=userMapper.updateCoin(usermap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
			}else if(coinOperateType.equals(Byte.valueOf("6"))){
				if((userModel.getCoin().add(coin).subtract(new BigDecimal(userModel.getFcion()))).compareTo(new BigDecimal("100000000"))>=0) {
					throw new RuntimeException("加款过多，该会员存款将超过上限100000000");
				}
				String order_id = SnowFlakeIdWorker.getOrderIdBySnowFlake("D");
				//赠送金额
				BigDecimal depositGive =queryDepositGiveScheme(coin,userModel.getRechargeTimes());
				//设置充值记录表
				Map<String,Object> deposit = new HashMap<>();
				deposit.put("order_id", order_id);
				deposit.put("uid", userModel.getUid());
				deposit.put("user_name", userModel.getUserName());
				deposit.put("name", userModel.getName());
				deposit.put("recharge_amount",coin.add(depositGive));
				deposit.put("apply_time", new Date());
				deposit.put("amount", coin);
				deposit.put("coin", userModel.getCoin());
				deposit.put("bankAccount", userModel.getBankAccount());
				deposit.put("bankAddress", userModel.getName());
				deposit.put("bankName", userModel.getBankName());
				deposit.put("fcoin", userModel.getFcion());
				deposit.put("operate_ip", operate_ip);
				deposit.put("operate_user", operate_user);
				deposit.put("state", 3);//成功到账
				deposit.put("deposit_operate_type",3);//3人工加款，4人工赠送
				deposit.put("info","人工加款");
				int set=depositMapper.manualDeposit(deposit);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//设置资金流动记录表
				CoinLog coinLog = new CoinLog();
				coinLog.setOrderId(order_id);
				coinLog.setUid(userModel.getUid());
				coinLog.setUserName(userModel.getUserName());
				coinLog.setCoinOperateType(coinOperateType);
				coinLog.setInfo(String.valueOf(map.get("info")));
				coinLog.setInputtime(new Date());
				coinLog.setCoin(coin);
				coinLog.setCoinBefore(userModel.getCoin());
				coinLog.setUsercoin(userModel.getCoin().add(coin));
				//插入资金流动记录表（充值）
				set=insertAddAndSubtractMoney(request,coinLog);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//插入打码量变动记录表
				Map<String, Object> betmap=new HashMap<>();
				betmap.put("uid",userModel.getUid());
				betmap.put("user_name",userModel.getUserName());
				betmap.put("betsum_amount",coin);
				betmap.put("betsum_before",userModel.getWithdrawNeedsum());
				betmap.put("betsum_operate_type",1);
				betmap.put("operate_ip",operate_ip);
				betmap.put("operate_uid",operate_uid);
				betmap.put("operate_user",operate_user);
				betmap.put("info","充值增加提现所需打码量");
				set=betsumLogMapper.insertBetsum(betmap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				if(depositGive!=null&&depositGive.compareTo(new BigDecimal(0))>0) {
					//插入资金流动记录表（充值赠送）
					coinLog.setCoinOperateType(Byte.valueOf("10"));
					coinLog.setOrderId("");
					coinLog.setCoinBefore(userModel.getCoin().add(coin));
					coinLog.setCoin(depositGive);
					coinLog.setUsercoin(userModel.getCoin().add(coin).add(depositGive));
					set=insertAddAndSubtractMoney(request,coinLog);
					if(set<=0) {
						throw new RuntimeException("操作失败");
					}
					//插入充值赠送打码量变动记录表
					betmap.put("betsum_amount",coin);
					betmap.put("betsum_before",userModel.getWithdrawNeedsum());
					betmap.put("depositGive",depositGive);
					betmap.put("betsum_operate_type",10);
					betmap.put("info","充值赠送增加提现所需打码量");
					set=betsumLogMapper.insertBetsum(betmap);
					if(set<=0) {
						throw new RuntimeException("操作失败");
					}
				}
				//更新会员账户资金
				Map<String, Object> usermap=new HashMap<>();
				usermap.put("coin", coin);
				usermap.put("uid", userModel.getUid());
				usermap.put("depositGive",depositGive==null?new BigDecimal(0):depositGive);
				String uid = userModel.getUid().toString();
				int VIP_ID =  getVIPId(uid,coin);
				usermap.put("VIP_ID",VIP_ID);//——获取充值、人工赠送、完成后的VIPID
				set=userMapper.updateCoin(usermap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
			}else if(coinOperateType.equals(Byte.valueOf("21"))){//人工赠送
				if((userModel.getCoin().add(coin).subtract(new BigDecimal(userModel.getFcion()))).compareTo(new BigDecimal("100000000"))>=0) {
					throw new RuntimeException("加款过多，该会员存款将超过上限100000000");
				}
				//设置资金流动记录表
				CoinLog coinLog = new CoinLog();
				coinLog.setOrderId(null);
				coinLog.setUid(userModel.getUid());
				coinLog.setUserName(userModel.getUserName());
				coinLog.setCoinOperateType(coinOperateType);
				coinLog.setInfo(String.valueOf(map.get("info")));
				coinLog.setInputtime(new Date());
				coinLog.setCoin(coin);
				coinLog.setCoinBefore(userModel.getCoin());
				coinLog.setUsercoin(userModel.getCoin().add(coin));
				//插入资金流动记录表（充值）
				int set=insertAddAndSubtractMoney(request,coinLog);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//插入打码量变动记录表
				Map<String, Object> betmap=new HashMap<>();
				betmap.put("uid",userModel.getUid());
				betmap.put("user_name",userModel.getUserName());
				betmap.put("betsum_amount",coin);
				betmap.put("rgGiving",1);
				betmap.put("betsum_before",userModel.getWithdrawNeedsum());
				betmap.put("betsum_operate_type",coinOperateType);//人工赠送
				betmap.put("operate_ip",operate_ip);
				betmap.put("operate_uid",operate_uid);
				betmap.put("operate_user",operate_user);
				betmap.put("info","人工赠送增加提现所需打码量");
				set=betsumLogMapper.insertBetsum(betmap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//更新会员账户资金
				Map<String, Object> usermap=new HashMap<>();
				usermap.put("coin", coin);
				usermap.put("uid", userModel.getUid());
				int VIP_ID =  getVIPId(String.valueOf(userModel.getUid()),coin);
				usermap.put("VIP_ID",VIP_ID);//——获取充值、人工赠送/加款 完成后的VIPID
				usermap.put("giving",1);
				set=userMapper.updateCoin(usermap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
			}
			return new Message(200,"操作成功");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
			e.printStackTrace();
			LOGGER.error("操作失败");
			return new Message(321,e.getMessage());
		}
	}

	@Override
	public List<TeamCount> queryFinanceList(Map<String, Object> paramMap) {
		return coinLogMapper.queryFinanceList(paramMap);
	}

	@Override
	public List<Map<String, Object>> queryHistoryFinanceList(Map<String, Object> paramMap) {
		return this.coinLogMapper.queryHistoryFinanceList(paramMap);
	}

	@SuppressWarnings("finally")
	@Override
	public Message toWeekReturnWaterDatas() {
		Message message =new Message();
		//获取上周日期时间
		String lastWeekMonday[]=DateUtils.getLastWeekMonday().split(",");
		String monday = lastWeekMonday[0],sunday = lastWeekMonday[1];
		Map<String, Object> paramterMap = new HashMap<>();
		paramterMap.put("moduleId", 8);
		//获取反水配置比例
		List<SysConfigure> configures = configureMapper.querySysConfigure(paramterMap);
		if(configures==null || configures.isEmpty()) {
			message.setCode(201);
			message.setMsg("投注反水配置还未配置");
			return message;
		}
		SysConfigure configure = configures.get(0);
		if(configure.getOnOff()==0) {
			message.setCode(202);
			message.setMsg("投注反水配置还未开启");
			return message;
		}
		String config1 = configure.getSysConfig1();
		
		paramterMap.put("startDate", monday);
		paramterMap.put("endDate", sunday);
		//获取上周时间所需要计算反水用户投注信息
		List<Map<String,Object>> userCoinLogs = this.gameBetsMapper.queryGameBetsInfo(paramterMap);
		if(userCoinLogs!=null&& userCoinLogs.size()>0) {
			int error=0;
			for(int i=0;i<userCoinLogs.size();i++) {
				DefaultTransactionDefinition def = new DefaultTransactionDefinition();
				def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务
				TransactionStatus status = txManager.getTransaction(def);
				try {
					CoinLog coinLog = new CoinLog();
					Map<String,Object> userCoinLog = userCoinLogs.get(i);
					BigDecimal amount = new BigDecimal(userCoinLog.get("amount").toString());
					BigDecimal bonus = new BigDecimal(userCoinLog.get("bonus").toString());
					BigDecimal lossMoney = amount.subtract(bonus);//亏赢 取绝对值
					BigDecimal pro = this.returnWaterDatas(config1, lossMoney.abs());//反水比例
					//反水金额==变动金额
					BigDecimal returnMoney = lossMoney.multiply(pro);
					coinLog.setBetid(Integer.valueOf(userCoinLog.get("betid").toString()));
					coinLog.setOrderId(userCoinLog.get("orderId").toString());
					coinLog.setUid(Long.valueOf(userCoinLog.get("uid").toString()));
					coinLog.setCoin(returnMoney);
					coinLog.setCoinBefore(lossMoney);
					coinLog.setFcoin(lossMoney.abs().add(returnMoney)); 
					coinLog.setCoinOperateType(Byte.valueOf("8"));
					coinLog.setUserName(userCoinLog.get("username")!=null?userCoinLog.get("username").toString():"");
					coinLog.setInputtime(DateUtils.StringToDate(userCoinLog.get("inputTime").toString()));
					coinLog.setType(Byte.valueOf(userCoinLog.get("type").toString()));
					//插入资金流动记录表
					coinLogMapper.insertSelective(coinLog);
					txManager.commit(status);
				} catch (Exception e) {
					LOGGER.error("添加平台反水金额异常："+e.getMessage());
					error++;
					txManager.rollback(status);
				}finally {
					continue;
				}
			}
			message.setCode(200);
			message.setMsg("新增成功,总共"+userCoinLogs.size()+"条信息，其中有"+error+"条新增失败");
		}else {
			message.setCode(201);
			message.setMsg("无相关信息");
		}
		return message;
	}

	/**
	 * 
	 * @param config1 反水配置值
	 * @param lossMoney 金额
	 * @return 反水比例值
	 */
	private BigDecimal returnWaterDatas(String config1,BigDecimal lossMoney) {
		//1、反水比例格式
		//1.1、1-500-1#501-1000-2#1001-1500-3
		BigDecimal pro = new BigDecimal(0);
		String configs[] = config1.split("#");
		for(String config:configs) {
			String config_s[] = config.split("-");
			BigDecimal min = new BigDecimal(config_s[0]);
			BigDecimal max = new BigDecimal(config_s[1]);
			if((lossMoney.compareTo(min)==1 || lossMoney.compareTo(min)==0)
					&& (lossMoney.compareTo(max)==-1 || lossMoney.compareTo(max)==0)) {
				pro = new BigDecimal(config_s[2]);
				break;
			}
		}
		return pro;
	}

	@Override
	public List<OperationAnalysis> getDepositList(Map<String, Object> map) {
		return coinLogMapper.getDepositList(map);
	}

	@Override
	public OperationAnalysis getDepositListTotal(Map<String, Object> map) {
		return coinLogMapper.getDepositListTotal(map);
	}
	
	public BigDecimal queryDepositGiveScheme(BigDecimal paymoney,Integer user_recharge_fq) {
		try {
			BigDecimal giving_money = new BigDecimal(0);
			List<Map<String,Object>> depositGiviSchemeList = userMapper.queryDepositGiveScheme();		
			if(depositGiviSchemeList != null && depositGiviSchemeList.size() != 0 && paymoney != null && paymoney.compareTo(BigDecimal.ZERO) > 0) {
				for(int i = 0;i<depositGiviSchemeList.size();i++) {
					Map<String,Object> itemMap = depositGiviSchemeList.get(i);
					if(null == itemMap || itemMap.keySet().size() == 0) break;
					//判断金额范围在amount_down到amount_up之间
					if(!StringUtil.isBlank(itemMap.get("amount_up"))
							&& new BigDecimal(itemMap.get("amount_up").toString()).compareTo(paymoney) < 0) {
						LOGGER.info("高于赠送限金额:"+ giving_money);
						continue;
					}
					if(!StringUtil.isBlank(itemMap.get("amount_down"))
							&& new BigDecimal(itemMap.get("amount_down").toString()).compareTo(paymoney) > 0) {
						LOGGER.info("低于赠送下限金额:"+ giving_money);
						continue;
					}
					//用户当前充值次数
					int recharge_fq = Integer.valueOf(itemMap.get("recharge_fq").toString());
					//赠送方式
					String giving_away = itemMap.get("giving_way").toString();
					//赠送金额上限
					BigDecimal give_limit = null;
					if(!StringUtil.isBlank(itemMap.get("Giving_ceiling"))) {
						give_limit = new BigDecimal(itemMap.get("Giving_ceiling").toString());
					}
					//当前方案实际赠送金额
					BigDecimal expect_give = new BigDecimal(0);
					//当前方案实际赠送金额
					BigDecimal final_give = new BigDecimal(0);
					//普通（每次）赠送
					if(recharge_fq == 0) {
						if("FIXED".equals(giving_away)) {
							Float fiexd_give = (Float)itemMap.get("giving_number");//固定赠送 X元/每充值100元
							expect_give = new BigDecimal(fiexd_give);//固定赠送 
							if(give_limit != null) {
								final_give = final_give.add(give_limit.compareTo(expect_give)>=0 ? expect_give : give_limit);
							}else {
								final_give = expect_give;
							}
							giving_money=giving_money.add(final_give);							
						}else {
							BigDecimal percent = new BigDecimal(itemMap.get("giving_number").toString());
							percent = percent.divide(new BigDecimal(100));
							expect_give = paymoney.multiply(percent);
							if(give_limit != null) {
								final_give = final_give.add(give_limit.compareTo(expect_give)>=0 ? expect_give : give_limit);
							}else {
								final_give = expect_give;
							}
							giving_money = giving_money.add(final_give);							
						}
					}else {
						//需要判断充值次数的赠送方案
						boolean flag = (user_recharge_fq+1) == recharge_fq;
						if(flag) {
							if("FIXED".equals(giving_away)) {
								Float fiexd_give = (Float)itemMap.get("giving_number");//固定赠送 
								expect_give = new BigDecimal(fiexd_give);//固定赠送
								if(give_limit != null) {
									final_give = final_give.add(give_limit.compareTo(expect_give)>=0 ? expect_give : give_limit);
								}else {
									final_give = expect_give;
								}
								giving_money=giving_money.add(final_give);							
							}else {
								BigDecimal percent = new BigDecimal(itemMap.get("giving_number").toString());
								percent = percent.divide(new BigDecimal(100));
								expect_give = paymoney.multiply(percent);
								if(give_limit != null) {
									final_give = final_give.add(give_limit.compareTo(expect_give)>=0 ? expect_give : give_limit);
								}else {
									final_give = expect_give;
								}
								giving_money = giving_money.add(final_give);							
							}
						}						
					}
				}
				//四舍五入
				giving_money=giving_money.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			return giving_money;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**VIP级别**/
	public int getVIPId(String uid,BigDecimal paymoney) {
		//LOGGER.info("[会员充值],UID:"+uid+"充值金额:"+paymoney);
		try {
			List<Map<String, Object>> VIPList = userMapper.queryVIPGroup();
			if(null == VIPList || VIPList.size() == 0) return -1;
			String userDepositSUM = userMapper.queryDepositSUM(uid);
			if(StringUtils.isEmpty(userDepositSUM) || "0".equals(userDepositSUM)) return -1;
			BigDecimal sum = new BigDecimal(userDepositSUM);
			
			//LOGGER.info("[会员充值],总充值金额(含本次):"+sum+",历史总充值金额"+userDepositSUM);
			for(int i=0;i<VIPList.size();i++) {
				Map<String, Object> itemMap = VIPList.get(i);
				if(null == itemMap || itemMap.keySet().size() ==0) break;
				BigDecimal indexVIPSUM = new BigDecimal(itemMap.get("depositAmount").toString());
				//LOGGER.info("[会员充值],当前MAP:"+itemMap.entrySet());
				if(sum.compareTo(indexVIPSUM)>=0) {
					//LOGGER.info("[会员充值],indexVIPSUM:"+indexVIPSUM+",SUM:"+sum+",compare:"+sum.compareTo(indexVIPSUM));
					return (int) itemMap.get("vipId");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		}
		return -1;
	}

	@Override
	public Map<String, Object> queryProxyByUserName(String userName) {
		return coinLogMapper.queryProxyByUserName(userName);
	}

	@Override
	public Message proxyAddAndSubtractMoney(HttpServletRequest request, Map<String, Object> map) {
		try {	
			SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
			Long operate_uid = sysUser.getUserId();
			String operate_user = sysUser.getUserName();
			String operate_ip = request.getRemoteAddr();
			Date operate_time = new Date();
			//加减款判断
			Byte coinOperateType = Byte.valueOf(String.valueOf(map.get("coinOperateType")));
			BigDecimal coin=new BigDecimal(""+map.get("coin"));
			//查询会员信息
			String userName = String.valueOf(map.get("userName"));
			Map<String, Object> userModel=coinLogMapper.queryProxyByUserName(userName);
			//代理账号资金
			BigDecimal coinSum=userModel.get("coin")==null?new BigDecimal(0):new BigDecimal(userModel.get("coin").toString());
			//代理冻结资金
			BigDecimal fcion=userModel.get("fcion")==null?new BigDecimal(0):new BigDecimal(userModel.get("fcion").toString());
			//判断加扣款
			if(coinOperateType.equals(Byte.valueOf("7"))) {//扣款
				if((coinSum.subtract(fcion)).compareTo(coin)<0) {
					throw new RuntimeException("所减款不能大于代理的余额与冻结资金之差");
				}
				String order_id = SnowFlakeIdWorker.getOrderIdBySnowFlake("W");
				//插入提现记录表
				Map<String,Object> withdraw = new HashMap<>();
				withdraw.put("order_id", order_id);
				withdraw.put("uid", userModel.get("id"));
				withdraw.put("user_type", 1);//代理
				withdraw.put("user_name", userModel.get("userName"));
				withdraw.put("name", userModel.get("proxy_name"));
				withdraw.put("amount", coin);
				withdraw.put("state", 3);//成功到账
				withdraw.put("operate_ip", operate_ip);
				withdraw.put("operate_user", operate_uid);
				withdraw.put("operate_user", operate_user);
				withdraw.put("bankAccount", userModel.get("bank_account"));
				withdraw.put("bankAddress", userModel.get("bank_address"));
				withdraw.put("bankName", userModel.get("bank_name"));
				withdraw.put("operate_time", operate_time);
				withdraw.put("info", "人工扣款");	
				int set=withDrawMapper.manualWithDraw(withdraw);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//插入资金流动记录表
				CoinLog coinLog = new CoinLog();
				coinLog.setOrderId(order_id);
				coinLog.setUserType(Byte.valueOf("1"));
				coinLog.setCoinBefore(coinSum);
				coinLog.setUid(Long.parseLong(userModel.get("id").toString()));
				coinLog.setUserName(userModel.get("userName").toString());
				coinLog.setCoinOperateType(coinOperateType);
				coinLog.setInfo(String.valueOf(map.get("info")));
				coinLog.setInputtime(new Date());
				coinLog.setCoin(coin);
				coinLog.setUsercoin(coinSum.subtract(coin));
				coinLog.setOperateTime(new Date());
				coinLog.setOperateUid(operate_uid);
				coinLog.setOperateUser(operate_user);
				coinLog.setOperateIp(operate_ip);
				set=coinLogMapper.insertSelective(coinLog);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//更新代理账户资金
				Map<String, Object> usermap=new HashMap<>();
				usermap.put("coin", "-"+coin);  
				usermap.put("id", userModel.get("id"));
				set=coinLogMapper.updateProxyCoin(usermap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
			}else if(coinOperateType.equals(Byte.valueOf("6"))){
				if((coinSum.add(coin).subtract(fcion)).compareTo(new BigDecimal("100000000"))>=0) {
					throw new RuntimeException("加款过多，该代理存款将超过上限100000000");
				}
				//String order_id = SnowFlakeIdWorker.getOrderIdBySnowFlake("D");
				//插入资金流动记录表
				CoinLog coinLog = new CoinLog();
				//coinLog.setOrderId(order_id);
				coinLog.setCoinBefore(coinSum);
				coinLog.setUserType(Byte.valueOf("1"));
				coinLog.setUid(Long.parseLong(userModel.get("id").toString()));
				coinLog.setUserName(userModel.get("userName").toString());
				coinLog.setCoinOperateType(coinOperateType);
				coinLog.setInfo(String.valueOf(map.get("info")));
				coinLog.setInputtime(new Date());
				coinLog.setCoin(coin);
				coinLog.setUsercoin(coinSum.add(coin));
				coinLog.setOperateTime(new Date());
				coinLog.setOperateUid(operate_uid);
				coinLog.setOperateUser(operate_user);
				coinLog.setOperateIp(operate_ip);
				int set=coinLogMapper.insertSelective(coinLog);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
				//更新代理账户资金
				Map<String, Object> usermap=new HashMap<>();
				usermap.put("coin",coin);
				usermap.put("id", userModel.get("id"));
				set=coinLogMapper.updateProxyCoin(usermap);
				if(set<=0) {
					throw new RuntimeException("操作失败");
				}
			}
			return new Message(200,"操作成功");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 手动捕获异常后,事物会回滚
			e.printStackTrace();
			LOGGER.error("操作失败");
			return new Message(321,e.getMessage());
		}
	}

	@Override
	public List<Map<String, Object>> getCoinOperateType() {
		return coinLogMapper.getCoinOperateType();
	}
}
