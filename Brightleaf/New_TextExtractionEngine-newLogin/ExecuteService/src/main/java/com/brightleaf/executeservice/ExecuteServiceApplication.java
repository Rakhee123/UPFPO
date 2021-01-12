package com.brightleaf.executeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.brightleaf.executeservice.repository")
@SpringBootApplication
public class ExecuteServiceApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(ExecuteServiceApplication.class, args);
	}
}
