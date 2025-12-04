package com.niwe.erp.sale.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.DailySalesSummary;

public interface DailySalesSummaryRepository extends JpaRepository<DailySalesSummary, UUID> {

	Optional<DailySalesSummary> findBySummaryDate(LocalDate summaryDate);

}
