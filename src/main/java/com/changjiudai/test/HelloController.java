package com.changjiudai.test;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.changjiudai.bean.Cagent;

@Controller
@RequestMapping("/hello")
public class HelloController {

	private static final Logger logger = Logger.getLogger(HelloController.class);

	@RequestMapping(value = "/say", method = RequestMethod.GET)
	public String sayHello(ModelMap model) {
		logger.info("receive the request...");

		// logs debug message
		if (logger.isDebugEnabled()) {
			logger.debug("getWelcome is executed!");
		}

		model.addAttribute("message", "test hello mvc controller");
		Dog dog = new Dog("001", "jack", 12);
		model.put("dog", dog);
		
		return "hello";
	}
	
	@RequestMapping(value = "/session", method = RequestMethod.GET)
	public String sayAgent(ModelMap model, HttpSession session) {
		logger.info("receive the request...");
		
		// logs debug message
		if (logger.isDebugEnabled()) {
			logger.debug("getWelcome is executed!");
		}
		
		logger.info("===========" + session.getAttribute("cagent"));
		
		model.addAttribute("message", "skjkdfklsjbdfjweklffbl");
		Cagent c = new Cagent();
		c.setCaptchaName(System.currentTimeMillis() + "");
		model.put("c", c);
		
		return "session";
	}
}
