package com.niwe.erp.reconciliation.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.niwe.erp.reconciliation.domain.SaleDeclared;

public interface SaleDeclaredRepository extends JpaRepository<SaleDeclared, UUID>  {
	@Query("""
		    SELECT c FROM SaleDeclared c
		    WHERE c.receiptNumber NOT IN (
		        SELECT s.receiptNumber FROM SaleGenerated s
		    )
		""")
		List<SaleDeclared> findMissingDeclaredRecords();

}
