package com.brightleaf.resultservice.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.resultservice.service.ResultService;

@CrossOrigin(origins = "*")
@RestController
public class ResultResource {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	ResultService resultService;
	

	@GetMapping("/displayresult")
	public String exportDcumentWise() {

		resultService.displaydata();
		return "done";
	}

	@GetMapping(path = "/downloadDocument/{docName}/{companyId}/{txn_id}", produces = "application/octet-stream")
	public Resource downloadDocument(@PathVariable("docName") String docName,
			@PathVariable("companyId") Integer companyId, @PathVariable("txn_id") String txnId) {
		String path = System.getProperty("catalina.base") + "/webapps/sourcepdf/" + companyId + "/" + txnId + "/"
				+ docName;
//	    String path="C:\\Tomcat-Dev\\webapps\\sourcepdf\\"+companyId+"\\"+txnId+"\\"+docName;
		return resultService.getFileSystem(path);
	}
	
}
