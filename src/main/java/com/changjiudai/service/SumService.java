package com.changjiudai.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpInetSocketAddress;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.changjiudai.bean.Cagent;
import com.changjiudai.bean.ReportData;
import com.changjiudai.model.ReportModel;
import com.changjiudai.util.Cconstant;
import com.changjiudai.util.CommonUtil;

@Service
public class SumService {

	private static final Logger logger = LoggerFactory.getLogger(SumService.class);
	
	@Autowired
	private ReportService reportService;
	
	
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
				logger.info("sign get status: {}", response.getStatusLine());
				HttpEntity entity1 = response.getEntity();
				
//				CommonUtil.logHeaders(response1.getAllHeaders());
				
				BufferedInputStream bisSign = new BufferedInputStream(entity1.getContent());
				StringBuffer sbSign = new StringBuffer();
				byte[] buffSign = new byte[1024];
				int lenSign = -1;
				while ((lenSign = bisSign.read(buffSign)) != -1) {
					sbSign.append(new String(buffSign, 0, lenSign));
				}
				
				logger.info("sign response: {}", sbSign.toString());//[5,6,7,8,9,11,16,25,26,27,28,29,30,]
				
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
	
	/*
	 redis store design
	 
	 "2017-01-12" :{
	 	"date":2017-01-12,
	 	"total":1233.33,
	 	"capital":0.00,
	 	"interest":1233.33
	 }
	
	*/
	
	
	/*
	 * get data from page(online)
	 */
	public ReportData parsePage(Cagent cagent) throws IOException{
		
		List<String> datelst = new ArrayList<String>();
		List<Long> totallst = new ArrayList<Long>();
		List<Long> capitallst = new ArrayList<Long>();
		List<Long> interestlst = new ArrayList<Long>();
		
		Map<String, String> cookies = new HashMap<String, String>();
		
		List<Cookie> cookielst = cagent.getCookieStore().getCookies();
		
		for (int i = 0; i < cookielst.size(); i++) {
			logger.info("-{}\t{}", cookielst.get(i).getName(), cookielst.get(i).getValue());	//cookies.get(i).toString()
			cookies.put(cookielst.get(i).getName(), cookielst.get(i).getValue());
		}
		
		Document doc = null;
		
		if(cagent.getTotalPages() == 0){
			
			doc = Jsoup.connect(Cconstant.PAGE_NUM_URL).cookies(cookies).timeout(30000).get();
			
			Elements pages = doc.select("div .userPage");
			String pageStr = pages.get(0).text();	//共12页/当前为第1页 首页 上一页 下一页 尾页
			int totalPages = Integer.parseInt(getPageNum(pageStr));
			logger.info("==========get total pages :{}", totalPages);
			cagent.setTotalPages(totalPages);
		}
		
		for(int i=1; i<=cagent.getTotalPages(); i++){
			String url = Cconstant.PAGE_URL + i;
			
			doc = Jsoup.connect(url).cookies(cookies).timeout(30000).get();
			
			Elements tables = doc.select(".tableInfo");
			
			logger.info("{}\t\t\t\t{}\t{}\t{}", "date", "total", "capital", "interest");
			//2017-04-26	￥1233.33	￥0.00	￥1233.33
			for(Element table : tables){
				String date = table.select("td").get(1).attr("title").trim(); //date 日期
				Long total = parseStringToLong(table.select("td").get(4).text().trim());	//total 共收入
				Long capital = parseStringToLong(table.select("td").get(5).text().trim());	//capital 本金
				Long interest = parseStringToLong(table.select("td").get(6).text().trim());	//interest 利息
				logger.info("{}\t\t\t\t{}\t{}\t{}", date, total, capital, interest);
				
				datelst.add(date);
				totallst.add(total);
				capitallst.add(capital);
				interestlst.add(interest);
			}
		}
		
		ReportData data = new ReportData(datelst,
				totallst, 
				capitallst, 
				interestlst);
		
		return data;
	
	}
	
	
	
