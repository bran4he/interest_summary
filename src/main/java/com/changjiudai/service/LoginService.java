package com.changjiudai.service;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.changjiudai.bean.Cagent;
import com.changjiudai.bean.ReportData;
import com.changjiudai.util.Cconstant;
import com.changjiudai.util.CommonUtil;

@Service
public class LoginService {

	private static final Logger logger = Logger.getLogger(LoginService.class);
	
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
				
				logger.info("get img code status: " + response.getStatusLine());
				EntityUtils.consume(entity);

				logger.info("Initial set of cookies:");
				List<Cookie> cookies = cagent.getCookieStore().getCookies();
				if (cookies.isEmpty()) {
					logger.info("None");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						logger.info("- " + cookies.get(i).getName() + "\t" + cookies.get(i).getValue());
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
			
			logger.info("login get status: " + response.getStatusLine());
			EntityUtils.consume(entity);
			
			logger.info("Post login cookies:");
			List<Cookie> cookies = cagent.getCookieStore().getCookies();
			
			if (cookies.isEmpty()) {
				logger.info("None");
			} else {
				for (int i = 0; i < cookies.size(); i++) {
					logger.info("- " + cookies.get(i).getName() + "\t" + cookies.get(i).getValue());	//cookies.get(i).toString()
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
	
	public void sign(Cagent cagent){
		
		if(cagent.isLogined()){
			logger.info("==============sign daily");
			
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cagent.getCookieStore()).build();
			
			HttpUriRequest sign;
			CloseableHttpResponse response = null;
			try {
				sign = RequestBuilder.post()
						.setUri(new URI("http://www.changjiudai.com/index.php?user&q=code/credit/registertime"))
						.build();
				response = httpclient.execute(sign);
				logger.info("sign get status: " + response.getStatusLine());
				HttpEntity entity1 = response.getEntity();
				
//				CommonUtil.logHeaders(response1.getAllHeaders());
				
				BufferedInputStream bisSign = new BufferedInputStream(entity1.getContent());
				StringBuffer sbSign = new StringBuffer();
				byte[] buffSign = new byte[1024];
				int lenSign = -1;
				while ((lenSign = bisSign.read(buffSign)) != -1) {
					sbSign.append(new String(buffSign, 0, lenSign));
				}
				
				logger.info("sign response: \t" + sbSign.toString());//[5,6,7,8,9,11,16,25,26,27,28,29,30,]
				
				cagent.setSignedList(separateSignedDays(sbSign.toString()));
				
				EntityUtils.consume(entity1);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					response.close();
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	//2017-04-26	￥1233.33	￥0.00	￥1233.33
	public ReportData prepareReport(Cagent cagent) throws IOException{
		
		List<String> datelst = new ArrayList<String>();
		List<Long> totallst = new ArrayList<Long>();
		List<Long> capitallst = new ArrayList<Long>();
		List<Long> interestlst = new ArrayList<Long>();
		Map<String, String> cookies = new HashMap<String, String>();
		
		List<Cookie> cookielst = cagent.getCookieStore().getCookies();
		
		for (int i = 0; i < cookielst.size(); i++) {
			logger.info("- " + cookielst.get(i).getName() + "\t" + cookielst.get(i).getValue());	//cookies.get(i).toString()
			cookies.put(cookielst.get(i).getName(), cookielst.get(i).getValue());
		}
		
		Document doc = null;
		
		if(cagent.getTotalPages() == 0){
			doc = Jsoup.connect(Cconstant.PAGE_NUM_URL).cookies(cookies).timeout(30000).get();
			Elements pages = doc.select("div .userPage");
			String pageStr = pages.get(0).text();	//共12页/当前为第1页 首页 上一页 下一页 尾页
			int totalPages = Integer.parseInt(getPageNum(pageStr));
			logger.info("==========get total pages :" + totalPages);
			cagent.setTotalPages(totalPages);
		}
		
		for(int i=1; i<=cagent.getTotalPages(); i++){
			String url = Cconstant.PAGE_URL + i;
			
			doc = Jsoup.connect(url).cookies(cookies).timeout(30000).get();
			
			Elements tables = doc.select(".tableInfo");
			
			//2017-04-26	￥1233.33	￥0.00	￥1233.33
			for(Element table : tables){
				String date = table.select("td").get(1).attr("title"); //date 日期
				String total = table.select("td").get(4).text();	//total 共收入
				String capital = table.select("td").get(5).text();	//capital 本金
				String interest = table.select("td").get(6).text();	//interest 利息
				logger.info(date +"\t"+ total +"\t"+ capital + "\t" +interest);
				datelst.add(date);
				totallst.add(parseStringToDouble(total));
				capitallst.add(parseStringToDouble(capital));
				interestlst.add(parseStringToDouble(interest));
			}
		}
	
		ReportData data = new ReportData(datelst,
										totallst, 
										capitallst, 
										interestlst);
		return data;
	}
	
	public void exportReport(Cagent cagent, ReportData data){
		cagent.setReportPath(generateExcelReport(data));
	}
	
	private String generateExcelReport(ReportData data){
		
		List<String> datelst = data.getDatelst();
		List<Long> totallst = data.getTotallst();
		List<Long> capitallst = data.getCapitallst();
		List<Long> interestlst = data.getInterestlst();
		
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		
		Row row = sheet.createRow((short)0);
		Cell cell1 = row.createCell(0);
		cell1.setCellValue("日期");
		Cell cell2 = row.createCell(1);
		cell2.setCellValue("总收入");
		Cell cell3 = row.createCell(2);
		cell3.setCellValue("本金");
		Cell cell4 = row.createCell(3);
		cell4.setCellValue("利息");
		
		for(int i=0; i< datelst.size(); i++){
			row = sheet.createRow((short)(i+1));
			cell1 = row.createCell(0);
			cell1.setCellValue(datelst.get(i));
			cell2 = row.createCell(1);
			cell2.setCellValue(totallst.get(i));
			cell3 = row.createCell(2);
			cell3.setCellValue(capitallst.get(i));
			cell4 = row.createCell(3);
			cell4.setCellValue(interestlst.get(i));
		}
		
		String path = CommonUtil.getProjectPath();
		String fileName = "changjiudai_" + new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date()) + ".xlsx";
	    FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(path + fileName);
			wb.write(fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    
	    return fileName;
	}
	
	//[5,6,7,8,9,11,16,25,26,27,28,29,30,]
	private List<String> separateSignedDays(String data){
		List<String> lst = new ArrayList<String>();
		String regex = "\\d{1,2}";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		while(m.find()){
			lst.add(m.group());
		}
		return lst;
	}
	
	//￥1233.33 -> 1233.33(double)
	private static Long parseStringToDouble(String str){
		Format f=new DecimalFormat("￥#.##");
		Long result =0L;
		try {
			Object d = f.parseObject(str);
			if(d instanceof Long){
				result = (Long) d;
			}else if(d instanceof Double){
				Double tmp = (Double) d;
				result = tmp.longValue();
			}else{
				logger.info(str +"cannot be cast to Long or Double!!");
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.debug("==========the parsed data is :" + str);
			e.printStackTrace();
		}
		return result;
	}
	
	private String getPageNum(String str){
		String regex = "\\d{1,2}";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		
		if(m.find()){
			return m.group(0);
		}else{
			logger.info("cannot find page num, please check!!");
			return "0";
		}
	}
}
