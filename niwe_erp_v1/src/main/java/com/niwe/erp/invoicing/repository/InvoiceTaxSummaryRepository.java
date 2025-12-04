package com.niwe.erp.invoicing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.invoicing.domain.InvoiceTaxSummary;

 

public interface InvoiceTaxSummaryRepository extends JpaRepository<InvoiceTaxSummary, UUID>{

	List<InvoiceTaxSummary>  findByInvoiceIdOrderByTaxDisplayLevelAsc(UUID id);
}
