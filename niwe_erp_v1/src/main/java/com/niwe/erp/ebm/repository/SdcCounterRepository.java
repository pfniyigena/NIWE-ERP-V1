package com.niwe.erp.ebm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.ebm.domain.SdcCounter;
import com.niwe.erp.ebm.domain.SdcInformation;

public interface SdcCounterRepository extends JpaRepository<SdcCounter, UUID> {

	SdcCounter findBySdcInformation(SdcInformation sdcInformation);

}
