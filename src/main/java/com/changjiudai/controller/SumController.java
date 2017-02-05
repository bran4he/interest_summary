package com.changjiudai.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.changjiudai.bean.Cagent;
import com.changjiudai.bean.ReportData;
import com.changjiudai.service.SumService;

@Controller
@RequestMapping("/sum")
public class SumController {
	
	private static final Logger logger = LoggerFactory.getLogger(SumController.class);
	
	@Autowired
	private SumService sumService;

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
	
	@RequestMapping(value="export", method=RequestMethod.GET)
	public @ResponseBody Cagent export(HttpSession session) throws IOException{
		logger.info("request export");
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		sumService.exportReport(cagent, sumService.prepareReport(cagent));
		return cagent;
	}
	
	/*
		List<String> datelst = new ArrayList<String>();
		List<Long> totallst = new ArrayList<Long>();
		List<Long> capitallst = new ArrayList<Long>();
		List<Long> interestlst = new ArrayList<Long>();
	*/
	
	@RequestMapping(value="chart", method=RequestMethod.GET)
	public @ResponseBody ReportData exportChartData(HttpSession session) throws IOException{
		logger.info("request login");
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		
		ReportData reportData = sumService.prepareReport(cagent);
		
		List<String> datelst = reportData.getDatelst();
		List<Long> totallst = reportData.getTotallst();
		List<Long> capitallst = reportData.getCapitallst();
		List<Long> interestlst = reportData.getInterestlst();
		
		Map<String, Long> totalMap = new HashMap<String, Long>();
		Map<String, Long> capotalMap = new HashMap<String, Long>();
		Map<String, Long> interestMap = new HashMap<String, Long>();
		
		for(int i=0; i<datelst.size(); i++){
			String date = datelst.get(i);
			if(!totalMap.containsKey(date)){
				totalMap.put(date, totallst.get(i));
				capotalMap.put(date, capitallst.get(i));
				interestMap.put(date, interestlst.get(i));
			}else{
				totalMap.put(date, totalMap.get(date) + totallst.get(i));
				capotalMap.put(date, capotalMap.get(date) + capitallst.get(i));
				interestMap.put(date, interestMap.get(date) + interestlst.get(i));
			}
		}
		Set<String> set = totalMap.keySet();
		List<String> dateList = new ArrayList<String>();
		dateList.addAll(set);
		
		Collections.sort(dateList);
		
		List<String> dates = new ArrayList<String>();
		List<Long> totals = new ArrayList<Long>();
		List<Long> capitals = new ArrayList<Long>();
		List<Long> interests = new ArrayList<Long>();
		for(String date: dateList){
			dates.add(date);
			totals.add(totalMap.get(date));
			capitals.add(capotalMap.get(date));
			interests.add(interestMap.get(date));
		}
		
		ReportData chartData = new ReportData(dates, totals, capitals, interests);
		
		return chartData;
	}
	
	
	
	
	
	public SumService getSumService() {
		return sumService;
	}

	public void setSumService(SumService sumService) {
		this.sumService = sumService;
	}
	
}
