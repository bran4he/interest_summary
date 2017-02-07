package com.changjiudai.util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("config")
public class ConfigUtil {

	private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
	
	@Value("${proxy.enable}")
	private boolean enableProxy;
	
	@Value("${proxy.host}")
	private String proxyHost;
	
	@Value("${proxy.port}")
	private int proxyPort;

	@Value("${proxy.auth}")
	private boolean proxyAuth;
	
	@Value("${proxy.username}")
	private String proxyUsername;
	
	@Value("${proxy.password}")
	private String proxyPassword;
	
	/**
	 * proxy: org.apache.http.HttpHost
	 * @return
	 */
	public HttpHost getHttpHost(){
		if(!this.proxyAuth){
			return new HttpHost(this.proxyHost, this.proxyPort);
		}else{
			logger.info("auth param in config is {}, please check!", this.proxyAuth);
			return null;
		}
	}
	
	/**
	 * httpclient 4.x auth
	 * @return
	 */
	public CredentialsProvider getCredentialsProvider(){
		if(this.proxyAuth){
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(
					new AuthScope(this.proxyHost, this.proxyPort),
					new UsernamePasswordCredentials(this.proxyUsername, this.proxyPassword));
			return credsProvider;
		}else{
			logger.info("auth param in config is {}, please check!", this.proxyAuth);
			return null;
		}
	}
	
	
	/**
	 * proxy: java.net.Proxy
	 * @return
	 */
	public Proxy getProxy(){
		return new Proxy(Type.HTTP, new InetSocketAddress(this.proxyHost, this.proxyPort));
	}
	
	public boolean isEnableProxy() {
		return enableProxy;
	}

	public void setEnableProxy(boolean enableProxy) {
		logger.info("enable proxy param in config is {}", enableProxy);
		this.enableProxy = enableProxy;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isProxyAuth() {
		return proxyAuth;
	}

	public void setProxyAuth(boolean proxyAuth) {
		logger.info("auth param in config is {}", proxyAuth);
		this.proxyAuth = proxyAuth;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	public ConfigUtil(boolean enableProxy, String proxyHost, int proxyPort,
			boolean proxyAuth, String proxyUsername, String proxyPassword) {
		super();
		this.enableProxy = enableProxy;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyAuth = proxyAuth;
		this.proxyUsername = proxyUsername;
		this.proxyPassword = proxyPassword;
	}

	public ConfigUtil(boolean enableProxy, String proxyHost, int proxyPort) {
		super();
		this.enableProxy = enableProxy;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
	}

	public ConfigUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
