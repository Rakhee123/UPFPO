package com.brightleaf.reportservice.serviceimpl;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brightleaf.reportservice.model.UserInfo;
import com.brightleaf.reportservice.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static Logger logger = Logger.getLogger(UserDetailsServiceImpl.class);
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userName) {
		UserInfo user = userRepository.findByUserName(userName);
		if (user == null) {
			logger.error("Error in loadUserByUsername " + userName);
			throw new UsernameNotFoundException(userName);
		}

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),
				grantedAuthorities);
	}
}
