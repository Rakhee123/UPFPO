package com.brightleaf.executeservice.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public interface ExecuteService {
	
	
	    public String uploadFilesFromLocal(final List<MultipartFile> filesUploaded, String transactionId);
	    
		public String generateTransactionId(int companyId, String identifier) ;
		
}
