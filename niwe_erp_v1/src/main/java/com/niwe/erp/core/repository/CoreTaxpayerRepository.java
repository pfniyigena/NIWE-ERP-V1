package com.niwe.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.core.domain.CoreTaxpayer;


public interface CoreTaxpayerRepository extends JpaRepository<CoreTaxpayer, UUID>{
	CoreTaxpayer  findByTinNumber(String tinNumber);
}
