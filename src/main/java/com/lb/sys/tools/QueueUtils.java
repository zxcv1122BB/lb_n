package com.lb.sys.tools;

import java.util.LinkedList;

import net.sf.json.JSONObject;

public class QueueUtils {
	/***
	 * 
	 * type 1:先入先出,2:先入后出 maxSize: LinkedList.maxSize
	 * 
	 * @param list
	 * @param value
	 * @param type
	 * @param maxSize
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized LinkedList queue(LinkedList<Object> list, Object value, int type, int maxSize) {
		JSONObject jo = JSONObject.fromObject(value);
		String match_id = jo.getString("match_id");
		for (Object object : list) {
			JSONObject joItem = JSONObject.fromObject(object);
			String match_id_Item = joItem.getString("match_id");
			if(match_id_Item.equals(match_id)) {
				int index = list.indexOf(object);
				list.set(index, value);
				return list;
			}
		}
		
		LinkedList<Object> linkedList = new LinkedList<Object>();
		linkedList.addAll(list);
		if (linkedList.size() < maxSize) {
			if (type == 1) {
				linkedList.offerFirst(value);
			} else {
				linkedList.offerLast(value);
			}
		} else {
			if (type == 1) {
				linkedList.pollFirst();
				linkedList.offerLast(value);
			} else {
				linkedList.pollLast();
				linkedList.offerFirst(value);
			}
		}


		return linkedList;
	}
}
