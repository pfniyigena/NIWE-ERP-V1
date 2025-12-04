package com.niwe.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.invoicing.domain.InvoiceChargeSummary;


public interface InvoiceChargeSummaryRepository  extends JpaRepository<InvoiceChargeSummary, UUID>{

}
