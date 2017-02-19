package com.changjiudai.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.changjiudai.bean.Cagent;
import com.changjiudai.bean.ReportData;
import com.changjiudai.model.ReportModel;
import com.changjiudai.service.ReportService;
import com.changjiudai.service.SumService;

@Controller
@RequestMapping("/sum")
public class SumController {
	
	private static final Logger logger = LoggerFactory.getLogger(SumController.class);
	
	@Autowired
	private SumService sumService;

	@Autowired
	private ReportService reportService;
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String chartsPage(HttpSession session){
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		logger.debug("sum index page, cagent:/n", cagent);
		if(null == cagent || cagent.isLogined() != true){
			//not login page
			return "notLogin";
		}else{
			return "sum";
		}
		
	}
	
	@RequestMapping(value="sign", method=RequestMethod.GET)
	public @ResponseBody Cagent sign(HttpSession session){
		logger.info("request sign");
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		sumService.sign(cagent);
		return cagent;
	}
	
	@RequestMapping(value="download", method=RequestMethod.GET)
	public ResponseEntity<byte[]> export(HttpSession session) throws IOException {
		logger.info("download excel report");
		
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		sumService.exportReport(cagent, sumService.prepareReport(cagent));
		
		byte[] body=null;
		ServletContext servletContext=session.getServletContext();
        InputStream ins = servletContext.getResourceAsStream("../WEB-INF/download/" 
			+ cagent.getUserName() + File.separator 
			+ cagent.getReportName());
        
        
        body=new byte[ins.available()];
        ins.read(body);
        
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        headers.add("Content-Disposition", "attachment;filename=" + cagent.getReportName() + ".xlsx");
        HttpStatus statusCode=HttpStatus.OK;
        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
		
	}
	/*
	@RequestMapping(value="export", method=RequestMethod.GET)
	public @ResponseBody Cagent export(HttpSession session) throws IOException{
		logger.info("request export");
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		sumService.exportReport(cagent, sumService.prepareReport(cagent));
		return cagent;
	}
	*/
	/*
		List<String> datelst = new ArrayList<String>();
		List<Long> totallst = new ArrayList<Long>();
		List<Long> capitallst = new ArrayList<Long>();
		List<Long> interestlst = new ArrayList<Long>();
	*/
	
	@RequestMapping(value="chart", method=RequestMethod.GET)
	public @ResponseBody ReportData exportChartData(HttpSession session) throws IOException{
		logger.info("request show chart");
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		
		ReportData reportData = sumService.prepareReport(cagent);

		//test
		logger.info("test with total list : ======================");
		List<ReportModel> datalst = reportService.getAllModels();
		for(ReportModel data : datalst){
			logger.info(data.getDate() + "\t" + data.getTotal());
		}
		logger.info("====================== end");
		return reportData;
	}
	
	
	
	
	
	public ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		this.reportService = reportService;
	}

	public SumService getSumService() {
		return sumService;
	}

	public void setSumService(SumService sumService) {
		this.sumService = sumService;
	}
	
}
