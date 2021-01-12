package com.brightleaf.executeservice.serviceimpl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.brightleaf.executeservice.model.UserInfo;
import com.brightleaf.executeservice.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
    @Autowired
    private UserRepository userRepository;

	@Override
    @Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String userName)  {
		 UserInfo user = userRepository.findByUserName(userName);
	        if (user == null) throw new UsernameNotFoundException(userName);

	        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();


	        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(), grantedAuthorities);
	}
}
