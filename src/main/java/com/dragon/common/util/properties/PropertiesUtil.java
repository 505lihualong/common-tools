/**
 * <p>Title:AgentConfig.java </p>
 * <p>Description:[功能描叙] </p>
 * <p>Copyright:  Copyright (C) 2002 - 2008 GuangDong EShore Techonlogy Co. Ltd</p>
 * <p>Company: 中数通</p>
 * @author Dragon
 * @version 1.0
 * @time: 2010-1-28
 */

package com.dragon.common.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author wang
 */
public class PropertiesUtil {

	private Properties prop;
	private String filePath;
	
	public PropertiesUtil() {
		
	}
	public PropertiesUtil(String filePath) {
		super();
		this.filePath = filePath;
	}


	/**
	 * 获取配置文件值
	 */
	public String getValue(String key) {
		if (prop == null) {
			// TODO
			prop=this.loadConfigFile(filePath);
		}
		return prop.getProperty(key);
	}

	
	/**
	 * 加载配置文件
	 */
	public static Properties loadConfigFile(String fileName) {
		Properties propertie=null;
		InputStream inputStream = PropertiesUtil.class.getResourceAsStream(fileName);
		try {
			propertie = new Properties();
			propertie.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propertie;
	}
	

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
