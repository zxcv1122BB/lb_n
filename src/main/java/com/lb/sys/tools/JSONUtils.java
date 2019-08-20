package com.lb.sys.tools;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;

public class JSONUtils {
	private static ObjectMapper mapper = new MyObjectMapper();

	public static ObjectMapper getMapper(){
		return mapper;
	}

	public static <T> T parse(String value, Class<T> clz) {

		if (StringUtils.isEmpty(value)) {
			return null;
		}
		try {
			return mapper.readValue(value, clz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> T parse(byte[] bytes, Class<T> clz) {
		try {
			return mapper.readValue(bytes, clz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> T parse(InputStream ins, Class<T> clz) {
		try {
			return mapper.readValue(ins, clz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T> T parse(Reader reader, Class<T> clz) {
		try {
			return mapper.readValue(reader, clz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T update(String value, T object) {
		try {
			return (T) mapper.readerForUpdating(object).readValue(value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String writeValueAsString(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static void write(OutputStream outs, Object o) {
		try {

			mapper.writeValue(outs, o);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static void write(Writer writer, Object o) {
		try {
			mapper.writeValue(writer, o);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String toString(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("deprecation")
	public static String toString(Object o, Class<?> clz) {
		try {
			return mapper.writerWithType(clz).writeValueAsString(o);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static byte[] toBytes(Object o) {
		try {
			return mapper.writeValueAsBytes(o);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> toMap(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);  
		return JSONObject.fromObject(jsonObject);
	}
	
	@SuppressWarnings("unchecked")
	public static  <T> T  toBean(String json,Class<T> t) {
		JSONObject jsonObject = JSONObject.fromObject(json);  
		return (T) JSONObject.toBean(jsonObject, t); 
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,Object> jsonToMap(String json){
		JSONObject jsonObject = JSONObject.fromObject(json);  
		return JSONObject.fromObject(jsonObject);
	}
	
}
