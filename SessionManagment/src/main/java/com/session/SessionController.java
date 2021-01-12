package com.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

	@GetMapping("/login")
	String login(HttpServletRequest request){
		HttpSession session= request.getSession();
		session.setAttribute("name", "xyz");
		return "login success";
	}
	
	@GetMapping("/hello")
	String hello(){
//		HttpSession session= request.getSession();
//		session.setAttribute("name", "xyz");
		return "hello friend";
	}
	

}
