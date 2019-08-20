package com.lb.sys.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

import com.lb.sys.tools.ParseProFileUtil;

@Component
public class CustomizationBean implements EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		Map<String, String> map = new HashMap<>();
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("windows")) {
			map = ParseProFileUtil.parseProFile("dbData.properties",true);
		} else if (os.contains("linux")) {
			map = ParseProFileUtil.getProperties("/ls/application/conf/dbData.properties",true);
		}
		container.setPort(Integer.parseInt(map.get("lbPort")));
	}
}