	//2017-04-26	￥1233.33	￥0.00	￥1233.33
	public ReportData prepareReport(Cagent cagent) throws IOException{
		
		ReportData data = new ReportData();
		
		String currentDate = reportService.getCurrentDate();
		logger.info("get currentDate from redis : {}", currentDate);
		
		String currentDateSys = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		logger.info("get currentDate from system : {}", currentDateSys);
		
		//currentDate is null, or is not equals date of today, update all data from page
		if(null == currentDate || 
				!currentDate.equals(currentDateSys)){
			logger.info("go to get data from page and sync redis process");
			data = parsePage(cagent);
			syncCurrentDate(data);
			syncReportModel(data);
		}
		logger.info("go to get data from redis process");
		//get data from redis directly
		data = getDataFromRedis();
		
		return data;
	}
	/*
	 * get data from redis directly
	 */
	private ReportData getDataFromRedis(){
		
		List<String> dates = reportService.getAllDates();
		logger.info("date list before sort: {}", dates);
		Collections.sort(dates);
		logger.info("date list after sort: {}", dates);
		
		
		List<String> datelst = new ArrayList<String>();
		List<Long> totallst = new ArrayList<Long>();
		List<Long> capitallst = new ArrayList<Long>();
		List<Long> interestlst = new ArrayList<Long>();
		
		for(String key : dates){
			ReportModel m = reportService.getReportModel(key);
			datelst.add(m.getDate());
			totallst.add(m.getTotal());
			capitallst.add(m.getCapital());
			interestlst.add(m.getInterest());
		}
		
		ReportData data = new ReportData(datelst,
				totallst, 
				capitallst, 
				interestlst);
		
		return data;
	}
	
	
	private void syncCurrentDate(ReportData data){
		logger.info("syncCurrentDate :{}", data.getDatelst().get(0));
		reportService.updateCurrentDate(data.getDatelst().get(0));
	}
	
	/*
	 * sync data with redis server
	 */
	private void syncReportModel(ReportData data){
		//clear all data bound by session user
		reportService.clean();
		
		List<String> datelst = data.getDatelst();
		List<Long> totallst = data.getTotallst();
		List<Long> capitallst = data.getCapitallst();
		List<Long> interestlst = data.getInterestlst();
		
		for(int i = 0; i<datelst.size(); i++){
			ReportModel model = new ReportModel(
					datelst.get(i),
					totallst.get(i), 
					capitallst.get(i), 
					interestlst.get(i));
			//*1. not in redis need add
			//*2. in redis but need update
			reportService.addOrUpdate(model);
		}
	}
	
	
	public void exportReport(Cagent cagent, ReportData data){
		cagent.setReportName(generateExcelReport(data, cagent.getUserName()));
	}
	
	private String generateExcelReport(ReportData data, String username){
		
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
		
		String path = CommonUtil.getWEBINFPath();
		//20170206_Changjiudai_username.xlsx
		String fileName = new SimpleDateFormat(Cconstant.EXORT_NAME_TIMESTAMP).format(new Date()) + Cconstant.EXPORT_NAME_PREFIX + username  + ".xlsx";
	    
		String folder = path + "download/" + username;
		if(!new File(folder).exists()){
			new File(folder).mkdir();
		}
		
		//path/download/username/file
		File reportFile = new File(folder + File.separator +fileName);
		if(reportFile.exists() && reportFile.length() != 0L){
	    	logger.info("{} already exported, return directly!", fileName);
	    }else{
	    	FileOutputStream fileOut = null;
	    	try {
	    		fileOut = new FileOutputStream(reportFile);
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
	private static Long parseStringToLong(String strWithFlag){
		
		String str = strWithFlag.substring(1);
		
		Format f=new DecimalFormat("#.##");
		Long result =0L;
		try {
			Object d = f.parseObject(str);
			if(d instanceof Long){
				result = (Long) d;
			}else if(d instanceof Double){
				Double tmp = (Double) d;
				result = tmp.longValue();
			}else{
				logger.info("{} cannot be cast to Long or Double!!", str);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.debug("==========the parsed data is :{}", str);
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

	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}
	
	
}
