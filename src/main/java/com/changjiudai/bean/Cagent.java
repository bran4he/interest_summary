package com.changjiudai.bean;

import java.util.List;

import org.apache.http.client.CookieStore;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
//@Scope("session")
public class Cagent {

	private static Logger logger = Logger.getLogger(Cagent.class);
	
	
	private String userName;
	private String passWord;
	private String code;
	
	@JsonIgnore
	private CookieStore cookieStore;
	
	private boolean logined;
	private List<String> signedList;
	
	private String imgCodePath;
	private String reportPath;
	private int totalPages = 0;
	
	
	public CookieStore getCookieStore() {
		return cookieStore;
	}
	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImgCodePath() {
		return imgCodePath;
	}
	public void setImgCodePath(String imgCodePath) {
		this.imgCodePath = imgCodePath;
	}
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public boolean isLogined() {
		return logined;
	}
	public void setLogined(boolean logined) {
		this.logined = logined;
	}
	public List<String> getSignedList() {
		return signedList;
	}
	public void setSignedList(List<String> signedList) {
		this.signedList = signedList;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
