package com.brightleaf.usercompanyservice.serviceimpl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.brightleaf.usercompanyservice.model.UserInfo;
import com.brightleaf.usercompanyservice.repository.UserInfoRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserInfoRepository userInfoRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) {
		UserInfo user = userInfoRepository.findByUserName(userName);
		if (user == null)
			throw new UsernameNotFoundException(userName);

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),
				grantedAuthorities);
	}
}