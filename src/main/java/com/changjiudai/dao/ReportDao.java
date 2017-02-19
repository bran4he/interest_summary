package com.changjiudai.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.changjiudai.model.ReportModel;
import com.changjiudai.util.RedisUtil;

@Repository
public class ReportDao {

	@Autowired
	private RedisTemplate redisTemplate;

	private String getReportDataKey(){
		return RedisUtil.getReportDataKey();
	}
	
	private String getReportCurrentDateKey(){
		return RedisUtil.getReportCurrentDateKey();
	}
	
	/**
	 * get model
	 * @param key
	 * @return
	 */
	public ReportModel get(String key) {
		ReportModel model = (ReportModel) redisTemplate.opsForHash().get(getReportDataKey(), key);
		return model;
	}

	/**
	 * add model
	 * @param key
	 * @param obj
	 */
	public void insert(String key, ReportModel obj) {
		redisTemplate.opsForHash().put(getReportDataKey(), key, obj);
	}

	/**
	 * update model
	 * @param key
	 * @param obj
	 */
	public void update(String key, ReportModel obj) {
		insert(key, obj);
	}

	/**
	 * delete model
	 * @param key
	 */
	public void deleteHash(String key) {
		redisTemplate.opsForHash().delete(getReportDataKey(), key);
	}
	
	public void deleteCurrentDate(){
		redisTemplate.delete(getReportCurrentDateKey());
	}
	
	public void deleteReportData(){
		redisTemplate.delete(getReportDataKey());
	}
	
	/**
	 * get all models
	 * @return
	 */
	public List<ReportModel> getAllModels() {
		return redisTemplate.opsForHash().values(getReportDataKey());
	}

	public List<String> getAllKeys(){
		return new ArrayList<String>(redisTemplate.opsForHash().keys(getReportDataKey()));
	}
	
	/**
	 * has key
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return redisTemplate.opsForHash().hasKey(getReportDataKey(), key);
	}

	/**
	 * exactly the same data, both key and model
	 * @param key
	 * @param obj
	 * @return
	 */
	public boolean containsModel(String key, ReportModel obj) {
		boolean keyExist = redisTemplate.opsForHash().hasKey(getReportDataKey(), key);
		if (keyExist) {
			ReportModel temp = (ReportModel) redisTemplate.opsForHash().get(getReportDataKey(), key);
			if (obj.equals(temp)) {
				return true;
			}
			return false;
		}
		return false;
	}

	public void addCurrentDate(String currentDate) {
		redisTemplate.opsForValue().set(getReportCurrentDateKey(), currentDate);
	}

	public String getCurrentDate() {
		return (String) redisTemplate.opsForValue().get(getReportCurrentDateKey());
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
