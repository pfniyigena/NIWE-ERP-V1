package com.niwe.erp.reconciliation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.niwe.erp.reconciliation.domain.SaleGenerated;

public interface SaleGeneratedRepository extends JpaRepository<SaleGenerated, UUID> {
	
	@Query("""
		    SELECT c FROM SaleGenerated c
		    WHERE c.receiptNumber NOT IN (
		        SELECT s.receiptNumber FROM SaleDeclared s
		    )
		""")
		List<SaleGenerated> findMissingGeneratedRecords();


}
