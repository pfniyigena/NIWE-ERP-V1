package com.niwe.erp.sale.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

	Optional<Customer> findByCustomerTin(String customerTin);

}
