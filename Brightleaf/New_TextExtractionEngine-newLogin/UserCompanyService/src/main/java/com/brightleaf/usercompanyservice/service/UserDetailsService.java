package com.brightleaf.usercompanyservice.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public interface UserDetailsService {
	public UserDetails findByUserName(String userName);
}
