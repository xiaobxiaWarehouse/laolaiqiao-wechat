/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.codi.laolaiqiao.utils;

import java.util.Map;

import org.springframework.util.Assert;

import com.google.common.collect.Maps;

/**
 * @author ThinkGem
 * @version 2013-03-23
 */
public class Global {
	
	private static Map<String, String> map = Maps.newHashMap();
	
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("config.properties");
	
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = propertiesLoader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}

	/////////////////////////////////////////////////////////
	
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	
	public static String getWechatPath(){
		return getConfig("wechatPath");
	}
	
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	public static String getCkBaseDir() {
		String dir = getConfig("userfiles.basedir");
		Assert.hasText(dir, "配置文件里没有配置userfiles.basedir属性");
		if(!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
	
	public static String VIEW_PROP_TITLE = "title";
}
