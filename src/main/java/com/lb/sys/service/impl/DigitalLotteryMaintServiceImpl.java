/**
 * 
 */
package com.lb.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.DigitalLotteryMaintMapper;
import com.lb.sys.dao.DigitalOpenMapper;
import com.lb.sys.service.IDigitalLotteryMaintService;
import com.lb.sys.tools.DateUtils;
import com.lb.sys.tools.model.Message;

/**
 * @describe 数字彩维护service层接口
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class DigitalLotteryMaintServiceImpl implements IDigitalLotteryMaintService {

	
	@Autowired DigitalLotteryMaintMapper digitalLotteryMaintMapper;
	
	@Autowired DigitalOpenMapper openMapper;

	@Override
	public List<Map<String, Object>> getNewDigitalLotteryList(Map<String, Object> param) {
		return digitalLotteryMaintMapper.getNewDigitalLotteryList(param);
	}

	@Override
	public Map<String, Object> getDigitalLotteryInfoList(Map<String, Object> param) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("typeList", digitalLotteryMaintMapper.getDigitalLotteryTypeList());
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getDigitalLotteryIssueList(Map<String, Object> param) {
		return digitalLotteryMaintMapper.getDigitalLotteryIssueList(param);
	}
	
	@Override
	public Map<String, Object> qryOpenDataById(String id) {
		return digitalLotteryMaintMapper.qryOpenDataById(id);
	}

	@Override
	public int updateDigitalLotteryInfo(Map<String, Object> param) {
		return 0;
	}

	@Override
	public Message updateOpenDataInfo(Map<String, Object> param) {
		Message msg = new Message();
		int result = 0;
		result = this.openMapper.insertDigitalOpenData(param);
		if(result>0) {
			msg.setCode(200);
			msg.setMsg("修改成功");
		}else {
			msg.setCode(201);
			msg.setMsg("修改失败");
		}
		return msg;
	}

	@Override
	public Message saveOpenDataInfo(Map<String, Object> param) {
		Message msg = new Message();
		//判断是否已有开奖数据
		Map<String, Object> openData = digitalLotteryMaintMapper.qryOpenDataById(param.get("id")+"");
		if(openData!=null&&openData.size()>0) {
			msg.setCode(201);
			msg.setMsg("您这期所添加的开奖信息已经存在，无需再次添加");
			return msg;
		}
		param.put("open_time", DateUtils.getDateString(new Date()));
		int result = this.openMapper.insertDigitalOpenData(param);
		if(result>0) {
			msg.setCode(200);
			msg.setMsg("添加成功");
		}else {
			msg.setCode(201);
			msg.setMsg("添加失败");
		}
		return msg;
	}
	
}
