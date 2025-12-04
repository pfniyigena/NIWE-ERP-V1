package com.niwe.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.invoicing.domain.ChargeType;

public interface ChargeTypeRepository  extends JpaRepository<ChargeType, UUID> {
	
	ChargeType  findByChargeCode(String chargeCode);

}
