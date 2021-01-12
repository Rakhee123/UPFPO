package com.brightleaf.usercompanyservice.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.usercompanyservice.model.LoginDetails;
import com.brightleaf.usercompanyservice.service.LoginDetailsService;

@CrossOrigin(origins = "*")
@RestController
public class LoginDetailsResource {

	@Autowired
	private LoginDetailsService loginDetailsService;	
	

	@GetMapping("/getLoginDetailsList")
	public List<LoginDetails> getLoginDetailsList() {
		return loginDetailsService.getLoginDetailsList();
	}

	// GET USERINFO 
	@PostMapping("/getUserInfo")
	public boolean changeForgotPassword(@RequestBody String userInfo) {

		// return loginDetailsService.getLoggedInUser(userId)
		return true;
	}

}
