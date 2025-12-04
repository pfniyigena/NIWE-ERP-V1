package com.niwe.erp.purchase.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.purchase.domain.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
}
