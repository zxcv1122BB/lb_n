package com.lb.weblListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.lb.sys.dao.PropertiesMapper;

@WebListener
public class MyServletContextListener implements ServletContextListener {
	
	
	public static final Map<String,Map<String,String>> propertiesMap = new HashMap<>();

    @Autowired private PropertiesMapper propertiesMapper;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<Map<String, Object>> properties = propertiesMapper.selectAll();
        if(properties!=null && properties.size()>0) {
        	Map<String,String> childMap = null;
        	for(int i = 0;i<properties.size();i++) {
        		Map<String, Object> propertie = properties.get(i);
        		String keys = propertie.get("parent_key").toString();
        		if(!propertiesMap.containsKey(keys)) {
        			childMap =  new HashMap<>();
        			propertiesMap.put(keys, childMap);
        		}
				String child_key = propertie.get("child_key").toString();
    			String value = propertie.get("child_value").toString();
    			childMap.put(child_key, value);
    			propertiesMap.put(keys, childMap);
        	}
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
