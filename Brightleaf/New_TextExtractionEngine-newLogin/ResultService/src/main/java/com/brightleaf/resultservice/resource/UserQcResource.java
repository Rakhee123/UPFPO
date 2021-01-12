package com.brightleaf.resultservice.resource;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.resultservice.model.UserQc;
import com.brightleaf.resultservice.service.UserQcService;

@CrossOrigin(origins = "*")
@RestController
public class UserQcResource {
	private static Logger logger = Logger.getLogger(UserQcResource.class);
	@Autowired
	private UserQcService userQcService;
	
	@GetMapping("/getUserQcList/{qcTid}/{companyName}")
	public List<UserQc> getUserQcList(@PathVariable("qcTid") final Integer qcTid, @PathVariable("companyName") final String companyName) {
		return userQcService.getUserQcList(qcTid, companyName);
	}
	
	
	@PostMapping(path = "/updateAssingedUser", consumes = "application/json", produces = "application/json")
	public String updateAssingendUser(@RequestBody String content) {
		logger.info(content);
		JSONObject jsonObject = new JSONObject(content);
		String transactionId = jsonObject.getString("transactionId");
		Integer qcLevel = jsonObject.getInt("qcLevel");
		String userName = jsonObject.getString("userName");
		UserQc object=userQcService.getUserQcByTxnIdAndQcLevel(qcLevel,transactionId);
		object.setAssignedTo(userName);
		System.err.println(object.toString());
		//return null;
		UserQc userWcoBject=userQcService.updateAssingedUser(object);
		//Set this user name as assigned by to the next QC level. 
		object = userQcService.getUserQcByTxnIdAndQcLevel(qcLevel + 1, transactionId);
		if (object != null) {
			object.setAssignedBy(userName);
			userQcService.updateAssingedUser(object);
		}
		return "sucess"; 
	}
	
	@GetMapping("/getUserQcByTxnIdAndQcLevel/{qcLevel}/{transactionId}")
	public UserQc getUserQcByTxnIdAndQcLevel(@PathVariable("qcLevel") final Integer qcLevel, @PathVariable("transactionId") final String transactionId) {
		return userQcService.getUserQcByTxnIdAndQcLevel(qcLevel, transactionId);
	}
	
}
