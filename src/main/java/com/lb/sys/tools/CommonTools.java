package com.lb.sys.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lb.redis.JedisClient;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CommonTools {

	private static Logger logger = LoggerFactory.getLogger(CommonTools.class);
	
	private static String macAddressStr = null;
    private static final String[] windowsCommand = { "ipconfig", "/all" };
    private static final String[] linuxCommand = { "/sbin/ifconfig", "-a" };
    private static final Pattern macPattern = Pattern.compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*",
            Pattern.CASE_INSENSITIVE);

	// LIST TO MAP
	public static Map<String, Map<String, Object>> listToMap(String keyName, List<Map<String, Object>> list) {
		// Hashtable 线程安全
		Map<String, Map<String, Object>> m = new Hashtable<String, Map<String, Object>>();
		try {
			for (Map<String, Object> m1 : list) {
				m.put(m1.get(keyName).toString(), m1);
			}
			return m;
		} catch (Exception e) {
			logger.error("Convert List to Map failed");
			e.printStackTrace();
		}
		return null;
	}
	
	// map to list
	public static List<Map<String, Object>>  MapToList( Map<String, Map<String, Object>> map) {
		List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
		try {
			int i = 0;
			for (String key : map.keySet())
			{
				l.add(i, map.get(key));
				i ++ ;
			}
			return l;
		} catch (Exception e) {
			logger.error("Convert Map to List failed");
			e.printStackTrace();
		}
		return null;
	}
	
	// 从list种查询
	public static List<Map<String, Object>> selectFromList(List<Map<String, Object>> list,String keyName,String keyValue){
		List<Map<String, Object>> newList = list.stream().filter(action->{
			if(keyValue.equalsIgnoreCase( action.get(keyName).toString()))
			     return true; 
			else
				return false;
		}).collect(Collectors.toList());
        return newList;		
	}
	
	// list按keyname排序  flag=falase 降序 
	public static List<Map<String, Object>> sortFromList(List<Map<String, Object>> list,String keyName,boolean flag){
		List<Map<String, Object>> newList = 
		list.stream().sorted((a, b) ->	
			flag?a.get(keyName).toString().compareTo(b.get(keyName).toString()):b.get(keyName).toString().compareTo(a.get(keyName).toString())	
		).collect(Collectors.toList());
        return newList;		
	}
	
	//更新MAP
	public static Map<String, Map<String,Object>> updateMaps ( Map<String, Map<String,Object>> maps,Map<String,Object> map,String key){
		Map<String,Object> newMap = maps.get(key);
		newMap.putAll(map);	
		maps.put(key, newMap);
		return maps;
		
	}
	
	public static Map<String,String> getFieldToIDMap(Map<String, Map<String,Object>> mapList,String fieldName)
	{
		Map<String,String> newMap = new Hashtable<String,String>();
		for (String key : mapList.keySet())
		{
			newMap.put(mapList.get(key).get(fieldName).toString(),key );
		}
		return newMap;
	}
	
	//根据用户名模糊查询 
	public static List<String> search(String name, List<String> list) {
		List<String> results = null;
		Pattern pattern = Pattern.compile(name);
		if(list!=null && !list.isEmpty()) {
			results = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				Matcher matcher = pattern.matcher((CharSequence) list.get(i).replace("\"", ""));
				if (matcher.find()) {
					results.add(list.get(i));
				}
			}
		}
		return results;
	}
	
	 /**
     * 获取多个网卡地址
     * 
     * @return
     * @throws IOException
     */
    private final static List<String> getMacAddressList() {
        final ArrayList<String> macAddressList = new ArrayList<String>();
        try {
			final String os = System.getProperty("os.name");
			String command[] = null;

			if (os.startsWith("Windows")) {
			    command = windowsCommand;
			} else if (os.startsWith("Linux")) {
			    command = linuxCommand;
			} else {
				logger.error("Unknow operating system:" + os);
			}
			// 执行命令
			final Process process = Runtime.getRuntime().exec(command);
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			for (String line = null; (line = bufReader.readLine()) != null;) {
			    Matcher matcher = macPattern.matcher(line);
			    if (matcher.matches()) {
			        macAddressList.add(matcher.group(1));
			        macAddressList.add(matcher.group(1).replaceAll("[-:]",""));//去掉MAC中的“-”
			    }
			}
			process.destroy();
			bufReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return macAddressList;
    }

    /**
     * 获取一个网卡地址（多个网卡时从中获取一个）
     * 
     * @return
     */
    public static String getMacAddress() {
        if (macAddressStr == null || macAddressStr.equals("")) {
            StringBuffer sb = new StringBuffer(); // 存放多个网卡地址用，目前只取一个非0000000000E0隧道的值
            try {
                List<String> macList = getMacAddressList();
                for (Iterator<String> iter = macList.iterator(); iter.hasNext();) {
                    String amac = iter.next();
                    if (!amac.equals("0000000000E0")) {
                        sb.append(amac);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            macAddressStr = sb.toString();
        }
        return macAddressStr;
    }
    
    /**
     * 比较football_match_info表中相应的字段值是否有改动
     * @param redismap 原数据源
     * @param itemMap 新数据源
     * @return
     */
 	public static boolean checkFootBallMatchInfoChange(Map<String,String> redismap, Map<String,String> itemMap) {	
 		Set<String> redisKeySet = redismap.keySet();	
 		Set<String> itemMapKeySet = itemMap.keySet();
 		String base_column = "match_id,league_id,group_id,match_season,banner_number,sessions,letball_number,event_id," + 
 				"league_name,home_team_id,home_team_name,away_team_name,away_team_id," + 
 				"match_date,deadline_bet,match_status,is_hot," + 
 				"is_win_draw_lose,is_letball_win_draw_lose,is_score,is_total_goal,is_up_down_odd_even,is_half_full";
 		String[] base_column_array = base_column.split(",");
 		String itemKey = null;
 		boolean redisFlag = false,itemMapFlag = false;
 		for(int i=0;i<base_column_array.length;i++) {
 			itemKey = base_column_array[i];
 			redisFlag = redisKeySet.contains(itemKey);
 			itemMapFlag = itemMapKeySet.contains(itemKey);
 			if(redisFlag != itemMapFlag)return false;//都包含或者都不包含
 			if(!redisFlag || !itemMapFlag)return false;//限制为都包含
 			if(!redismap.get(itemKey).equals(itemMap.get(itemKey)))return false;//都包含时判断value是否一致
 		}		
 		return true;
 	}
 	
 	/**
     * 比较football_odds_info表中相应的赔率是否有改动
     * @param redismap 原数据源
     * @param itemMap 新数据源
     * @return
     */
 	public static boolean checkFootBallOddsInfoChange(Map<String,String> map1, Map<String,String> map2) {
		boolean contain = false;
		for (String o : map1.keySet()) {
			contain = map2.containsKey(o);
			if (contain) {
				contain = map1.get(o).equals(map2.get(o));
			}
			if (!contain) {
				return false;
			}
		}
		return true;
	}
 	
 	/**
 	 * football_match_info表中热门赛事设置
 	 * @param matchJSON
 	 * @param jedis
 	 * @return
 	 */
 	@SuppressWarnings("unchecked")
	public static synchronized boolean setHot(JSONObject matchJSON,JedisClient jedis) {		
		try {
			String key = "LS_HotSportMatch";
			String hotString = jedis.get(key);			
			JSONArray hotJA = new JSONArray();
			LinkedList<Object> linkList = new LinkedList<>();
			if(StringUtils.isNotEmpty(hotString)) {
				hotJA = JSONArray.fromObject(hotString);
				linkList.addAll(hotJA);
			}
			int id1 = Integer.valueOf(matchJSON.getString("one_type_id"));
			//match_id,sessions,league_name,home_team_id,home_team_name,away_team_id,away_team_name,match_date,deadline_bet,event_id,match_status,issue
			//【热门】篮球和足球需要展示哪些字段			
			String[] base_column_array = {"match_id","sessions","league_name","home_team_id","home_team_name","away_team_id","away_team_name","match_date","deadline_bet","event_id","match_status","issue","homeTeamLogUrl","awayTeamLogUrl"}; // 基础赛事信息配置列
			String[] play_column_array_football = {"home_win", "home_draw", "home_lose", "is_win_draw_lose", "is_hot","banner_number","one_type_id"};			
			String[] play_column_array_baskball = { "home_win", "home_lose", "is_winlose", "is_hot","one_type_id"};
			//组装字段
			String[] all_column_array_football = ArrayUtils.addAll(base_column_array, play_column_array_football);
			String[] all_column_array_baskball = ArrayUtils.addAll(base_column_array, play_column_array_baskball);
			JSONObject dealJSON = new JSONObject(); 
			for (String itemKey : (id1 == 1 || id1 == 3) ? all_column_array_football : all_column_array_baskball) {				
				dealJSON.put(itemKey, matchJSON.get(itemKey));
			}
			//执行先入先出
			linkList = QueueUtils.queue(linkList, dealJSON, 1, 6);//请注意参数值含义 Ctrl+click this Func
			jedis.set(key, JSONArray.fromObject(linkList).toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
 	
 	/**
 	 * 生成代理层级编号
 	 * @return
 	 */
 	public static String autoProxyHierarchyUid(String curNum) {
 		char[] maxNumChar = curNum.substring(curNum.length()-3, curNum.length()).toCharArray();
 		char[] nextNumChar = maxNumChar.clone();
 		for(int i=2;i>=0;i--) {
 			if(getNextAscii((int)maxNumChar[i])==48) {
 	    		nextNumChar[i]='0';
 	    		if(getNextAscii((int)maxNumChar[i-1])==48) {
 	    			nextNumChar[i-1]='0';
 	    			nextNumChar[i-2]=(char)(getNextAscii((int)maxNumChar[i-2]));
 	        		break;
 	    		}
 	    	}else {
 	    		nextNumChar[i]=(char)getNextAscii((int)maxNumChar[i]);
 	    		break;
 	    	}
 		}
 		return curNum.substring(0,curNum.length()-3)+new String(nextNumChar);
 	}
 	
 	private static int getNextAscii(int curAscii) {
 		if(curAscii==57) {
 			return 97;
 		}else if(curAscii==122) {
 			return 48;
 		}else{
 		    return curAscii+1;
 		}
 	}
}
