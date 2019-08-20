package com.lb.OpenLog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lb.OpenLog.service.OpenLogService;
import com.lb.sys.dao.OpenLogMapper;
/***
 * 开奖日志    实现类
 * @author ASUS
 */
@Service
public class OpenLogServiceImpl implements OpenLogService {
	//开奖日志数据库连接
	@Autowired
	private OpenLogMapper openLogMapper;
	
	/***
	 * 查询所有的开奖信息
	 */
	@Override
	public List<Map<String, Object>> getAll(Map<String, Object> parmMap) {
		return openLogMapper.getAll(parmMap);
	}

	@Override
	public Map<String, Object> selectByOpenno(Map<String, Object> parmMap) {
		//调用查询方法
		Map<String, Object> selectByOpenno = openLogMapper.selectByOpenno(parmMap);
		//将操作日志切割
		if(selectByOpenno.get("openlog")!=null && !(selectByOpenno.get("openlog").equals(""))) {
			//取出日志
			String result = selectByOpenno.get("openlog").toString().substring(1);
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			//将日志切割成数组
			String temp[]=result.split("#");
			//再次切割
			for (String string : temp) {
				Map<String,Object> map=new HashMap<String,Object>();
				String openlog[]=string.split("@");
				map.put("curno",openlog[0]);
				//截取日期
				map.put("date",openlog[2].replace("T:", "").substring(0,13));
				//判断是否为空
				Map<String, Object> selectMatchById=new HashMap<String, Object>();
				if(openlog[1]!=null && !(openlog[2].equals(""))) {
				  selectMatchById = openLogMapper.selectMatchById(openlog[1]);
				}
				map.put("matchInfo", selectMatchById);
				//截取毫秒    
				map.put("millisecond",openlog[2].replace("T:", "").substring(13).replace("(", "").replace(")", ""));
				list.add(map);
			}
			selectByOpenno.put("openlogs", list);
		}
		return selectByOpenno;
	}
	
}
