package com.brightleaf.resultservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.resultservice.model.UserQc;
import com.brightleaf.resultservice.repository.UserQcRepository;
import com.brightleaf.resultservice.service.UserQcService;

@Service
public class UserQcServiceImpl implements UserQcService {
	
	@Autowired UserQcRepository userQcRepository;

	@Override
	public List<UserQc> getUserQcList(Integer qcTid, String companyName) {
		List<UserQc> lst =  userQcRepository.findUserOcByQcId(qcTid, companyName);
		if (qcTid > 1) {
			List<UserQc> retLst = new ArrayList<UserQc>();
			for (UserQc uq: lst) {
				List<UserQc> prevRec = userQcRepository.findUserQcByQcIdTrnxId(qcTid-1, companyName, uq.getTransactionId());
				if (prevRec != null && prevRec.size() > 0) {
					String assignedTo = prevRec.get(0).getAssignedTo();
					if (assignedTo != null && !assignedTo.contentEquals(""))
						retLst.add(uq);
				}
			}
			return retLst;
		} else {
			return lst;
		}
	}

	@Override
	public UserQc getUserQcByTxnIdAndQcLevel(Integer qcLevel, String transactionId) {
		return userQcRepository.findTransactionByTxQc(transactionId, qcLevel);
	}

	@Override
	public UserQc updateAssingedUser(UserQc object) {
		return userQcRepository.save(object);
	}

}
