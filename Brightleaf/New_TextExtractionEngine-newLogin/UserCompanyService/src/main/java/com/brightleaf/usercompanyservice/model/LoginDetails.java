package com.brightleaf.usercompanyservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "login_detail", catalog = "textextractionengine")
@NamedQuery(name = "UPDATE_LOGIN_DETAILS", query = "update LoginDetails ld set ld.logoutTime = :logoutTime, ld.logoutMethod = :logoutMethod where ld.logoutMethod =null and userId = :userId and sessionId= :sessionId")
@NamedQuery(name = "GET_LOGIN_DETAILS_BY_USERID", query = "SELECT lg FROM LoginDetails lg WHERE lg.userId = :userId and logoutMethod =null")
@NamedQuery(name = "UPDATE_LOGIN_DETAILS_FOR_COMPANY_ID", query = "update LoginDetails ld set ld.companyId = :companyId where userId = :userId and sessionId= :sessionId")
@NamedQuery(name = "DELETE_LOGIN_DETAILS_FOR_USERID_COMPANYID", query = "SELECT lgd FROM LoginDetails lgd WHERE lgd.userId = :userId and lgd.companyId = :companyId")

public class LoginDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "login_detail_id")
	private Integer loginDetailId;

	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "company_id")
	private Integer companyId;

	@Column(name = "login_time")
	private Date loginTime;

	@Column(name = "logout_time")
	private Date logoutTime;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "session_id")
	private String sessionId;

	@Column(name = "logout_method")
	private String logoutMethod;

	public Integer getLoginDetailId() {
		return loginDetailId;
	}

	public void setLoginDetailId(Integer loginDetailId) {
		this.loginDetailId = loginDetailId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getLogoutMethod() {
		return logoutMethod;
	}

	public void setLogoutMethod(String logoutMethod) {
		this.logoutMethod = logoutMethod;
	}
}
