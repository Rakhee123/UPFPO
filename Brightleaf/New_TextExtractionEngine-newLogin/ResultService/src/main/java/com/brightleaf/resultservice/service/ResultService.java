package com.brightleaf.resultservice.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public interface ResultService {

	public void displaydata();

	public Resource  getFileSystem(String path);	

}
