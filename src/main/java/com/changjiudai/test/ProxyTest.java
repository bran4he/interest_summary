package com.changjiudai.test;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.changjiudai.util.ConfigUtil;

public class ProxyTest {

	public static void main(String[] args) {
		
		ConfigUtil config = 
				new ConfigUtil(true, "shtmg.ebaotech.com", 8080, true, "", "");
		
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(config.getProxyHost(), config.getProxyPort()),
				new UsernamePasswordCredentials(config.getProxyUsername(), config.getProxyPassword()));
		
		BasicCookieStore cookieStore = new BasicCookieStore();
		
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).setDefaultCredentialsProvider(config.getCredentialsProvider()).build();
		
		HttpGet httpget = new HttpGet("http://www.changjiudai.com//plugins/index.php?q=imgcode&height=25");
		try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			
			HttpEntity entity = response.getEntity();
			
			System.out.println(entity.getContentLength());
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
