package com.dragon.common.util.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 动态管理 properties 文件
 * @author Administrator
 *
 */
public class PropertiesAutoUpdate {
	private  static String configFilePath="config";
    private static Map<String,Properties> propertisMap=new HashMap<String, Properties>();
    private static Map<String,Date> propertisTimeMap=new HashMap<String, Date>();
    //多少秒 更新一次
    private static int updateTime=60*10;
	private static Timer timer ;
	
	static{
			loadFile();
			setTimer();
	}
	private static synchronized void setTimer() {
		String updateType=getValue("base","updateType");
		if(updateType==null||"auto".equals(updateType)){
			Integer configUpdateTime=getIntegerValue("config","updateTime");
			updateTime=configUpdateTime!=null?configUpdateTime:updateTime;
			if(timer==null){
				timer = new Timer();
			}
			 
			 timer.schedule(new TimerTask() {  
		            public void run() {  
		                try {
							loadFile();
							setTimer();
						} catch (Exception e) {
							e.printStackTrace();
						}
		            }  
		        },  1000*updateTime);// 设定指定的时间time,此处为2000毫秒 
		}else{
			if(timer!=null){
				timer.cancel();
			}
		}
		
	}
	
	
	
	private static synchronized void loadFile() {
		Date now =new Date(System.currentTimeMillis());
		
		File folder=new File(PropertiesAutoUpdate.class.getResource("/").getPath()+configFilePath);
		if(folder.exists()){
			File[] files=folder.listFiles();
				for (File file : files) {
					String fileName=file.getName().substring(0,file.getName().lastIndexOf("."));
					if(file.getName().lastIndexOf(".properties")<0){
						continue;
					}
					Date filetime = new Date(file.lastModified());
					Date oldFileTime=propertisTimeMap.get(fileName);
					propertisTimeMap.put(fileName, filetime);
					if(oldFileTime==null ||filetime.compareTo(oldFileTime)!=0){
						loadProperties(fileName, file);
					}
				}
		}
	}
	
	
	public static void  loadProperties(String fileName,File file) {
		Properties properties=propertisMap.get(fileName);
		if(null==properties){
			properties=new Properties();
		}
		try {
			properties.load(new FileInputStream(file));
			propertisMap.put(fileName, properties);
		}  catch (Exception e) {
		}
	}
	
	/**
	 * 获取Properties对象
	 */
	public static Properties getProperties(String fileName){
		return propertisMap.get(fileName);
	}
	
	/**
	 * 获取配置文件
	 */
	public static String getValue(String fileName ,String key){
		
		Properties prop=propertisMap.get(fileName);
		if(prop==null){
			return null;
		}
		return prop.getProperty(key);
	}
	
	/**
	 * 获取配置文件
	 */
	public static Integer getIntegerValue(String fileName ,String key){
		String value=getValue( fileName , key);
		if(null==value){
			return null;
		}
		return  Integer.valueOf(value);
	}
	/**
	 * 获取配置文件
	 */
	public static Double getInDoubleValue(String fileName ,String key){
		String value=getValue( fileName , key);
		if(null==value){
			return null;
		}
		return  Double.valueOf(value);
	}
	
	
	/**
	 * 获取配置文件
	 */
	public static Boolean getInBooleanValue(String fileName ,String key){
		String value=getValue( fileName , key);
		if(null==value){
			return null;
		}
		Boolean result=  Boolean.valueOf(value);
		return  Boolean.valueOf(value);
	}
	public static void main(String[] args) throws Exception {
		
		System.out.println(PropertiesAutoUpdate.getValue("shop","app_id"));
		
		System.out.println(PropertiesAutoUpdate.getValue("base","updateType"));
		
		Properties properties =PropertiesAutoUpdate.getProperties("base");
		System.out.println(properties.get("updateType"));
	}

}
