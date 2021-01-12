package com.brightleaf.resultservice.serviceimpl;

import org.springframework.core.io.FileSystemResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.brightleaf.resultservice.service.ResultService;

@Service
public class ResultServiceImpl implements ResultService{
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public void displaydata() {
		logger.info("in result service impl");		
	}

	@Override
	public Resource getFileSystem(String path) {
		return getResource(path);
	}
	
	private Resource getResource(String path) {
		Resource resource = null;
		resource = new FileSystemResource(path);
		return resource;
	}
	

	
}
