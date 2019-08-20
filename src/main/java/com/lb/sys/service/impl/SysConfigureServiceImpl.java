/**
 * 
 */
package com.lb.sys.service.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lb.sys.dao.SysConfigureMapper;
import com.lb.sys.model.SysConfigure;
import com.lb.sys.model.SysConfigureModule;
import com.lb.sys.service.ISysConfigureService;
import com.lb.sys.tools.FileOperator;
import com.lb.sys.tools.SocketUtils;
import com.lb.sys.tools.StringUtil;

/**
 * @describe 
 */
@Service
@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class SysConfigureServiceImpl implements ISysConfigureService {
	
	private final Log LOGGER = LogFactory.getLog(SysConfigureServiceImpl.class);
	
	@Autowired
	private SysConfigureMapper sysConfigureMapper;
	
	
	@Override
	public List<SysConfigure> querySysConfigure(Map<String, Object> map) {
		List<SysConfigure> list= sysConfigureMapper.querySysConfigure(map);
		if(list != null) {
			for(int i=0;i<list.size();i++) {
				if(map.get("roleId") != null &&
						!"1".equals(map.get("roleId").toString())
						&& "isUseCacheData".equals(list.get(i).getConfigure())) {
					list.remove(i);
				}
				list.get(i).calcConfigureList();
			}
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.lb.sys.service.ISysConfigureService#updateSysConfigure(com.lb.sys.model.SysConfigure)
	 */
	@SuppressWarnings("unused")
	@Override
	public int updateSysConfigure(Collection<SysConfigure> configure) {
		int result = 0;
		String isUseCacheData = null;
		String url=null,version=null;
		for (SysConfigure sysConfigure : configure) {
			//boolean flag = false;
			if(sysConfigure != null) {
				if(sysConfigure.getDataType() != null) {
					if(sysConfigure.getDataType() == 1) {
						if(StringUtil.isBlank(sysConfigure.getSysConfig1())
								|| Integer.valueOf(sysConfigure.getSysConfig1()) <= 0) {
							throw new RuntimeException("修改数字不能小于等于0");
						}
						if(sysConfigure.getIsInput() == 3 || sysConfigure.getIsInput() == 4) {
							if(StringUtil.isBlank(sysConfigure.getSysConfig2())
									|| Integer.valueOf(sysConfigure.getSysConfig2()) <= 0) {
								throw new RuntimeException("修改数字不能小于等于0");
							}
						}
					}else if(sysConfigure.getDataType() == 3 
							|| sysConfigure.getDataType() == 4) {
						sysConfigure.calcConfigureRange();
					}
				}
				
				/*if("isUseCacheData".equals(sysConfigure.getConfigure())) {
					flag = true;
					isUseCacheData = jedisClient.get("isUseCacheData");
				}*/
				
				try {
					result = sysConfigureMapper.updateSysConfigure(sysConfigure);
					Integer config_id = sysConfigure.getId();
					if(result>0) {
						notifyWsUpdateWord(sysConfigure);
						if(config_id == 29) {//主要是为了生成IOS下载链接-plist文件
							url = sysConfigure.getSysConfig1();
						}else if(config_id==55) {
							version = sysConfigure.getSysConfig1();
						}
					}
				} catch (Exception e) {
					//jedisClient.set("isUseCacheData", isUseCacheData);
					LOGGER.error(e.getMessage());
					throw new RuntimeException("异常，修改失败",e.getCause());
				}
				/*if(result>0 && flag) {
					if(jedisClient != null) {
						jedisClient.set("isUseCacheData", String.valueOf(sysConfigure.getOnOff()));
					}
				}*/
			}
		}
		//根据ios安装包路径和版本号 生成ios_plist文件
		if(url!=null && version!=null) {
			try {
				Map<String,Object> t_params = new HashMap<>();
				t_params.put("configure", "webNameConfigure");
				t_params = this.getOnlyConfigure(t_params);
				FileOperator.createPlist(url,version,t_params.get("sysConfig1").toString());
			} catch (IOException e) {
				LOGGER.error("根据ios安装包路径和版本号 生成ios_plist文件异常："+e.getMessage());
			}
		}
		return result;
	}

	private void notifyWsUpdateWord(SysConfigure sysConfigure) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(3000);
					// configure=fifterKeyWord时 向websocket发送消息提示字段更新
					if("fifterKeyWord".equals(sysConfigure.getConfigure())) {
						String info = SocketUtils.getUserInfo("API.ReloadReg", "");
						if("ok".equals(info)) {
							LOGGER.info("向websocket发送消息, 更新过滤关键字: "+info);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/* (non-Javadoc)
	 * @see com.lb.sys.service.ISysConfigureService#querySysConfigureModule()
	 */
	
	@Override
	public List<SysConfigureModule> querySysConfigureModule() {
		return sysConfigureMapper.querySysConfigureModule();
	}

	/* (non-Javadoc)
	 * @see com.lb.sys.service.ISysConfigureService#getOnlyConfigure()
	 */
	@Override
	public Map<String,Object> getOnlyConfigure(Map<String, Object> map) {
		return sysConfigureMapper.getOnlyConfigure(map);
	}

	@Override
	public Map<String, Object> qryByConfigure(String configureName) {
		return sysConfigureMapper.qryByConfigure(configureName);
	}

	@Override
	public int resetPrivateKey(Map<String, Object> map) {
		return sysConfigureMapper.resetPrivateKey(map);
	}

	
}
