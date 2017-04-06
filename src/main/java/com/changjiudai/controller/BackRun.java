package com.changjiudai.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.changjiudai.bean.Cagent;
import com.changjiudai.service.SumService;

public class BackRun implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(BackRun.class);
	
	private SumService sumService;
	
	private Cagent cagent;
	
	public BackRun(SumService sumService, Cagent cagent) {
		super();
		this.sumService = sumService;
		this.cagent = cagent;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		logger.info("scheduleAtFixedRate and current:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		sumService.sign(cagent);
		logger.info("scheduleAtFixedRate end");
	}

}
