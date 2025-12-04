package com.niwe.erp.ebm.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.ebm.domain.SdcDailyReport;
import com.niwe.erp.ebm.domain.SdcInformation;

public interface SdcDailyReportRespository extends JpaRepository<SdcDailyReport, UUID> {

	SdcDailyReport  findBySdcInformationAndReportDate(SdcInformation sdcInformation,LocalDate reportDate);
}
