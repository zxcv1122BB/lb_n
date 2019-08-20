package com.lb.sys.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

public class ServerStateUtils {

	private static final Log logger = LogFactory.getLog(ServerStateUtils.class);
	// cpu、mem(物理内存)、swap(交换分区)命令
	private static final String CPU_MEM_SWAP_SHELL = "top -b -n 1";
	// 系统磁盘命令
	private static final String FILES_SHELL = "df -hl";
	private static final String[] COMMANDS = { CPU_MEM_SWAP_SHELL, FILES_SHELL };
	// liunx 行分隔符
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String JAVA_PID_SHELL = "ps -ef";
	private static final String SHELL_COMMAND = "/ls/script/ls_restart.sh";

	/**
	 * 获取服务器相关信息 cpu、内存、服务器信息、java虚拟机信息、堆/非堆
	 * 
	 * @return
	 */
	public static Map<String, Map<String, String>> getServerStateInfo() {
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().startsWith("linux")) {
			return disposeResultMessage();
		}
		return null;
	}

	/**
	 * 直接在本地执行 shell
	 * 
	 * @param commands
	 *            执行的脚本
	 * @return 执行结果信息
	 */
	private static Map<String, String> runLocalShell(String[] commands) {
		Runtime runtime = Runtime.getRuntime();
		Map<String, String> map = new HashMap<>();
		StringBuilder stringBuffer;
		BufferedReader reader = null;
		Process process;
		try {
			for (String command : commands) {
				stringBuffer = new StringBuilder();
				process = runtime.exec(command);
				InputStream inputStream = process.getInputStream();
				reader = new BufferedReader(new InputStreamReader(inputStream));
				String buf;
				while ((buf = reader.readLine()) != null) {
					// 舍弃PID 进程信息
					if (buf.contains("PID")) {
						break;
					}
					stringBuffer.append(buf.trim()).append(LINE_SEPARATOR);
				}
				// 每个命令存储自己返回数据-用于后续对返回数据进行处理
				map.put(command, stringBuffer.toString());
			}
		} catch (IOException e) {
			logger.error("执行shell命令异常：" + e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.error("执行shell命令过程中读取流关闭异常：" + e.getMessage());
			}
		}
		return map;
	}

	/**
	 * 处理 shell 返回的信息
	 * 
	 * 具体处理过程以服务器返回数据格式为准 不同的Linux 版本返回信息格式不同
	 *
	 * @return 最终处理后的信息
	 */
	private static Map<String, Map<String, String>> disposeResultMessage() {
		Map<String, String> result = runLocalShell(COMMANDS);
		if (result == null || result.size() < 1) {
			return null;
		}
		Map<String, Map<String, String>> resultMap = new HashMap<>();
		for (String command : COMMANDS) {
			String commandResult = result.get(command);
			if (null == commandResult) {
				continue;
			}
			if (CPU_MEM_SWAP_SHELL.equals(command)) {
				String[] strings = commandResult.split(LINE_SEPARATOR);
				for (String line : strings) {
					Map<String, String> map = new HashMap<>();
					// 转大写处理
					line = line.toUpperCase();
					line = line.replaceAll("\\s+", "");
					if (line.contains("CPU(S):")) {
						logger.info("cpu:"+line);
						// cpu数据结果格式化：Cpu(s): 0.0%us, 0.0%sy, 0.0%ni,100.0%id, 0.0%wa, 0.0%hi, 0.0%si,0.0%st
						//用户空间占用CPU百分比,内核空间占用CPU百分比,户进程空间内改变过优先级的进程占用CPU百分比,空闲CPU百分比
						//等待输入输出的CPU时间百分比,等待输入输出的CPU时间百分比,硬件中断,软件中断,实时
						List<String> keysList = new ArrayList<>();
						keysList.add("US");
						keysList.add("SY");
						keysList.add("NI");
						keysList.add("ID");
						keysList.add("WA");
						keysList.add("HI");
						keysList.add("SI");
						keysList.add("ST");
						String[] cpus = line.split(":")[1].split(",");
						for (String value : cpus) {
							for (int i = 0; i < keysList.size(); i++) {
								String itemKey = keysList.get(i);
								if(!value.contains(itemKey))continue;
								String new_value = value.replace(itemKey, "").trim();
								String keyLower = itemKey.toLowerCase();
								map.put(keyLower, new_value);
							}
						}
						map.put("count", Integer.toString(Runtime.getRuntime().availableProcessors()));
						resultMap.put("cpu", map);
					} else if (line.contains("MEM:")) {
						logger.info("mem/swap:" + line);
						// mem数据结果格式化：Mem: 32826948k total, 1360332k used, 31466616k free, 135952k
						// 内存总计
						String mem = line.split(":")[1].replaceAll("\\.", ",");
						String[] mems = mem.split(",");
						List<String> keysList = new ArrayList<>();
						keysList.add("TOTAL");
						keysList.add("USED");
						keysList.add("FREE");
						keysList.add("BUFFERS");
						keysList.add("BUFF/CACHE");
						for (String value : mems) {
							for (int i = 0; i < keysList.size(); i++) {
								String itemKey = keysList.get(i);
								if(!value.contains(itemKey))continue;
								String new_value = value.replace(itemKey, "").trim();
								if (!new_value.contains("k")) new_value = new_value + "k";
								String keyLower = itemKey.toLowerCase();
								String new_value_unit = disposeUnit(new_value);
								if(keyLower.contains("buffers") || keyLower.contains("buff/cache")) {
									map.put("buffers", new_value_unit);
								}else {
									map.put(keyLower, new_value_unit);
								}
							}
						}
						resultMap.put("mem", map);
					}
				}
			} else if (FILES_SHELL.equals(command)) {
				logger.info("disk:" + commandResult);
				String[] strings = commandResult.split(LINE_SEPARATOR);
				BigDecimal size = new BigDecimal(0);
				BigDecimal used = new BigDecimal(0);
				for (int i = 0; i < strings.length - 1; i++) {
					if (i == 0)
						continue;
					String ss = strings[i].replaceAll("\\s+", ",");
					String[] strs = ss.split(",");
					if (strs.length == 1)
						continue;
					size = size.add(disposeUnitConvertG(strs[1]));
					used = used.add(disposeUnitConvertG(strs[2]));
				}
				Map<String, String> map = new HashMap<>();
				// 磁盘总大小
				map.put("total", disposeUnit(size + "g"));
				// 磁盘已使用
				map.put("used", disposeUnit(used + "g"));
				// 磁盘空闲
				map.put("free", disposeUnit((size.subtract(used)) + "g"));
				resultMap.put("disk", map);
			}
		}
		// ===================jvm================

		Runtime jvm = Runtime.getRuntime();
		Map<String, String> map = new HashMap<>();
		// jvm 可以使用的总内存 以字节(B)为单位
		String total = String.valueOf(jvm.totalMemory()/1024);
		if (!total.contains("k"))
			total = total + "k";
		map.put("total", disposeUnit(total));
		// jvm 空闲的内存
		String free = String.valueOf(jvm.freeMemory()/1024);
		if (!free.contains("k"))
			free = free + "k";
		map.put("free", disposeUnit(free));
		// jvm 已使用的内存
		String used = String.valueOf((jvm.totalMemory() - jvm.freeMemory())/1024);
		if (!used.contains("k"))
			used = used + "k";
		map.put("used", disposeUnit(used));
		logger.info("jvm:" + map.toString());
		resultMap.put("jvm", map);

		// ===================system================
		map = new HashMap<>();
		OperatingSystemMXBean system = ManagementFactory.getOperatingSystemMXBean();
		// 操作系统类型
		map.put("type", system.getName());
		// 操作系统架构
		map.put("arch", system.getArch());
		// 操作系统版本
		map.put("version", system.getVersion());
		// 服务器名称
		String hostname = "localhost.unknow";
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error("获取服务器名称异常："+e.getMessage());
		}
		map.put("name", hostname);
		map.put("ip", getLinuxLocalIp());
		resultMap.put("system", map);

		// ===================java================
		map = new HashMap<>();
		RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
		// java 的名称
		map.put("name", System.getProperty("java.vm.name"));
		map.put("vendor",System.getProperty("java.vendor"));
		// java 的安装路径
		map.put("home", System.getProperty("java.home"));
		// java 的版本
		map.put("version",System.getProperty("java.version"));
		// java 启动时间
		map.put("startTime", DateUtils.getDateString(new Date(mxBean.getStartTime())));
		// java 运行时间
		map.put("runTime", DateUtils.secToTime(Integer.valueOf(mxBean.getUptime() + "")));
		resultMap.put("java", map);
		// 堆与非堆
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		// 堆
		map = new HashMap<>();
		MemoryUsage heap = memoryMXBean.getHeapMemoryUsage();
		logger.info("Heap Memory Usage:"+heap);
		// 初始大小
		Long heapInit = heap.getInit();
		if(heapInit<0) {
			heapInit = 0L;
		}else {
			heapInit = heapInit / 1024;
		}
		map.put("heapInit", disposeUnit(heapInit + "k"));
		// 已用内存
		Long heapUsed = heap.getUsed();
		if(heapUsed<0) {
			heapUsed = 0L;
		}else {
			heapUsed = heapUsed / 1024;
		}
		map.put("heapUsed", disposeUnit(heapUsed + "k"));
		// 最大内存
		Long heapMax = heap.getMax();
		if(heapMax<0) {
			heapMax = 0L;
		}else {
			heapMax = heapMax / 1024;
		}
		map.put("heapMax", disposeUnit(heapMax + "k"));
		// 可用内存
		Long heapFree = heapMax - heapUsed;
		if(heapFree<0) {
			heapFree = 0L;
		}
		map.put("heapFree", disposeUnit(heapFree + "k"));
		resultMap.put("heap", map);
		
		// 非堆
		map = new HashMap<>();
		MemoryUsage nonHeap = memoryMXBean.getNonHeapMemoryUsage();
		logger.info("Non-Heap Memory Usage:"+nonHeap);
		// 初始大小
		Long noHeapInit = nonHeap.getInit();
		if(noHeapInit<0) {
			noHeapInit = 0L;
		}else {
			noHeapInit = noHeapInit / 1024;
		}
		map.put("noHeapInit", disposeUnit(noHeapInit + "k"));
		// 已用内存
		Long noHeapUsed = nonHeap.getUsed();
		if(noHeapUsed<0) {
			noHeapUsed = 0L;
		}else {
			noHeapUsed = noHeapUsed / 1024;
		}
		map.put("noHeapUsed", disposeUnit(noHeapUsed + "k"));
		// 最大内存
		Long noHeapMax = nonHeap.getMax();
		if(noHeapMax<0) {
			noHeapMax = 0L;
		}else {
			noHeapMax = noHeapMax / 1024;
		}
		map.put("noHeapMax", disposeUnit(noHeapMax + "k"));
		// 可用内存
		Long notheapfree = noHeapMax - noHeapUsed;
		if(notheapfree<0) {
			notheapfree = 0L;
		}
		map.put("noHeapFree", disposeUnit(notheapfree + "k"));
		resultMap.put("noheap", map);
		return resultMap;
	}

	/**
	 * 处理单位转换
	 * 
	 * @param s
	 *            带单位的数据字符串
	 * 
	 * @return 以K/KB/M/G/T为单位处理后的数值
	 */
	private static String disposeUnit(String s) {
		BigDecimal bg = new BigDecimal(0);
		try {
			s = s.toUpperCase();
			String lastIndex = s.substring(s.length() - 1);
			String num = s.substring(0, s.length() - 1);
			BigDecimal size = new BigDecimal(1024);
			bg = new BigDecimal(num);
			if ("K".equals(lastIndex) || "KB".equals(lastIndex)) {
				if (bg.compareTo(size) == 1 || bg.compareTo(size) == 0) {
					s = (bg.divide(size)) + "m";
					return disposeUnit(s);
				}
				s = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "KB";
				return s;
			} else if ("M".equals(lastIndex)) {
				if (bg.compareTo(size) == 1 || bg.compareTo(size) == 0) {
					s = (bg.divide(size)) + "g";
					return disposeUnit(s);
				}
				s = bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "M";
				return s;
			} else if ("G".equals(lastIndex)) {
				if (bg.compareTo(size) == 1 || bg.compareTo(size) == 0) {
					s = (bg.divide(size).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()) + "T";
					return s;
				}
				return bg.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() + "G";
			}
		} catch (NumberFormatException e) {
			logger.error("获取服务器内存大小单位转换异常：" + e.getMessage());
			return bg + "k";
		}
		return bg + "k";
	}

	/**
	 * 处理单位转换 K/KB/M/T 最终转换为G 处理
	 *
	 * @param s
	 *            带单位的数据字符串
	 * @return 以G 为单位处理后的数值
	 */
	private static BigDecimal disposeUnitConvertG(String s) {
		BigDecimal bg = new BigDecimal(0);
		try {
			s = s.toUpperCase();
			String lastIndex = s.substring(s.length() - 1);
			String num = s.substring(0, s.length() - 1);
			if (num.length() == 0)
				return bg;
			bg = new BigDecimal(num);
			BigDecimal size = new BigDecimal(1024);
			if (lastIndex.equals("G")) {
				return bg.setScale(3, BigDecimal.ROUND_HALF_UP);
			} else if (lastIndex.equals("T")) {
				return bg.multiply(size).setScale(3, BigDecimal.ROUND_HALF_UP);
			} else if (lastIndex.equals("M")) {
				return bg.divide(size).setScale(3, BigDecimal.ROUND_HALF_UP);
			} else if (lastIndex.equals("K") || lastIndex.equals("KB")) {
				size = size.multiply(size).setScale(3, BigDecimal.ROUND_HALF_UP);
				return bg.divide(size).setScale(3, BigDecimal.ROUND_HALF_UP);
			}
		} catch (NumberFormatException e) {
			logger.error("获取服务器磁盘大小统一转换成G异常：" + e.getMessage());
			return bg;
		}
		return bg;
	}
	
	/**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
	private static String getLinuxLocalIp(){
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            logger.error("获取服务器ip异常："+ex.getMessage());
            ip = "127.0.0.1";
        }
        return ip;
    }
	
	public static int executeShellCommandStart() {
		int result = 0 ;
		try {
			StringBuilder stringBuilder = getExecuteShellCommandResult();
			if(stringBuilder==null || stringBuilder.length()==0) {
				Runtime.getRuntime().exec(SHELL_COMMAND);
				logger.info("execute a shell command start success");
			}
			result = 1;
		} catch (Exception e) {
			logger.error("execute a shell command start exception info：" + e.getMessage()); 
		}
		return result;
	}
	
	private static StringBuilder getExecuteShellCommandResult() {
		StringBuilder stringBuilder = null;
		BufferedReader reader = null;
		try {
			stringBuilder = new StringBuilder();
			Process process = Runtime.getRuntime().exec(JAVA_PID_SHELL);
			InputStream inputStream = process.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String buf = null;
			logger.info("reader:"+reader.readLine());
			while ((buf = reader.readLine()) != null) {
				 if(buf.contains("java -jar")) {
					 String[] strs = buf.split("\\s+"); 
					 stringBuilder.append(strs[1]).append(" ");
				 }
			}
			logger.info("execute a shell command result success");
		} catch (Exception e) {
			logger.error("execute a shell command result  exception info：" + e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				logger.error("execute a shell command result reader io exception info：" + e.getMessage());
			}
		}
		return stringBuilder;
	} 
	
	public static int executeShellCommandShutdown() {
		int result = 0;
		Runtime runtime = Runtime.getRuntime();
		StringBuilder stringBuilder = getExecuteShellCommandResult();
		logger.info("command result:"+stringBuilder.toString());
		try {
			if(stringBuilder!=null) {
				runtime.exec("kill -9 "+stringBuilder.toString());
			}
			logger.info("execute a shell command shutdown success");
			result = 1;
		} catch (Exception e) {
			logger.error("execute a shell command shutdown exception info：" + e.getMessage());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static void executeHttpClient() {
		try {
			JSONObject jsonObj = new JSONObject();
			String mac = CommonTools.getMacAddress();
			String url =  GetPropertiesValue.getValue("URL", "total_backstage_url")+"/system/getServerValidate";
			jsonObj.put("controlMac",mac);
			jsonObj.put("timeStamp",System.currentTimeMillis());
			String rsaData = ControlTools.escapeEncrypt(jsonObj.toString());
			Map<String,Object> params = new HashMap<>();
			params.put("RSA_data",rsaData);
			String result = HttpClientUtil.doTotalBackStageClient(url,params);
			logger.info("返回结果："+result);
			if(result!=null && !"".equals(result)) {
				Map<String,Object> resultMap = JSONUtils.jsonToMap(result);
				String code = resultMap.get("code").toString();
				if("200".equals(code)) {
					Map<String,Object> resultBody = (Map<String, Object>) resultMap.get("body");
					String status = resultBody.get("status")!=null?resultBody.get("status").toString():"2";
					if("1".equals(status)) {//normal
						executeShellCommandStart();
					}else if("0".equals(status)) {//no normal
						executeShellCommandShutdown();
					}else {
						logger.error("incorrect data format");
					}
				}else {
					logger.error(resultMap.get("msg"));
				}
			}else {
				logger.error("remote request failure");
			}
		} catch (Exception e) {
			 logger.error("remote request exception info："+e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void executeHttpClient(String privateKey) {
		try {
			JSONObject jsonObj = new JSONObject();
			String mac = CommonTools.getMacAddress();
			String url =  GetPropertiesValue.getValue("URL", "total_backstage_url")+"/system/getServerValidate";
			jsonObj.put("controlMac",mac);
			jsonObj.put("timeStamp",System.currentTimeMillis());
			String rsaData = ControlTools.escapeEncrypt(privateKey,jsonObj.toString());
			Map<String,Object> params = new HashMap<>();
			params.put("RSA_data",rsaData);
			String result = HttpClientUtil.doTotalBackStageClient(url,params);
			logger.info("返回结果："+result);
			if(result!=null && !"".equals(result)) {
				Map<String,Object> resultMap = JSONUtils.jsonToMap(result);
				String code = resultMap.get("code").toString();
				if("200".equals(code)) {
					Map<String,Object> resultBody = (Map<String, Object>) resultMap.get("body");
					String status = resultBody.get("status")!=null?resultBody.get("status").toString():"2";
					if("1".equals(status)) {//normal
						executeShellCommandStart();
					}else if("0".equals(status)) {//no normal
						executeShellCommandShutdown();
					}else {
						logger.error("incorrect data format");
					}
				}else {
					logger.error(resultMap.get("msg"));
				}
			}else {
				logger.error("remote request failure");
			}
		} catch (Exception e) {
			 logger.error("remote request exception info："+e.getMessage());
		}
	}
}
