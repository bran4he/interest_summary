package com.changjiudai.util;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

@Controller
public class ContextUtil {

	public static HttpSession getSession(HttpSession httpSession){
		return httpSession;
	}
	
	
}
