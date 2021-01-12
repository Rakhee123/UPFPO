package com.brightleaf.executeservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
	 public UserDetails loadUserByUserName(String userName);

}
