package com.example.newupfpo1.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.newupfpo1.models.User;

public class myUserDetail implements UserDetails{
	
	private String userName;
	private String password;
	private Boolean isEnabled;
	private String roleRefId;
	
	//for static user
//	public myUserDetail(String userName) {
//		this.userName = userName;
//	}

	//for dynamic user
	public myUserDetail(User user) {
		this.userName = user.getUserName();
		this.password = user.getPassword();
		this.isEnabled = user.isEnabled();
		this.roleRefId = user.getRoleRefId();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return isEnabled;
	}

}
