package com.niwe.erp.inventory.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

	Optional<Supplier> findBySupplierTin(String supplierTin);

}
