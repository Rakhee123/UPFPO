package com.brightleaf.exportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ExportServiceApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ExportServiceApplication.class, args);
	}
}
