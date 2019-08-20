package com.lb.sys.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.core.io.ClassPathResource;

public class ScriptEngineUtil {

	private static ScriptEngine engine;

	static {
		try {
			//File file = ResourceUtils.getFile("classpath:lottery_open_calculate.js");
//			File file = ResourceUtils.getFile("C:\\Users\\Administrator\\Documents\\HBuilderProject\\Demo\\js\\lottery_open_calculate.js");
			//FileReader fr = new FileReader(file);
			ClassPathResource classPathResource = new ClassPathResource("lottery_open_calculate.js");
			InputStream is = classPathResource .getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			ScriptEngineManager manager = new ScriptEngineManager();
			engine = manager.getEngineByName("javascript");
			engine.eval(isr);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String execute(String functionName, String...args) {
		try {
			// 调用JS方法
			StringBuffer sb = new StringBuffer();
			sb.append("('");
			for (int i = 0; i < args.length; i++) {
				if (i<args.length-1) {
					sb.append(args[i]+"','");
				}else {
					sb.append(args[i]);
				}
			}
			sb.append("');");
//			Object result = engine.eval(functionName + "('" + arg1 + "','" + arg2 + "');");
			Object result = engine.eval(functionName + sb.toString());
			// 执行JS脚本
			return result.toString();
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return "";
	}

	
	
	
	
	
	
	
	
	
	

}
