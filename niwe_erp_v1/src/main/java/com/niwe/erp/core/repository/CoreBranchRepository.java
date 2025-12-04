package com.niwe.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreBranch;


public interface CoreBranchRepository extends JpaRepository<CoreBranch, UUID>{
	
	
	CoreBranch  findByInternalCode(String internalCode);
	List<CoreBranch> findByTaxpayerId(UUID id);

}
