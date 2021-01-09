package com.example.newupfpo1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public String Home()
	{
		return "welcome home";
	}
	
	@GetMapping("/user")
	public String user()
	{
		return "welcome user";
	}
}
