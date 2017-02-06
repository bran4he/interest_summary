package com.changjiudai.util;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	public static void main(String[] args) {
		CommonUtil.getProjectPath();
	}
	
	public static void logHeaders(Header[] headers) {
		logger.info("----response headers----");
		for (Header header : headers) {
			logger.info("{}\t{}",header.getName(), header.getValue());
		}
		logger.info("----response headers----");
	}
	
	public static boolean checkLogin(String responseStr) {
		logger.debug("login response: \n {}", responseStr);
		
		//验证码错误
		//用户名密码错误
		if(responseStr.indexOf("icon_ture") != -1){
			logger.info("login successfully!");
			return true;
		}else if(responseStr.indexOf("icon_worrg") != -1){
			logger.info("fail login with wrong img code!");
			return false;
		}else{
			logger.info("fail login with wrong username or password!");
			return false;
		}
		
	}
	
	/**
	 * D:/dev/apache-tomcat-7.0.70/wtpwebapps/web-root/
	 * @return
	 */
	public static String getProjectPath() {
		String temp = CommonUtil.class.getResource("/").getPath();
		logger.info("getResources path:{}", temp);
		String path = temp.substring(0, temp.indexOf("WEB-INF"));
		logger.info("project path:{}", path);
		return path;
	}
	
	/**
	 * D:/dev/apache-tomcat-7.0.70/wtpwebapps/web-root/WEB-INF/
	 * @return
	 */
	public static String getWEBINFPath(){
		///D:/dev/apache-tomcat-7.0.70/wtpwebapps/interest-summary/WEB-INF/classes/
		String temp = CommonUtil.class.getResource("/").getPath();
		String path = temp.substring(0, temp.indexOf("classes"));
		logger.info("WEB-INF path:{}", path);
		return path;
	}
	
}
