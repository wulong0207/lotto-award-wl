package com.hhly.award.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;


/**
 * 
 * @desc 配置文件读取
 * @author jiangwei
 * @date 2017年5月9日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class PropertyUtil {
	public static final Logger LOGGER = Logger.getLogger(PropertyUtil.class);
	/**
	 * 读取配置文件值
	 * @param fileName 文件名
	 * @param key
	 * @return 
	 */
	public static String getPropertyValue(String fileName,String key){
		try {
			String value = PropertiesLoaderUtils.loadAllProperties(fileName).getProperty(key);
			if("".equals(value) || value == null){
				return "";
			}
			return new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (IOException e) {
			LOGGER.error("读取配置文件错误", e);
			return null;
		}
	}
}
