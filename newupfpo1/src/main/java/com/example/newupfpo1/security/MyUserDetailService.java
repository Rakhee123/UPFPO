package com.example.newupfpo1.security;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.newupfpo1.models.User;
import com.example.newupfpo1.repository.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// for static username
		//return new myUserDetail(username);
		
		//for dynamic user check from database
		System.err.println("hello username "+username);
		User user = userRepository.findByUserName(username);
		return new myUserDetail(user);
	}

}
