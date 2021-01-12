package com.brightleaf.mongoservice.jwt;

public class Constants {
	
	 private Constants() {
		    throw new IllegalStateException("Utility class");
		  }

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60L;
    public static final String SIGNING_KEY = "Br!GhtLe@f";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String DOCUMENT_LIST = "documentList";
    public static final String DOCUMENT_NAME = "documentName";
    public static final String ATTRIBUTE_LIST = "attributes";
    
    public static final String NEWVALUE="NewValue";
    public static final String INITIALVALUE="InitialValue";
    public static final String STATUS="Status";
    public static final String QCDONEBY="QcDoneBy";
    public static final String QCLEVEL="QcLevel";
    public static final String IGNORERESULT="IgnoreResult";
    public static final String QC="QC";





}
