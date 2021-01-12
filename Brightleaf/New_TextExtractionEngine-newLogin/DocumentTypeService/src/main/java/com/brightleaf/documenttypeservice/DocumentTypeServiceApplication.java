package com.brightleaf.documenttypeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableJpaRepositories(basePackages = "com.brightleaf.documenttypeservice.repository")
@SpringBootApplication
public class DocumentTypeServiceApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(DocumentTypeServiceApplication.class, args);
	}

}
