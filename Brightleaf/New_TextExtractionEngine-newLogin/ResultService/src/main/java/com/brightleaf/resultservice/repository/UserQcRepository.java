package com.brightleaf.resultservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.brightleaf.resultservice.model.UserQc;

public interface UserQcRepository extends JpaRepository<UserQc, Integer>{
	@Query(name = "FIND_USER_QC_BY_QCID")
	public List<UserQc> findUserOcByQcId(@Param("qcTid") Integer qcTid, @Param("companyName") String companyName);
	
	@Query(name = "FIND_TX_BY_QC_TXID")
	public UserQc findTransactionByTxQc(@Param("txName") String txName, @Param("qcTid") Integer qcTid);
	
		
	@Query(name = "GET_TX_BY_COMPANYID")
	public List<UserQc> getTransactionList();
	
	@Query(name = "FIND_USER_QC_BY_QCID_TRANSACTIONID")
	public List<UserQc> findUserQcByQcIdTrnxId(@Param("qcTid") Integer qcTid, @Param("companyName") String companyName,
											   @Param("trnxId") String trnxId);

}
