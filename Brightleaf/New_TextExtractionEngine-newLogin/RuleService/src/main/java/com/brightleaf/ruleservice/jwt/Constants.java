package com.brightleaf.ruleservice.jwt;

public class Constants {
	
	 private Constants() {
		    throw new IllegalStateException("Utility class");
		  }

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60L;
    public static final String SIGNING_KEY = "Br!GhtLe@f";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
