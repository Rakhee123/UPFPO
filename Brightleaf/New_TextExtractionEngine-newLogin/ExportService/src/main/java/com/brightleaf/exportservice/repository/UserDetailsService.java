package com.brightleaf.exportservice.repository;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {
	 public UserDetails loadUserByUserName(String userName);

}
