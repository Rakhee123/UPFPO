package com.brightleaf.resultservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.brightleaf.resultservice.model.UserQc;

@Component
public interface UserQcService {

	List<UserQc> getUserQcList(Integer qcTid, String companyName);

	UserQc getUserQcByTxnIdAndQcLevel(Integer qcLevel, String transactionId);

	UserQc updateAssingedUser(UserQc object);

}
