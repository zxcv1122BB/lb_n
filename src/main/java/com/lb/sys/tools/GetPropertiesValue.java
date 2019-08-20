package com.lb.sys.tools;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.lb.weblListener.MyServletContextListener;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 获取指定属性名的属性值
 */
public class GetPropertiesValue {
	
	/**
	 * 
	 * @param keyName 文件名
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	public static String getValue(String keyName,String propertyName) {
		Map<String,Map<String,String>> properties = MyServletContextListener.propertiesMap;
		for (Map.Entry<String, Map<String, String>> entry :properties.entrySet()) {
			String keys = entry.getKey();
			if(keys.equals(keyName)) {
				Map<String,String> childMap = entry.getValue();
				for(Map.Entry<String, String> child_entry :childMap.entrySet()) {
					String child_key = child_entry.getKey();
					if(child_key.equals(propertyName)) {
						return child_entry.getValue();
					}
				}
			}
		}
		return null;
	}
	
	public static String[] getValueArray(String keyName,String propertyName) {
		String[] values = null;
		String value = getValue(keyName,propertyName);
		if (value!=null&&value.contains(",")) {
			values = value.split(",");
		}
		return values;
	}
	
	/**
	 * 
	 * @param keyName 文件名
	 * @return 属性值
	 */
	public static Map<String,String> getMapValue(String keyName) {
		Map<String,Map<String,String>> properties = MyServletContextListener.propertiesMap;
		for (Map.Entry<String, Map<String, String>> entry :properties.entrySet()) {
			String keys = entry.getKey();
			if(keys.equals(keyName)) {
				 return entry.getValue();
			}
		}
		return null;
	}
	
	
	/**
	 *@describe:以json的格式定义value,返回Map集合
	 *@param key
	 *@return:
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getJson2Map(String keyName,String propertyName) {
		Map<String, String> map = null;
		String value = getValue(keyName,propertyName);
		if (!StringUtils.isEmpty(value)) {
			map=JSONObject.fromObject(value);
		}
		return map;
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static <T> List<T> getJson2List(String key,String propertyName,Class<T> t){
		String value = getValue(key,propertyName);
		JSONArray fromObject = JSONArray.fromObject(value);
		return JSONArray.toList(fromObject,t);
	}
}