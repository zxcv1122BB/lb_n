package com.lb.sys.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jsonrpc4j.JsonRpcClient;

public class SocketUtils {
	final static String IP =  "127.0.0.1";
	final static Integer PORT =  3333;
	private static final Logger logger = LoggerFactory.getLogger(SocketUtils.class);

	@SuppressWarnings("resource")
	public static String getUserInfo(String method, String param) {
		String result = null;
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(IP, PORT), 1000);//设置连接请求超时时间10 s
			socket.setSoTimeout(5000);//设置读操作超时时间30 s
			JsonRpcClient client = new JsonRpcClient();
			InputStream ips = socket.getInputStream();
			OutputStream ops = socket.getOutputStream();
			result = client.invokeAndReadResponse(method, new Object[] { param }, String.class, ops, ips);
		} catch (Throwable e) {
			logger.error("参数:" + method + "," + param + ",调用Go程序异常：" + e.getMessage());
		}
		return result;
	}

}
