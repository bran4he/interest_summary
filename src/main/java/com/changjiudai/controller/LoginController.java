package com.changjiudai.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.changjiudai.bean.Cagent;
import com.changjiudai.service.LoginService;
import com.changjiudai.service.SumService;

@Controller
@RequestMapping("/login")
public class LoginController {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private SumService sumService;
	
	@RequestMapping(value="index", method=RequestMethod.GET)
	public String chartsPage(){
		return "login";
	}
	
	
	@RequestMapping(value="code", method=RequestMethod.GET)
	public @ResponseBody Cagent getImageCode(HttpSession session){
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		if(null == cagent){
			cagent =  new Cagent();
		}
		loginService.setImagePath(cagent);
		session.setAttribute("cagent", cagent);
		return cagent;
	}
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public @ResponseBody Cagent login(HttpSession session,
			@RequestParam("username")String userName,
			@RequestParam("password")String passWord,
			@RequestParam("imgcode")String code){
		
		logger.info("============username: {}", userName);
		logger.info("============password:{}", passWord);
		logger.info("============imgcode:{}", code);
		
		Cagent cagent = (Cagent) session.getAttribute("cagent");
		cagent.setUserName(userName);
		cagent.setPassWord(passWord);
		cagent.setCode(code);
		
		loginService.login(cagent);
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(); 
		service.scheduleAtFixedRate(new BackRun(sumService, cagent), 1, 5, TimeUnit.MINUTES);
		
		return cagent;
		
	}
	
	@RequestMapping(value="gocharts", method=RequestMethod.GET)
	public String goCharts(HttpSession session){
		return "redirect:/sum/index";
	}
}
