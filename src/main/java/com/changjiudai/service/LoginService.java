package com.changjiudai.service;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.changjiudai.bean.Cagent;
import com.changjiudai.util.CommonUtil;

@Service
public class LoginService {

	private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
	
	public void setImagePath(Cagent cagent){
		BasicCookieStore cookieStore = new BasicCookieStore();
		cagent.setCookieStore(cookieStore);
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		try {
			logger.info("==============get img code start");
			HttpGet httpget = new HttpGet("http://www.changjiudai.com//plugins/index.php?q=imgcode&height=25");
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				HttpEntity entity = response.getEntity();

				Header[] headers = response.getAllHeaders();
				
				CommonUtil.logHeaders(headers);
				
				InputStream ins = entity.getContent();
				
				String path = CommonUtil.getProjectPath();
				
				BufferedInputStream bis = new BufferedInputStream(ins);
				cagent.setImgCodePath("changjiudai_" + System.currentTimeMillis() + ".gif");
				FileOutputStream fileOut = new FileOutputStream(path + cagent.getImgCodePath());
				byte[] buff = new byte[1024];
				int len = -1;
				while ((len = bis.read(buff)) != -1) {
					fileOut.write(buff, 0, len);
				}
				fileOut.close();
				bis.close();
				
				logger.info("get img code status: {}", response.getStatusLine());
				EntityUtils.consume(entity);

				logger.info("initial set of cookies:");
				List<Cookie> cookies = cagent.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					logger.info("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						logger.info("-{}\t{}", cookies.get(i).getName(), cookies.get(i).getValue());
					}
				}
			} finally {
				response.close();
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	public void login(Cagent cagent){

		logger.info("==============login start");
		
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cagent.getCookieStore()).build();
		CloseableHttpResponse response = null;
		try {
			HttpUriRequest login = RequestBuilder.post()
					.setUri(new URI("http://www.changjiudai.com/index.php?user&q=action/login"))
					.addParameter("keywords", cagent.getUserName())
					.addParameter("password", cagent.getPassWord())
					.addParameter("valicode", cagent.getCode())
					.build();
			response = httpclient.execute(login);
			HttpEntity entity = response.getEntity();
			
			InputStream ins = entity.getContent();
			BufferedInputStream bis = new BufferedInputStream(ins);
			StringBuffer sb = new StringBuffer();
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = bis.read(buff)) != -1) {
				sb.append(new String(buff, 0, len));
			}
			
			CommonUtil.logHeaders(response.getAllHeaders());
			
			logger.info("login get status: {}", response.getStatusLine());
			EntityUtils.consume(entity);
			
			logger.info("Post login cookies:");
			List<Cookie> cookies = cagent.getCookieStore().getCookies();
			
			if (cookies.isEmpty()) {
				logger.info("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					logger.info("-{}\t{}", cookies.get(i).getName(), cookies.get(i).getValue());	//cookies.get(i).toString()
				}
			}
			
			boolean result = CommonUtil.checkLogin(sb.toString());
			cagent.setLogined(result);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
