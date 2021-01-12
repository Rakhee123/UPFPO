package com.brightleaf.usercompanyservice.model;

public class AuthToken {

	private String token;
	private String username;
	private String sessionId;

	public AuthToken() {

	}

	public AuthToken(String token, String username, String sessionId) {
		this.token = token;
		this.username = username;
		this.sessionId=sessionId;
		
	}

	public AuthToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
}
