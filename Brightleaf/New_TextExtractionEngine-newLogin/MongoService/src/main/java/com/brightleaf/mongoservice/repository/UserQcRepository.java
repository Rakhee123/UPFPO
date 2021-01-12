package com.brightleaf.mongoservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.brightleaf.mongoservice.model.UserQC;

@Repository
public interface UserQcRepository extends JpaRepository<UserQC, Integer>{
	
	@Query(name = "FIND_USER_QC")
	UserQC getUserQc(@Param("transactionId") String transactionId, @Param("qcLevel") Integer qcLevel);
	
	@Query(name = "FIND_TANSACTIONS")
	List<UserQC> getTransactions(@Param("transactionId") String transactionId);
}
