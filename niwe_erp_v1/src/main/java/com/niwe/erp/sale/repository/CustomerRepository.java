package com.niwe.erp.sale.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.core.dto.CustomerDTO;
import com.niwe.erp.sale.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	Optional<Customer> findByCustomerTin(String customerTin);

	@Query("""
		    SELECT new com.niwe.erp.core.dto.CustomerDTO(
		    c.customerName,
		    c.internalCode,
		    c.customerTin,
		    c.customerPhone,
		    c.customerEmail
		    )
		    FROM Customer c
		""")
	Page<CustomerDTO> findAllAsDto(Pageable pageable);
	
	@Query("""
		    SELECT new com.niwe.erp.core.dto.CustomerDTO(
		    c.customerName,
		    c.internalCode,
		    c.customerTin,
		    c.customerPhone,
		    c.customerEmail
		    )
		    FROM Customer c
		    WHERE c.lastUpdated > :lastUpdated
		""")
	Page<CustomerDTO> findByLastUpdatedAfter(@Param("lastUpdated") LocalDateTime lastUpdated, Pageable pageable);

}
