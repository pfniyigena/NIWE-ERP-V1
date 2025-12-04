package com.niwe.erp.sale.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID>{
	
	Optional<PaymentMethod> findByNameContainingIgnoreCase(String name);

}
