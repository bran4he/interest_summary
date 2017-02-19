package com.changjiudai.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.changjiudai.dao.ReportDao;
import com.changjiudai.model.ReportModel;


@Service
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
	
	@Autowired
	private ReportDao reportDao;

	public void clean(){
		reportDao.deleteCurrentDate();
		reportDao.deleteReportData();
	}
	
	public boolean addModel(ReportModel model){
		reportDao.insert(model.getDate(), model);
		if(reportDao.containsModel(model.getDate(), model)){
			return true;
		}
		return false;
	}
	
	public boolean updateModel(ReportModel model){
		return addModel(model);
	}
	
	/*
	 * 1. origin data not exists, update to current data
	 * 2. origin data exists, add with current data
	 */
	public void addOrUpdate(ReportModel model){
		String key = model.getDate();
		if(!reportDao.containsKey(key)){
			logger.info("{}: key not exists, insert!", key);
			addModel(model);
		}else{
			logger.info("{}: key exists, add new and old!", key);
			ReportModel old = getReportModel(key);
			
			Long c = model.getCapital();
			Long t = model.getTotal();
			Long i = model.getInterest();
			
			model.setCapital(c + old.getCapital());
			model.setTotal(t + model.getTotal());
			model.setInterest(i + old.getInterest());
			updateModel(model);
		}
		
	}
	
	public void addOrUpdateCurrentDate(String date){
		reportDao.addCurrentDate(date);
	}
	
	public List<String> getAllDates(){
		return reportDao.getAllKeys();
	}
	
	public List<ReportModel> getAllModels(){
		return reportDao.getAllModels();
	}
	
	public ReportModel getReportModel(String date){
		return reportDao.get(date);
	}
	
	private ReportModel addTwoModels(ReportModel m1, ReportModel m2){
		ReportModel m = new ReportModel();
//		Assert.assertEquals(m1.getDate(), m2.getDate());
		Assert.isTrue(m1.getDate().equals(m2.getDate()));
		m.setDate(m1.getDate());
		m.setTotal(m1.getTotal() + m2.getTotal());
		m.setCapital(m1.getCapital() + m2.getCapital());
		m.setInterest(m1.getInterest() + m2.getInterest());
		return m;
	}
	
	
	public boolean deleteModel(ReportModel model){
		reportDao.deleteHash(model.getDate());
		if(!reportDao.containsModel(model.getDate(), model)){
			return true;
		}
		return false;
	}
	
	public boolean containsModel(ReportModel model){
		return reportDao.containsModel(model.getDate(), model);
	}
	
	public void updateCurrentDate(String currentDate){
		reportDao.addCurrentDate(currentDate);
	}
	
	public String getCurrentDate(){
		return reportDao.getCurrentDate();
	}
	
	public ReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}
	
	
	
}
