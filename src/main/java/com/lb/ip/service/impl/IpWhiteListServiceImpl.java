package com.lb.ip.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.ip.model.IpWhiteList;
import com.lb.ip.model.IpWhiteListExample;
import com.lb.ip.service.IpWhiteListService;
import com.lb.ip.tools.IpWhiteListTools;
import com.lb.sys.dao.IpWhiteListMapper;

/***
 * ip配置 业务逻辑实现层
 * 
 * @author ASUS
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class IpWhiteListServiceImpl implements IpWhiteListService {

	@Autowired
	private IpWhiteListMapper ipWhiteListMapper;

	/***
	 * 查询记录条数
	 */
	@Override
	public long countByExample(IpWhiteListExample example) {
		// TODO Auto-generated method stub
		return ipWhiteListMapper.countByExample(example);
	}

	/**
	 * 真实删除一条记录
	 */
	@Override
	public int deleteByExample(IpWhiteListExample example) {
		// TODO Auto-generated method stub
		return ipWhiteListMapper.deleteByExample(example);
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(IpWhiteList record) {
		// 标记参数
		int flag = 0;
		boolean red = IpWhiteListTools.red(record.getIpaddress());
		if (red == false) {
			flag = 101;
		} else {
			// 判断ip是否已经在数据库中存在
			if ((ipWhiteListMapper.selectIpYorN(record.getIpaddress())) <= 0) {
				int i = ipWhiteListMapper.insert(record);
				if (i > 0) {
					flag = 100;
				} else {
					flag = 102;
				}
			} else {
				flag = 103;
			}
		}
		return flag;
	}

	@Override
	public int insertSelective(IpWhiteList record) {
		// TODO Auto-generated method stub
		return 0;
	}

	/***
	 * 查询所有Ip白名单
	 */
	// @Override
	// public List<IpWhiteList> selectByExample(IpWhiteListExample example) {
	// return ipWhiteListMapper.selectByExample(example);
	// }

	@Override
	public List<IpWhiteList> selectByExample(Integer stuas) {
		return ipWhiteListMapper.selectByExample(stuas);
	}

	@Override
	public IpWhiteList selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * 修改
	 */
	@Override
	public int updateByExampleSelective(IpWhiteList record, IpWhiteListExample example) {
		// 标记参数
		// 判断ip是否
		int flag = 0;
		boolean red = IpWhiteListTools.red(record.getIpaddress());
		if (red == false) {
			flag = 101;
		} else {
			int i = ipWhiteListMapper.updateByExampleSelective(record, example);
			if (i > 0) {
				flag = 100;
			} else {
				flag = 102;
			}
		}
		return flag;
	}

	@Override
	public int updateByExample(IpWhiteList record, IpWhiteListExample example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKeySelective(IpWhiteList record) {
		// 标记参数
		int flag = 0;
		boolean red = IpWhiteListTools.red(record.getIpaddress());
		if (red == false) {
			flag = 101;
		} else {
			// 判断要修改的Ip是否已经存在
			if ((this.selectIpYorN(record.getIpaddress())) <= 0) {
				int i = ipWhiteListMapper.updateByPrimaryKeySelective(record);
				if (i > 0) {
					flag = 100;
				} else {
					flag = 102;
				}

			} else {
				flag = 103;
			}
		}
		return flag;
	}

	@Override
	public int updateByPrimaryKey(IpWhiteList record) {
		// TODO Auto-generated method stub
		return 0;
	}

	/****
	 * 逻辑删除 禁用一个ip
	 * 
	 * @return 返回状态码（100:删除成功;101:删除了空的对象,删除失败;102:删除失败）
	 */
	@Override
	public int deleteIp(IpWhiteList ipWhiteList) {
		// 标记参数
		int flag = 0;
		if (ipWhiteList == null) {
			flag = 101;
		} else {
			int i = ipWhiteListMapper.deleteIp(ipWhiteList);
			if (i > 0) {
				flag = 100;
			} else {
				flag = 102;
			}
		}
		return flag;
	}

	/**
	 * 查询某一个ip是否已经在数据库中存在
	 */
	@Override
	public Integer selectIpYorN(String ip) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("configure", "backstageLoginLimit");
		int on_off = ipWhiteListMapper.on_off(map);
		if (on_off == 1) {
			// 首先去除两端空格
			return ipWhiteListMapper.selectIpYorN(ip.replaceAll(" ", ""));
		} else {
			return 1;
		}

	}

	// 查询是否需要将截至时间相加
	@Override
	public String byDeadline_bet(String dateStr) {
		String datenew = dateStr;
		// 首先判断是否需要加上时间
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("configure", "closingBetTime");
		int on_off = ipWhiteListMapper.on_off(map);
		if (on_off > 0) {
			Map<String, Object> byConfigure = ipWhiteListMapper.byConfigure(map);
			int sys_config1 = Integer.parseInt(byConfigure.get("sys_config1").toString());
			// 将日期数据相加
			SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date;
			try {
				date = formatDate.parse(dateStr);
				Long nowValue = date.getTime();// date的毫秒数
				Long afterHour = nowValue + 1 * (sys_config1*60) * 1000;// date加十分钟的毫秒数
				Date afterHourDate = new Date(afterHour);
				datenew = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(afterHourDate);
			} catch (ParseException e) {
				return datenew;
			}
		}
		return datenew;
	}

	/***
	 * 验证用户是否可以登录
	 */
	@Override
	public boolean isLocking(String Uid) {
		// 首先获取到service对象
		// () SpringUtil.getBean("quartzJobServiceImpl");
		return false;
	}

}
