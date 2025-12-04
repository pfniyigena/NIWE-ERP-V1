package com.niwe.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.invoicing.domain.Invoice;

 

public interface InvoiceRepository extends JpaRepository<Invoice, UUID>  {

}
