package com.lb.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.PlayInfoMapper;
import com.lb.sys.service.IPayInfoService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class PayInfoServiceImpl implements IPayInfoService {

	@Autowired
	private PlayInfoMapper playInfoMapper;
	
	@Override
	public List<Map<String, Object>> queryPayList() {
		
		return playInfoMapper.queryPayList();
	}

	@Override
	public Integer addPayInfo(Map<String, Object> map) {
		Integer addPayInfo = playInfoMapper.addPayInfo(map);//添加支付信息
//		if (addPayInfo>0) {//获取回显的主键
//			Integer id = Integer.parseInt(map.get("id").toString());
//			map.put("mathid", id);
//			Integer addPaytype = playInfoMapper.addPaytype(map);
//			Integer result = addPaytype;
//			if (result>0) {//添加下面的支付方式
//				map.put("methodid", map.get("id"));
////				playInfoMapper.addPaytype(map);
//				return 1;
//			}
//		}
//		return -1;
		return addPayInfo;
	}

	@Override
	public List<Map<String, Object>> queryPaytypeListByMethid(Integer methodid) {
		
		return playInfoMapper.queryPaytypeByMethodid(methodid);
	}

	@Override
	public Integer delPayInfoByid(Integer id) {
		Integer result = playInfoMapper.deletePayInfo(id);
//		if (result>0) {
//			result =playInfoMapper.deletePayTypeList(id);
//		}
		return result;
	}

	@Override
	public List<Map<String, Object>> queryPayOnlineNames() {
		
		return playInfoMapper.queryPayOnlineNames();
	}

	@Override
	public List<Map<String, Object>> queryPayTypeList() {
		
		return playInfoMapper.queryPayTypeList();
	}

	@Override
	public Integer updatePayInfo(Map<String, Object> map) {//更新在线支付
		Integer result=playInfoMapper.updatePayInfo(map);
//		if (result>=0) {
//			result = playInfoMapper.updatePaytypeDetailInfo(map);
//		}
		return result;
	}

}
