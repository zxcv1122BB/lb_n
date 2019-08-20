package com.lb.sys.tools;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lb.excel.ExcelModel;

public class ExcelTool {

	// 封装一个方法将map集合拆分成key和value的集合
	public ExcelModel returnExcelModel(Map<String, String> map) {
		String[] arrKey = new String[map.size()];
		String[] arrValue = new String[map.size()];
		// 计数器
		int i = 0;
		String arrStr = "";
		// 转换成 数组
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			arrKey[i] = entry.getKey();
			arrValue[i] = entry.getValue();
			arrStr += entry.getValue();
			i++;
		}

		return new ExcelModel(arrKey, arrValue, arrStr);
	}

	// 将查询的条件集合 转换成查询的时候使用的字符串信息
	public static String listPingStr(List<String> strList) {
		StringBuilder builder = new StringBuilder();
		// 首先根据集合的长度判断是否为只有一个查询列，如果是，则拼接,
		if (strList.size() < 1) {
			return null;
		} else {
			for (int i = 0; i < strList.size(); i++) {
				builder.append(strList.get(i));
				if (i < (strList.size() - 1)) {
					builder.append(",");
				}
			}
			return builder.toString();
		}
	}

	public static String listPingStrInt(List<Integer> strList) {
		StringBuilder builder = new StringBuilder();
		// 首先根据集合的长度判断是否为只有一个查询列，如果是，则拼接,
		if (strList.size() < 1) {
			return null;
		} else {
			builder.append("(");
			for (int i = 0; i < strList.size(); i++) {
				builder.append(strList.get(i));
				if (i < (strList.size() - 1)) {
					builder.append(",");
				}
			}
			builder.append(")");
			return builder.toString();
		}
	}


}
