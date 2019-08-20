package com.lb.sys.tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
/*
 * 解析properties文件,return Map<String,String> or null;
 * 2017年12月11日16:01:59
 * 
 */
public class ParseProFileUtil {
	private static Map<String, Properties> map = new HashMap<>();
	private static Map<String, Properties> fileMap = new HashMap<>();
	private static Properties pro = null;
	private static InputStream is;
	
	@SuppressWarnings("rawtypes")
	public static Map<String,String> parseProFile(String file_name){
		try {
			Map<String,String> proMap = new  HashMap<String,String>();
			if(!map.containsKey(file_name)) {
				pro = new Properties();
				is = ParseProFileUtil.class.getClassLoader().getResourceAsStream(file_name);
				pro.load(is);
				is.close();
				map.put(file_name, pro);
			}
			pro = map.get(file_name);
			Iterator it=pro.entrySet().iterator();
			while(it.hasNext()){
			    Map.Entry entry=(Map.Entry)it.next();
			    String key = entry.getKey()+"";
			    String value = entry.getValue()+"";
			    proMap.put(key, value.trim());
			}			
			return proMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getProperties(String fileName){
		try {
			Map<String,String> proMap = new  HashMap<String,String>();
			if(!fileMap.containsKey(fileName)) {
				pro = new Properties();
				pro.load(new FileInputStream(new File(fileName)));
				fileMap.put(fileName, pro);
			}
			pro = fileMap.get(fileName);
			Iterator it=pro.entrySet().iterator();
			while(it.hasNext()){
			    Map.Entry entry=(Map.Entry)it.next();
			    String key = entry.getKey()+"";
			    String value = entry.getValue()+"";
			    proMap.put(key, value.trim());
			}			
			return proMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public static Map<String,String> parseProFile(String file_name,boolean ispwd){
		try {
			Map<String,String> proMap = new  HashMap<String,String>();
			if(!map.containsKey(file_name)) {
				pro = new Properties();
				is = ParseProFileUtil.class.getClassLoader().getResourceAsStream(file_name);
				pro.load(is);
				is.close();
				map.put(file_name, pro);
			}
			pro = map.get(file_name);
			Iterator it=pro.entrySet().iterator();
			EncrypAESUtils decode = new EncrypAESUtils();
			while(it.hasNext()){
			    Map.Entry entry=(Map.Entry)it.next();
			    String key = entry.getKey()+"";
			    String value = entry.getValue()+"";
			    value = decode.decode(value);
			    proMap.put(key, value.trim());
			}			
			return proMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getProperties(String fileName,boolean ispwd){
		try {
			Map<String,String> proMap = new  HashMap<String,String>();
			if(!fileMap.containsKey(fileName)) {
				pro = new Properties();
				pro.load(new FileInputStream(new File(fileName)));
				fileMap.put(fileName, pro);
			}
			pro = fileMap.get(fileName);
			Iterator it=pro.entrySet().iterator();
			EncrypAESUtils decode = new EncrypAESUtils();
			while(it.hasNext()){
			    Map.Entry entry=(Map.Entry)it.next();
			    String key = entry.getKey()+"";
			    String value = entry.getValue()+"";
			    value = decode.decode(value);
			    proMap.put(key, value.trim());
			}			
			return proMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
