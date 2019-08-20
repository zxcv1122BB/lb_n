package com.lb.member.service.impl;
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

import com.lb.download.model.WithdrawRecord;
import com.lb.member.model.MemberWithdraw;
import com.lb.member.service.IMemberWithdrawService;
import com.lb.sys.dao.MemberWithdrawMapper;
import com.lb.sys.model.SysUser;
import com.lb.sys.tools.StringUtil;
import com.lb.sys.tools.model.Message;

@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class MemberWithdrawServiceImpl implements IMemberWithdrawService{
	private final Log LOGGER = LogFactory.getLog(MemberWithdrawServiceImpl.class);
	@Autowired
	private MemberWithdrawMapper memberWithdrawMapper;
	
	@Override
	public List<Map<String, Object>> queryMemberWithdrawList(Map<String, Object> map) {
		return memberWithdrawMapper.queryMemberWithdrawList(map);
	}
	@Override
	public Message withdrawIsLock(HttpServletRequest request,Map<String, Object> map) {
		
		Map<String, Object> memberWithdrawMap= memberWithdrawMapper.queryById(map);
		//查询状态是否为锁定
		String olderState = String.valueOf(memberWithdrawMap.get("state"));
		String olderOperateUser = String.valueOf(memberWithdrawMap.get("operateUser"));
		//从session中获取当前操作人
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		String operateUser=sysUser.getUserName();
		//创建对象
		MemberWithdraw memberWithdraw = new MemberWithdraw();
		//判断是否锁定
		if("1".equals(olderState)||("2".equals(olderState)&&operateUser.equals(olderOperateUser))||"1".equals(sysUser.getRoleId().toString())) {
			memberWithdraw.setOperateUser(operateUser);
		}else {
			return new Message(306,"您不能进行此操作");
		}
		//设置更新属性
		Byte state =Byte.parseByte(String.valueOf(map.get("state")));
		Integer id= Integer.valueOf(map.get("id").toString());
		memberWithdraw.setId(id);
		memberWithdraw.setState(state);
		memberWithdraw.setOperateIp(request.getRemoteAddr());
		memberWithdraw.setOperateUid(sysUser.getUserId());
		memberWithdraw.setOperateTime(new Date());
		int update = memberWithdrawMapper.updateByPrimaryKeySelective(memberWithdraw);
		if(update>0) {
			return new Message(200,"提现锁定或取消成功");
		}else {
			return new Message(309,"提现锁定或取消失败");
		}
	}
	@Override
	public Map<String, Object> withdrawQuery(Map<String, Object> map) {
		Map<String, Object> memberWithdraw = memberWithdrawMapper.queryById(map);
		map.put("userType", Integer.valueOf(memberWithdraw.get("userType").toString()));
		if("2".equals(memberWithdraw.get("userType").toString())) {
		
			Map<String, Object> withdrawMap = memberWithdrawMapper.withdrawQuery(map);
			/*if(new BigDecimal((withdrawMap.get("isWithdraw").toString())).compareTo(new BigDecimal(0))<0) {
				withdrawMap.put("isWithdraw","是");
			}else {
				withdrawMap.put("isWithdraw","否");
			}*/
			return withdrawMap;
		}else if ("1".equals(memberWithdraw.get("userType").toString())) {
			
			Map<String, Object> withdrawMap = memberWithdrawMapper.withdrawQuery(map);
			return withdrawMap;
		}
		return memberWithdraw;
	}
	@Override
	public Message withdrawHandle(HttpServletRequest request,Map<String, Object> map) {
		Integer id= Integer.valueOf(map.get("id").toString());
		Map<String, Object> memberWithdrawMap= memberWithdrawMapper.queryById(map);
		//查询原更新人信息
		String olderOperateUser = String.valueOf(memberWithdrawMap.get("operateUser"));
		//从session中获取当前操作人
		SysUser sysUser = (SysUser) request.getSession().getAttribute("sysUser");
		String operateUser=sysUser.getUserName();
		//创建对象
		MemberWithdraw memberWithdraw = new MemberWithdraw();
		//判断是否锁定
		if(operateUser.equals(olderOperateUser)){
			//设置更新属性
			Byte state =Byte.parseByte(String.valueOf(map.get("state")));
			memberWithdraw.setId(id);
			memberWithdraw.setState(state);
			memberWithdraw.setOperateIp(request.getRemoteAddr());
			memberWithdraw.setOperateUid(sysUser.getUserId());
			memberWithdraw.setOperateTime(new Date());
			int update = memberWithdrawMapper.updateByPrimaryKeySelective(memberWithdraw);
			if(update>0) {
			/*	UserModel userModel=userService.queryUserByUserName(memberWithdrawMap.get("userName").toString());
				int updateCoin = userMapper.updateCoin(userModel);*/
				return new Message(200,"提现处理成功");
			}else {
				LOGGER.info("提现处理失败");
				return new Message(309,"提现处理失败");
			}
		}else {
			return new Message(306,"您不能进行此操作");
		}
	}
	@Override
	public List<WithdrawRecord> exportWithdrawRecord(Map<String, Object> map) {
		return memberWithdrawMapper.exportWithdrawRecord(map);
	}
	@Override
	public Map<String, Object> withdrawUserInfo(Map<String, Object> map) {
		return memberWithdrawMapper.withdrawUserInfo(map);
	}
	/* 
	 * 管理员对会员提款请求的操作
	 * 	1.对将会员提款的数据修改为处理成功并sql判断当前操作管理员是否为锁定管理员
	 * 	2.在管理员同意提款操作后，将用户提款后的提款金额从冻结金额中减除并减除余额，否则不管
	 * 	3.增加一条用户提款的资金明细记录
	 * 
	 */
	@Override
	public Message memberDrawingManage(Map<String, Object> paramMap) {
		Map<String, Object> infoMap= memberWithdrawMapper.queryById(paramMap);
		//查询原更新人信息
		int code = 555;//code值 响应信息
		String msg = null;
		if(StringUtil.isBlank(infoMap.get("operateUid"))) {
			return new Message(code,"该提款信息还未被锁定");
		}else {
//			if((infoMap.get("operateUid").toString().equals(paramMap.get("userId").toString()))||("1".equals(paramMap.get("roleId").toString()))) {
			if(infoMap.get("operateUid").toString().equals(paramMap.get("userId").toString())) {
				
				Byte state = Byte.valueOf(paramMap.get("state").toString());
				int result = memberWithdrawMapper.updateDrawingInfoState(paramMap);
				
				if(result > 0) {
					code = 200;
					msg = "操作成功";
					if(state == 3) { // 提现成功
						infoMap.put("userId", paramMap.get("userId"));
						infoMap.put("sysUserName", paramMap.get("sysUserName"));
						infoMap.put("operateIp", paramMap.get("operateIp"));
						infoMap.put("remark", paramMap.get("remark"));
						
						infoMap.put("betsumOperateType", 2);
						infoMap.put("info", "提款处理清空打码量");
						
						memberWithdrawMapper.insertBetsumLog(infoMap);
						
						result = memberWithdrawMapper.updateUserCoinInfo(infoMap);
						if(result <= 0) {
							throw new RuntimeException("操作失败");
						}
						result = memberWithdrawMapper.addUserCoinLog(infoMap);
						if(result <= 0) {
							throw new RuntimeException("操作失败");
						}
						return new Message(code,msg);
					}else if(state == 4) { // 驳回
						infoMap.put("operateType", 1);
						result = memberWithdrawMapper.updateUserCoinInfo(infoMap);
						if(result <= 0) {
							throw new RuntimeException("操作失败");
						}
					}
				}else {
					throw new RuntimeException("操作失败");
				}
			}else {
				return new Message(code,"该提款正在被锁定操作");
			}
		}
		msg = "操作失败";
		return new Message(code,msg);
	}
}
